/**
 * ManagementWebService.java - at.sti2.ngsee.monitoring.webservice
 */
package at.sti2.ngsee.monitoring.webservice;

import java.net.URL;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdValue;
import at.sti2.wsmf.api.data.state.WSInvocationState;
import at.sti2.wsmf.api.ws.IManagementWebService;
import at.sti2.wsmf.core.EndpointHandler;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Alex Oberhauser
 */
@WebService
public class ManagementWebService implements IManagementWebService {

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#listInstanceIDs()
	 */
	@WebMethod
	@Override
	public String[] listInstanceIDs() throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		Vector<String> resultVector = persHandler.getInstanceIDs();
		return resultVector.toArray(new String[resultVector.size()]);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getInvocationState(java.lang.String)
	 */
	@WebMethod
	@Override
	public WSInvocationState getInvocationState(@WebParam(name="activityStartedEvent")String _activityStartedEvent) throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		return persHandler.getInvocationState(_activityStartedEvent);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getQoSParam(java.lang.String)
	 */
	@WebMethod
	@Override
	public QoSParamValue getQoSParam(@WebParam(name="endpoint")URL _endpoint,
			@WebParam(name="key")QoSParamKey _key) throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		return persHandler.getQoSParam(_endpoint, _key);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#changeQoSThresholdValue(java.net.URL, at.sti2.wsmf.api.data.qos.IQoSThresholdValue)
	 */
	@WebMethod
	@Override
	public void changeQoSThresholdValue(@WebParam(name="endpoint")URL _endpoint, @WebParam(name="value")QoSThresholdValue _value)
			throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		persHandler.changeQoSThresholdValue(_endpoint, _value);
	}
	
	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getQoSThresholdValue(java.net.URL, at.sti2.wsmf.api.data.qos.QoSParamKey)
	 */
	@WebMethod
	@Override
	public QoSThresholdValue getQoSThresholdValue(@WebParam(name="endpoint")URL _endpoint,
			@WebParam(name="key")QoSThresholdKey _key) throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		return persHandler.getThresholdValue(_endpoint, _key);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#addEndpoint(java.lang.String)
	 */
	@WebMethod
	@Override
	public void addEndpoint(@WebParam(name="endpoint")URL _endpoint) throws Exception {
		new at.sti2.wsmf.core.data.WebServiceEndpoint(_endpoint);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#listEndpoints()
	 */
	@Override
	public URL[] listEndpoints() throws Exception {
		EndpointHandler handler = EndpointHandler.getInstance();
		Vector<URL> fallbackWS = handler.getFallbackWS();
		return fallbackWS.toArray(new URL[fallbackWS.size()]);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#removeEndpoint()
	 */
	@WebMethod
	@Override
	public void removeEndpoint(@WebParam(name="endpoint")URL _endpoint) throws Exception {
		EndpointHandler handler = EndpointHandler.getInstance();
		handler.removeFallbackWS(_endpoint);
		PersistentHandler.getInstance().deleteEndpoint(_endpoint);
	}
	
}
