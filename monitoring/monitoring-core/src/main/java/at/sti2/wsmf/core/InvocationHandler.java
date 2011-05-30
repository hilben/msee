/**
 * InvocationHandler.java - at.sti2.wsmf.core
 */
package at.sti2.wsmf.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import javax.management.InstanceNotFoundException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;
import at.sti2.wsmf.api.data.state.WSInvocationState;
import at.sti2.wsmf.core.common.Config;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;
import at.sti2.wsmf.core.data.WebServiceEndpoint;
import at.sti2.wsmf.core.data.channel.WSInvocationStateChannelHandler;
import at.sti2.wsmf.core.data.channel.WSQoSChannelHandler;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Alex Oberhauser
 */
public class InvocationHandler {
	private static Logger log = Logger.getLogger(InvocationHandler.class);
	private static final int TIME_OUT_MS = 12000;

	/**
	 * JAX-WS Invocation Implementation. 
	 * 
	 * @param _endpointURL
	 * @param _soapMessage
	 * @param _soapAction
	 * @return
	 * @throws SOAPException
	 * @throws IOException
	 */
	private static String _invoke(String _endpointURL, SOAPMessage _soapMessage, String _soapAction) throws SOAPException, IOException {
		Config cfg = Config.getInstance();
		QName serviceName = new QName(cfg.getWebServiceNamespace(), cfg.getWebServiceName());
		QName portName = serviceName;
		
		Service service = Service.create(serviceName);
		service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, _endpointURL);
		
		Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);

		if ( _soapAction == null ) {
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, false);
		} else {
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, true);
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, null);
		}
		 
		 SOAPMessage response = (SOAPMessage) dispatch.invoke(_soapMessage);
		 
		 ByteArrayOutputStream os = new ByteArrayOutputStream();
		 response.writeTo(os);
		 return os.toString();
	}
	
	/**
	 * TODO: Change this part to JAX-WS!!!
	 *  
	 * @param _endpoint
	 * @param _soapAction
	 * @param _message
	 * @throws XMLStreamException
	 * @throws AxisFault
	 */
	public static void sendChannelMessage(String _endpoint, String _soapAction, String _message) throws XMLStreamException, AxisFault {
		ServiceClient serviceClient = new ServiceClient();
		Options opts = new Options();
	    opts.setTo(new EndpointReference(_endpoint));
	    opts.setAction(_soapAction);
	    serviceClient.setOptions(opts);
	    
	    log.info("[ChannelMessage] Sending the following message to '" + _endpoint + "': " + _message);
	    OMElement omInput = AXIOMUtil.stringToOM(_message);
	    serviceClient.fireAndForget(omInput);
	    
		serviceClient.cleanup();
		serviceClient.cleanupTransport();
		serviceClient.removeHeaders();
	}

	public static boolean isWebServiceAvailable(String _endpoint, String _soapAction) {		
		try {
			MessageFactory msgFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = msgFactory.createMessage();
			SOAPBody soapBody = soapMessage.getSOAPBody();
			soapBody.addChildElement(new QName("ftp://wrongnamespace", "a" + UUID.randomUUID().toString()));
			soapMessage.saveChanges();
			_invoke(_endpoint, soapMessage, _soapAction);
			return true;
		} catch (SOAPFaultException e) {
			return true;
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static void sendStateChange(ActivityInstantiatedEvent _activeInstance) {
		try {
			WSInvocationStateChannelHandler stateChannel = WSInvocationStateChannelHandler.getInstance();
			stateChannel.sendState(Config.getInstance().getInstancePrefix() + _activeInstance.getIdentifier(), _activeInstance.getState());
		} catch (QueryEvaluationException e) {
			log.error("Not able to send state changed message, through exception: " + e.getLocalizedMessage());
		} catch (MalformedQueryException e) {
			log.error("Not able to send state changed message, through exception: " + e.getLocalizedMessage());
		} catch (RepositoryException e) {
			log.error("Not able to send state changed message, through exception: " + e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Not able to send state changed message, through exception: " + e.getLocalizedMessage());
		}
	}
	
	private static void sendQoSValue(ActivityInstantiatedEvent _activeInstance, QoSParamValue _value) {
		try {
			WSQoSChannelHandler qosChannel = WSQoSChannelHandler.getInstance();
			qosChannel.sendState(Config.getInstance().getInstancePrefix() + _activeInstance.getIdentifier(), _value);
		} catch (QueryEvaluationException e) {
			log.error("Not able to send qos value message, through exception: " + e.getLocalizedMessage());
		} catch (RepositoryException e) {
			log.error("Not able to send qos value message, through exception: " + e.getLocalizedMessage());
		} catch (MalformedQueryException e) {
			log.error("Not able to send qos value message, through exception: " + e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Not able to send qos value message, through exception: " + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 
	 * @param _soapRequest The request message encoded in SOAP
	 * @param _soapAction HTTP header field 'SOAPAction'
	 * @return 
	 * @throws Exception 
	 * @throws InstanceNotFoundException 
	 */
	public static String invoke(SOAPMessage _soapMessage, String _soapAction, ActivityInstantiatedEvent _activeInstance, int _soapMessageSize) throws Exception {
		WSAvailabilityChecker.getInstance(1);
		EndpointHandler endpointHandler = EndpointHandler.getInstance();
		WebServiceEndpoint currentWS = endpointHandler.getCurrentActiveWS();
		String endpoint = currentWS.getEndpoint().toExternalForm();

		log.info("WS Endpoint      : " + endpoint);
		log.info("SOAP Action      : " + _soapAction);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			_soapMessage.writeTo(os);
			log.info("SOAP Message     : " + os);
		} catch (SOAPException e3) {
			log.error("Could not print out the SOAP Message!");
		}
		
        ServiceClient serviceClient = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference(endpoint));
        opts.setAction(_soapAction);
	    opts.setTimeOutInMilliSeconds(TIME_OUT_MS);
        serviceClient.setOptions(opts);
        
        /* ***************************
         * Monitoring Block Start    */
        _activeInstance.setEndpoint(endpoint);
		_activeInstance.changeInvocationStatus(WSInvocationState.Instantiated);
		sendStateChange(_activeInstance);
		try {
			int totalRequests = currentWS.incrementeTotalRequests();
			sendQoSValue(_activeInstance, new QoSParamValue(QoSParamKey.RequestTotal, totalRequests + "", QoSUnit.Requests));
		} catch (QueryEvaluationException e1) {
			log.error("Not able to increment total requests value, through exception: " + e1.getLocalizedMessage());
		} catch (RepositoryException e1) {
			log.error("Not able to increment total requests value, through exception: " + e1.getLocalizedMessage());
		} catch (MalformedQueryException e1) {
			log.error("Not able to increment total requests value, through exception: " + e1.getLocalizedMessage());
		}
		try {
			currentWS.changePayloadSize(_soapMessageSize);
		} catch (QueryEvaluationException e2) {
			log.error("Not able to change payload size, through exception: " + e2.getLocalizedMessage());
		} catch (MalformedQueryException e2) {
			log.error("Not able to change payload size, through exception: " + e2.getLocalizedMessage());
		} 

		/* Monitoring Block End
         *****************************/
        
		try {
	        String result;
	        
	        /* ***************************
	         * Monitoring Block Start    */
	        _activeInstance.changeInvocationStatus(WSInvocationState.Started);
			sendStateChange(_activeInstance);
			/* Monitoring Block End
	         *****************************/
			
			long beforeInvocation = System.currentTimeMillis();
			result = _invoke(endpoint, _soapMessage, _soapAction);	    // <<= Invocation
			long afterInvocation = System.currentTimeMillis();
			try {
				long responseTime = (afterInvocation - beforeInvocation);
				Vector<QoSParamValue> changedValues = currentWS.changeResponseTime(responseTime);
				for ( QoSParamValue entry : changedValues )
					sendQoSValue(_activeInstance, entry);
			} catch (RepositoryException e1) {
				log.error("Not able to change the response time, through exception: " + e1.getLocalizedMessage());
			} catch (QueryEvaluationException e1) {
				log.error("Not able to change the response time, through exception: " + e1.getLocalizedMessage());
			} catch (MalformedQueryException e1) {
				log.error("Not able to change the response time, through exception: " + e1.getLocalizedMessage());
			}
			
			/* ***************************
	         * Monitoring Block Start    */
			_activeInstance.changeInvocationStatus(WSInvocationState.Completed);
			sendStateChange(_activeInstance);
			try {
				int successfulRequests = currentWS.incrementeSuccessfulRequests();
				sendQoSValue(_activeInstance, new QoSParamValue(QoSParamKey.RequestSuccessful, successfulRequests + "", QoSUnit.Requests));
			} catch (QueryEvaluationException e) {
				log.error("Not able to increment successful requests value, through exception: " + e.getLocalizedMessage());
			} catch (RepositoryException e) {
				log.error("Not able to increment successful requests value, through exception: " + e.getLocalizedMessage());
			} catch (MalformedQueryException e) {
				log.error("Not able to increment successful requests value, through exception: " + e.getLocalizedMessage());
			}
			/* Monitoring Block End
	         *****************************/
			
	        return result;
		} catch (SOAPException e) {
			/* ***************************
	         * Monitoring Block Start    */
			_activeInstance.changeInvocationStatus(WSInvocationState.Terminated);
			sendStateChange(_activeInstance);
			try {
				int failedRequests = currentWS.incrementeFailedRequests();
				sendQoSValue(_activeInstance, new QoSParamValue(QoSParamKey.RequestFailed, failedRequests + "", QoSUnit.Requests));
			} catch (QueryEvaluationException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: " + e.getLocalizedMessage());
			} catch (RepositoryException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: " + e.getLocalizedMessage());
			} catch (MalformedQueryException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: " + e.getLocalizedMessage());
			}
			/* Monitoring Block End
	         *****************************/
			throw new SOAPException(e);
		} catch (Exception e) {
			/* ***************************
	         * Monitoring Block Start    */
			_activeInstance.changeInvocationStatus(WSInvocationState.Terminated);
			sendStateChange(_activeInstance);
			try {
				int failedRequests = currentWS.incrementeFailedRequests();
				sendQoSValue(_activeInstance, new QoSParamValue(QoSParamKey.RequestFailed, failedRequests + "", QoSUnit.Requests));
			} catch (QueryEvaluationException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: " + e.getLocalizedMessage());
			} catch (RepositoryException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: " + e.getLocalizedMessage());
			} catch (MalformedQueryException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: " + e.getLocalizedMessage());
			}
			/* Monitoring Block End
	         *****************************/
			throw new Exception(e);
		} finally {			
			serviceClient.cleanup();
			serviceClient.cleanupTransport();
			serviceClient.removeHeaders();
		}
	}

}