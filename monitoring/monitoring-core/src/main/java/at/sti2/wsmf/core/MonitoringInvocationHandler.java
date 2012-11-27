/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.wsmf.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.management.InstanceNotFoundException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;
import at.sti2.wsmf.api.data.state.WSInvocationState;
import at.sti2.wsmf.core.availability.WSAvailabilityChecker;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;
import at.sti2.wsmf.core.data.WebServiceEndpoint;
import at.sti2.wsmf.core.data.channel.WSInvocationStateChannelHandler;
import at.sti2.wsmf.core.data.channel.WSQoSChannelHandler;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Alex Oberhauser
 * 
 * @author Benjamin Hiltpolt The {@link MonitoringInvocationHandler} invokes web
 *         services and stores monitoring informations about the webservice also
 *         it starts an AvailabilityChecker for the invoked webservice
 *         {TODO: removed use of {@link WSQoSChannelHandler} maybe reuse later?}
 * 
 */
public class MonitoringInvocationHandler {
	private static Logger log = Logger
			.getLogger(MonitoringInvocationHandler.class);
	
	private static final int TIME_OUT_MS = 12000;

	// TODO: change back to reasonable time
	private static final int WS_AVAILABILITY_TIMEOUT_MINUTES = 5;


	private static void sendQoSValue(ActivityInstantiatedEvent activeInstance,
			QoSParamValue value) {
		try {
			WSQoSChannelHandler qosChannel = WSQoSChannelHandler.getInstance();
			qosChannel.sendState(
					WebServiceEndpointConfig.getConfig(
							activeInstance.getEndpoint()).getInstancePrefix()
							+ activeInstance.getIdentifier(), value);
		} catch (QueryEvaluationException e) {
			log.error("Not able to send qos value message, through exception: "
					+ e.getLocalizedMessage());
		} catch (RepositoryException e) {
			log.error("Not able to send qos value message, through exception: "
					+ e.getLocalizedMessage());
		} catch (MalformedQueryException e) {
			log.error("Not able to send qos value message, through exception: "
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Not able to send qos value message, through exception: "
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * @param _soapRequest
	 *            The request message encoded in SOAP
	 * @param soapAction
	 *            HTTP header field 'SOAPAction'
	 * @return
	 * @throws Exception
	 * @throws InstanceNotFoundException
	 */
	public static String invokeWithMonitoring(SOAPMessage soapMessage,
			String soapAction, ActivityInstantiatedEvent activeInstance,
			int soapMessageSize) throws Exception {

		WSAvailabilityChecker.startAvailabilityChecking(
				activeInstance.getEndpoint(), WS_AVAILABILITY_TIMEOUT_MINUTES);

		WebServiceEndpoint currentWS = WebServiceEndpointConfig.getConfig(
				activeInstance.getEndpoint()).getWebServiceEndpoint();
		String endpoint = currentWS.getEndpoint().toExternalForm();

		log.info("WS Endpoint      : " + endpoint);
		log.info("SOAP Action      : " + soapAction);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(os);
			log.info("SOAP Message     : " + os);
		} catch (SOAPException e) {
			log.error("Could not print out the SOAP Message!");
			log.error(e.getCause());
		}

		Options opts = new Options();
		opts.setTo(new EndpointReference(endpoint));
		opts.setAction(soapAction);
		opts.setTimeOutInMilliSeconds(TIME_OUT_MS);

		/* ***************************
		 * Monitoring Block Start
		 */
		activeInstance.setEndpoint(endpoint);
		activeInstance.changeInvocationStatus(WSInvocationState.Instantiated);
		sendStateChange(activeInstance);
		try {
			int totalRequests = currentWS.incrementeTotalRequests();
			sendQoSValue(activeInstance, new QoSParamValue(
					QoSParamKey.RequestTotal, totalRequests + "",
					QoSUnit.Requests));
			
		} catch (QueryEvaluationException e) {
			log.error("Not able to increment total requests value, through exception: "
					+ e.getLocalizedMessage());
		} catch (RepositoryException e) {
			log.error("Not able to increment total requests value, through exception: "
					+ e.getLocalizedMessage());
		} catch (MalformedQueryException e) {
			log.error("Not able to increment total requests value, through exception: "
					+ e.getLocalizedMessage());
		}

//		activeInstance.setPayloadSizeRequest(soapMessageSize);
		currentWS.addQoSValue(new QoSParamValue(QoSParamKey.PayloadSizeRequest,Integer.toString(soapMessageSize),QoSUnit.Bytes));

		/*
		 * Monitoring Block End***************************
		 */

		try {
			String result;

			/* ***************************
			 * Monitoring Block Start
			 */
			activeInstance.changeInvocationStatus(WSInvocationState.Started);
			sendStateChange(activeInstance);
			/*
			 * Monitoring Block End***************************
			 */

			long beforeInvocation = System.currentTimeMillis();

			/*
			 * Invocation
			 */
			result = invokeWithoutMonitoring(endpoint, soapMessage,
					soapAction, null);
			/*
			 * End of Invocation
			 */

			/*
			 * Calculate the size of the response soap message
			 */
			SOAPMessage message = MonitoringInvocationHandler
					.generateSOAPMessage(result);

			message.saveChanges();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			message.writeTo(stream);

			String responseSize = Integer.toString(stream.size());
			QoSParamValue responseSizeQosParam = new QoSParamValue(QoSParamKey.PayloadSizeResponse,responseSize,QoSUnit.Bytes);
			currentWS.addQoSValue(responseSizeQosParam);


			long afterInvocation = System.currentTimeMillis();
			long responseTime = (afterInvocation - beforeInvocation);

//			activeInstance.setResponseTime(responseTime);
			QoSParamValue responseTimeQosParam = new QoSParamValue(QoSParamKey.ResponseTime,Long.toString(responseTime),QoSUnit.Milliseconds);
			currentWS.addQoSValue(responseTimeQosParam);



			/* ***************************
			 * Monitoring Block Start
			 */
			activeInstance.changeInvocationStatus(WSInvocationState.Completed);
			sendStateChange(activeInstance);
			try {
				int successfulRequests = currentWS
						.incrementeSuccessfulRequests();
				sendQoSValue(activeInstance, new QoSParamValue(
						QoSParamKey.RequestSuccessful, successfulRequests + "",
						QoSUnit.Requests));
			} catch (QueryEvaluationException e) {
				log.error("Not able to increment successful requests value, through exception: "
						+ e.getLocalizedMessage());
			} catch (RepositoryException e) {
				log.error("Not able to increment successful requests value, through exception: "
						+ e.getLocalizedMessage());
			} catch (MalformedQueryException e) {
				log.error("Not able to increment successful requests value, through exception: "
						+ e.getLocalizedMessage());
			}
			/*
			 * Monitoring Block End***************************
			 */

			return result;
		} catch (SOAPException e) {
			/* ***************************
			 * Monitoring Block Start
			 */
			activeInstance
					.changeInvocationStatus(WSInvocationState.Terminated);
			sendStateChange(activeInstance);
			try {
				int failedRequests = currentWS.incrementeFailedRequests();
				sendQoSValue(activeInstance, new QoSParamValue(
						QoSParamKey.RequestFailed, failedRequests + "",
						QoSUnit.Requests));
			} catch (QueryEvaluationException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: "
						+ e.getLocalizedMessage());
			} catch (RepositoryException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: "
						+ e.getLocalizedMessage());
			} catch (MalformedQueryException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: "
						+ e.getLocalizedMessage());
			}
			/*
			 * Monitoring Block End***************************
			 */
			throw new SOAPException(e);
		} catch (Exception e) {
			/* ***************************
			 * Monitoring Block Start
			 */
			activeInstance
					.changeInvocationStatus(WSInvocationState.Terminated);
			sendStateChange(activeInstance);
			try {
				int failedRequests = currentWS.incrementeFailedRequests();
				sendQoSValue(activeInstance, new QoSParamValue(
						QoSParamKey.RequestFailed, failedRequests + "",
						QoSUnit.Requests));
			} catch (QueryEvaluationException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: "
						+ e.getLocalizedMessage());
			} catch (RepositoryException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: "
						+ e.getLocalizedMessage());
			} catch (MalformedQueryException e1) {
				log.error("Not able to broadcast the new QoS value RequestFailed: "
						+ e.getLocalizedMessage());
			}
			/*
			 * Monitoring Block End***************************
			 */
			throw new Exception(e);
		} finally {
			// TODO: handle finally
		}
	}

	/**
	 * 
	 * Generates a SOAPMessage object out of a string
	 * 
	 * @param _soapMessageString
	 * @return
	 * @throws SOAPException
	 */
	public static SOAPMessage generateSOAPMessage(String _soapMessageString)
			throws SOAPException {
		MessageFactory msgFactory = MessageFactory.newInstance();
		SOAPMessage msg = msgFactory.createMessage();
		msg.getSOAPHeader();
		SOAPPart soapPart = msg.getSOAPPart();

		StreamSource msgSrc = new StreamSource(new StringReader(
				_soapMessageString));
		soapPart.setContent(msgSrc);
		msg.saveChanges();
		return msg;
	}

	/**
	 * JAX-WS Invocation Implementation.
	 * 
	 * Invocation without monitoring
	 * 
	 * @param _endpointURL
	 * @param _soapMessage
	 * @param _soapAction
	 * @return
	 * @throws SOAPException
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public static String invokeWithoutMonitoring(String _endpointURL,
			SOAPMessage _soapMessage, String _soapAction,
			WebServiceEndpointConfig config) throws SOAPException, IOException,
			RepositoryException {

		WebServiceEndpointConfig cfg = WebServiceEndpointConfig
				.getConfig(_endpointURL);

		QName serviceName = new QName(cfg.getWebServiceNamespace(),
				cfg.getWebServiceName());
		QName portName = serviceName;

		Service service = Service.create(serviceName);
		service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, _endpointURL);

		Dispatch<SOAPMessage> dispatch = service.createDispatch(portName,
				SOAPMessage.class, Service.Mode.MESSAGE);

		if (_soapAction == null) {
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY,
					false);
		} else {
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY,
					true);
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY,
					null);
		}

		SOAPMessage response = dispatch.invoke(_soapMessage);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		response.writeTo(os);
		os.close();

		return os.toString();
	}

	private static void sendStateChange(
			ActivityInstantiatedEvent _activeInstance) {
		try {
			WSInvocationStateChannelHandler stateChannel = WSInvocationStateChannelHandler
					.getInstance();
			stateChannel.sendState(
					WebServiceEndpointConfig.getConfig(
							_activeInstance.getEndpoint()).getInstancePrefix()
							+ _activeInstance.getIdentifier(),
					_activeInstance.getState());
		} catch (QueryEvaluationException e) {
			log.error("Not able to send state changed message, through exception: "
					+ e.getLocalizedMessage());
		} catch (MalformedQueryException e) {
			log.error("Not able to send state changed message, through exception: "
					+ e.getLocalizedMessage());
		} catch (RepositoryException e) {
			log.error("Not able to send state changed message, through exception: "
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Not able to send state changed message, through exception: "
					+ e.getLocalizedMessage());
		}
	}

	
}
