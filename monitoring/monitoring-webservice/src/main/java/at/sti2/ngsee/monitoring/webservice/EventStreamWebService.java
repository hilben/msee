/**
 * EventStreamWebService.java - at.sti2.ngsee.monitoring.webservice
 */
package at.sti2.ngsee.monitoring.webservice;

import java.net.URL;

import javax.jws.WebMethod;
import javax.jws.WebParam;

import at.sti2.wsmf.api.ws.IEventStreamWebService;
import at.sti2.wsmf.core.data.channel.WSAvailabilityChannelHandler;
import at.sti2.wsmf.core.data.channel.WSInvocationStateChannelHandler;
import at.sti2.wsmf.core.data.channel.WSQoSChannelHandler;

/**
 * @author Alex Oberhauser
 *
 */
public class EventStreamWebService implements IEventStreamWebService {

	/**
	 * @see at.sti2.wsmf.api.ws.IEventStreamWebService#registerStateChannel(java.lang.String, java.lang.String, java.lang.String)
	 */
	@WebMethod
	@Override
	public void subscribeStateChannel(@WebParam(name="endpoint")URL _endpoint,
			@WebParam(name="namespace")String _namespace,
			@WebParam(name="method")String _operationName,
			@WebParam(name="soapaction")String _soapAction) throws Exception {
		WSInvocationStateChannelHandler.getInstance().subscribe(_endpoint, _namespace, _operationName, _soapAction);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IEventStreamWebService#unregisterStateChannel(java.lang.String)
	 */
	@WebMethod
	@Override
	public void unsubscribeStateChannel(@WebParam(name="endpoint")URL _endpoint) throws Exception {
		WSInvocationStateChannelHandler.getInstance().unsubsribe(_endpoint);		
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IEventStreamWebService#registerAvailabilityChannel(java.lang.String, java.lang.String, java.lang.String)
	 */
	@WebMethod
	@Override
	public void subscribeAvailabilityChannel(@WebParam(name="endpoint")URL _endpoint,
			@WebParam(name="namespace")String _namespace,
			@WebParam(name="method")String _operationName,
			@WebParam(name="soapaction")String _soapAction) throws Exception {
		WSAvailabilityChannelHandler.getInstance().subscribe(_endpoint, _namespace, _operationName, _soapAction);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IEventStreamWebService#unregisterAvailabilityChannel(java.lang.String)
	 */
	@WebMethod
	@Override
	public void unsubscribeAvailabilityChannel(@WebParam(name="endpoint")URL _endpoint) throws Exception {
		WSAvailabilityChannelHandler.getInstance().unsubsribe(_endpoint);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IEventStreamWebService#registerQoSChannel(java.lang.String, java.lang.String, java.lang.String)
	 */
	@WebMethod
	@Override
	public void subscribeQoSChannel(@WebParam(name="endpoint")URL _endpoint,
			@WebParam(name="namespace")String _namespace,
			@WebParam(name="method")String _operationName,
			@WebParam(name="soapaction")String _soapAction) throws Exception {
		WSQoSChannelHandler.getInstance().subscribe(_endpoint, _namespace, _operationName, _soapAction);		
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IEventStreamWebService#unregisterQoSChannel(java.lang.String)
	 */
	@WebMethod
	@Override
	public void unsubscribeQoSChannel(@WebParam(name="endpoint")URL _endpoint) throws Exception {
		WSQoSChannelHandler.getInstance().unsubsribe(_endpoint);
	}

}
