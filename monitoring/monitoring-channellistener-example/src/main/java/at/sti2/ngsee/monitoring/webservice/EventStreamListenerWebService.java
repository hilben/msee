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
package at.sti2.ngsee.monitoring.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.sti2.wsmf.api.ws.channel.IAvailabilityChannelListener;
import at.sti2.wsmf.api.ws.channel.IInvocationStateChannelListener;
import at.sti2.wsmf.api.ws.channel.IQoSChannelListener;
import at.sti2.wsmf.api.ws.channel.data.AvailabilityChannelResponse;
import at.sti2.wsmf.api.ws.channel.data.InvocationStateChannelResponse;
import at.sti2.wsmf.api.ws.channel.data.QoSChannelResponse;

/**
 * @author Alex Oberhauser
 *
 */
@WebService
public class EventStreamListenerWebService implements IInvocationStateChannelListener, IAvailabilityChannelListener, IQoSChannelListener {

	/**
	 * @see at.sti2.wsmf.api.ws.channel.IInvocationStateChannelListener#getStateChangedEvent()
	 */
	@WebMethod
	@Override
	public void getStateChangedEvent(@WebParam(name="event")InvocationStateChannelResponse _event) throws Exception {
		System.out.println("[WSInvocationState] " + _event.getInstanceURI() + " has changed state to " + _event.getState());
	}

	/**
	 * @see at.sti2.wsmf.api.ws.channel.IAvailabilityChannelListener#getAvailabilityChangedEvent(at.sti2.wsmf.api.ws.channel.IAvailabilityChannelResponse)
	 */
	@WebMethod
	@Override
	public void getAvailabilityChangedEvent(@WebParam(name="event")AvailabilityChannelResponse _event) throws Exception {
		System.out.println("[WSAvailabilityState] " + _event.getEndpointURI() + " has changed availability to " + _event.getAvailability());
	}

	/**
	 * @see at.sti2.wsmf.api.ws.channel.IQoSChannelListener#getQoSChangedEvent(at.sti2.wsmf.api.ws.channel.data.QoSChannelResponse)
	 */
	@WebMethod
	@Override
	public void getQoSChangedEvent(@WebParam(name="event")QoSChannelResponse _event) throws Exception {
		System.out.println("[WSQoSValues] " + _event.getEndpointURL() + " has changed value '" 
				+ _event.getType() + "' to '" + _event.getValue() + " " + _event.getUnit() + "'"); 
	}

}
