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
package at.sti2.msee.monitoring.webservice;

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
