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
package at.sti2.wsmf.core.data.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

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
import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;
import at.sti2.wsmf.api.data.state.WSAvailabilityState;
import at.sti2.wsmf.api.data.state.WSInvocationState;
import at.sti2.wsmf.core.MonitoringInvocationHandler;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Alex Oberhauser
 */
public class WSQoSChannelHandler extends WSAbstractChannelHandler {
	private static Logger log = Logger.getLogger(WSQoSChannelHandler.class);
	private static WSQoSChannelHandler instance = null;
	
	public static WSQoSChannelHandler getInstance() throws IOException, RepositoryException {
		if ( null == instance )
			instance = new WSQoSChannelHandler();
		return instance;
	}
	
	private WSQoSChannelHandler() throws IOException, RepositoryException {
		super("wsqoschannel","WSQoSChannelHandler");
	}
	
	private String buildMessage(String _invocationInstance, String _namespace, String _operationName, QoSParamValue _value) {
		StringBuffer msg = new StringBuffer();
		QName operation = new QName(_namespace, _operationName);
		msg.append("<ns1:" + operation.getLocalPart() + " xmlns:ns1='" + operation.getNamespaceURI() + "'>");
		msg.append("<event>");
		msg.append("<endpointURL>" + _invocationInstance + "</endpointURL>");
		msg.append("<type>" + _value.getType() + "</type>");
		msg.append("<unit>" + _value.getUnit() + "</unit>");
		msg.append("<value>" + _value.getValue() + "</value>");
		msg.append("</event>");
		msg.append("</ns1:" + operation.getLocalPart() + ">");
		return msg.toString();
	}
	
	protected boolean shouldValueBeSend(URL _endpoint, QoSParamValue _value) throws FileNotFoundException, IOException, NumberFormatException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		QoSParamKey paramType = _value.getType();
		switch ( paramType ) {
			case PayloadSizeRequestAverage:
			case PayloadSizeRequestMaximum:
			case PayloadSizeRequestMinimum:
				long maxPayload = new Long(persHandler.getThresholdValue(_endpoint, QoSThresholdKey.PayloadSizeRequestMaximum).getValue());
				long minPayload = new Long(persHandler.getThresholdValue(_endpoint, QoSThresholdKey.PayloadSizeRequestMinimum).getValue());
				long payloadValue = new Long(_value.getValue());
				log.info("[QoSThreshold] Payload Size " + minPayload + " <= " + payloadValue + " <= " + maxPayload);
				if ( minPayload == -1 || maxPayload == -1 )
					return true;
				else if ( minPayload <= payloadValue && maxPayload >= payloadValue )
					return true;
				break;
			case RequestTotal:
			case RequestFailed:
			case RequestSuccessful:
				long maxRequests = new Long(persHandler.getThresholdValue(_endpoint, QoSThresholdKey.RequestsMaximum).getValue());
				long minRequests = new Long(persHandler.getThresholdValue(_endpoint, QoSThresholdKey.RequestsMinimum).getValue());
				long requestsValue = new Long(_value.getValue());
				log.info("[QoSThreshold] Requests " + minRequests + " <= " + requestsValue + " <= " + maxRequests);
				if ( minRequests == -1 || maxRequests == -1 )
					return true;
				else if ( minRequests <= requestsValue && maxRequests >= requestsValue )
					return true;
				break;
			case ResponseTimeAverage:
			case ResponseTimeMaximum:
			case ResponseTimeMinimum:
				long maxResponseTime = new Long(persHandler.getThresholdValue(_endpoint, QoSThresholdKey.ResponseTimeMaximum).getValue());
				long minResponseTime = new Long(persHandler.getThresholdValue(_endpoint, QoSThresholdKey.ResponseTimeMinimum).getValue());
				long responseTimeValue = new Long(_value.getValue());
				log.info("[QoSThreshold] Response time " + minResponseTime + " <= " + responseTimeValue + " <= " + maxResponseTime);
				if ( minResponseTime == -1 || maxResponseTime == -1 )
					return true;
				else if ( minResponseTime <= responseTimeValue && maxResponseTime >= responseTimeValue )
					return true;
				break;
		}
		return false;
	}
	
	public void sendState(String _invocationInstance, QoSParamValue _value) throws QueryEvaluationException, RepositoryException, MalformedQueryException, FileNotFoundException, IOException {
		if ( !this.shouldValueBeSend(new URL(_invocationInstance), _value) ) return;
		Vector<ChannelSubscriber> subscriber = this.getSubscriber();
		for ( ChannelSubscriber entry : subscriber ) {
			try {
				String messageToSend = this.buildMessage(_invocationInstance, entry.getNamespace(), entry.getOperationName(), _value);
				sendChannelMessage(entry.getEndpoint(), entry.getSoapAction(), messageToSend);
			} catch (AxisFault e) {
				log.error("Failed to send channel message to '" + entry.getEndpoint() + "', through exception: " + e.getLocalizedMessage());
			} catch (XMLStreamException e) {
				log.error("Failed to send channel message to '" + entry.getEndpoint() + "', through exception: " + e.getLocalizedMessage());
			}
		}
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
	public static void sendChannelMessage(String _endpoint, String _soapAction,
			String _message) throws XMLStreamException, AxisFault {
		ServiceClient serviceClient = new ServiceClient();
		Options opts = new Options();
		opts.setTo(new EndpointReference(_endpoint));
		opts.setAction(_soapAction);
		serviceClient.setOptions(opts);

		log.info("[ChannelMessage] Sending the following message to '"
				+ _endpoint + "': " + _message);
		OMElement omInput = AXIOMUtil.stringToOM(_message);
		serviceClient.fireAndForget(omInput);

		serviceClient.cleanup();
		serviceClient.cleanupTransport();
		serviceClient.removeHeaders();
	}

	
	
	public static void main(String[] args) throws Exception {
		WSAvailabilityChannelHandler availabilityHandler = WSAvailabilityChannelHandler.getInstance();
		availabilityHandler.subscribe(new URL("http://localhost:9999/wsmf-channellistener-example/services/events"),
				"http://webservice.wsmf.sti2.at/", "getAvailabilityChangedEvent", null);
		
		WSQoSChannelHandler qosHandler = WSQoSChannelHandler.getInstance();
		qosHandler.subscribe(new URL("http://localhost:9999/wsmf-channellistener-example/services/events"),
				"http://webservice.wsmf.sti2.at/", "getQoSChangedEvent", null);
		
		WSInvocationStateChannelHandler stateHandler = WSInvocationStateChannelHandler.getInstance();
		stateHandler.subscribe(new URL("http://localhost:9999/wsmf-channellistener-example/services/events"),
				"http://webservice.wsmf.sti2.at/", "getStateChangedEvent", null);
		
		availabilityHandler.sendState("http://example.org/endpointInstanceExample", WSAvailabilityState.WSAvailable);
		stateHandler.sendState("http://example.org/invocationInstanceExample", WSInvocationState.Completed);
		qosHandler.sendState("http://example.org/endpointInstanceExample", new QoSParamValue(QoSParamKey.PayloadSizeRequestAverage, "28", QoSUnit.Bytes));
	}
	
}
