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

import java.io.IOException;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.api.data.state.WSAvailabilityState;
import at.sti2.wsmf.core.MonitoringInvocationHandler;

/**
 * @author Alex Oberhauser
 */
public class WSAvailabilityChannelHandler extends WSAbstractChannelHandler {
	private static Logger log = Logger.getLogger(WSAvailabilityChannelHandler.class);
	private static WSAvailabilityChannelHandler instance = null;
	
	public static WSAvailabilityChannelHandler getInstance() throws IOException, RepositoryException {
		if ( null == instance )
			instance = new WSAvailabilityChannelHandler();
		return instance;
	}
	
	private WSAvailabilityChannelHandler() throws IOException, RepositoryException {
		super("wsavailabilitychannel","WSAvailabilityChannelHandler"); 
	}
	
	private String buildMessage(String _invocationInstance, String _namespace, String _operationName, WSAvailabilityState _state) {
		StringBuffer msg = new StringBuffer();
		QName operation = new QName(_namespace, _operationName);
		msg.append("<ns1:" + operation.getLocalPart() + " xmlns:ns1='" + operation.getNamespaceURI() + "'>");
		msg.append("<event>");
		msg.append("<endpointURI>" + _invocationInstance + "</endpointURI>");
		msg.append("<availability>" + _state + "</availability>");
		msg.append("</event>");
		msg.append("</ns1:" + operation.getLocalPart() + ">");
		return msg.toString();
	}
	
	public void sendState(String _invocationInstance, WSAvailabilityState _state) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		Vector<ChannelSubscriber> subscriber = this.getSubscriber();
		for ( ChannelSubscriber entry : subscriber ) {
			try {
				String messageToSend = this.buildMessage(_invocationInstance, entry.getNamespace(), entry.getOperationName(), _state);
				WSQoSChannelHandler.sendChannelMessage(entry.getEndpoint(), entry.getSoapAction(), messageToSend);
			} catch (AxisFault e) {
				log.error("Failed to send channel message to '" + entry.getEndpoint() + "', through exception: " + e.getLocalizedMessage());
			} catch (XMLStreamException e) {
				log.error("Failed to send channel message to '" + entry.getEndpoint() + "', through exception: " + e.getLocalizedMessage());
			}
		}
	}

}
