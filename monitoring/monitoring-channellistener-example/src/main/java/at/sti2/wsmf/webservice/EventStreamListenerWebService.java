/**
 * EventStreamListenerWebService.java - at.sti2.wsmf.webservice
 */
package at.sti2.wsmf.webservice;

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
