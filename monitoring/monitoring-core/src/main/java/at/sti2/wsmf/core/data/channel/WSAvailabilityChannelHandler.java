/**
 * WSInvocationStateChannelHandler.java - at.sti2.wsmf.core.data.channel
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
import at.sti2.wsmf.core.InvocationHandler;

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
		super("wsavailabilitychannel"); 
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
				InvocationHandler.sendChannelMessage(entry.getEndpoint(), entry.getSoapAction(), messageToSend);
			} catch (AxisFault e) {
				log.error("Failed to send channel message to '" + entry.getEndpoint() + "', through exception: " + e.getLocalizedMessage());
			} catch (XMLStreamException e) {
				log.error("Failed to send channel message to '" + entry.getEndpoint() + "', through exception: " + e.getLocalizedMessage());
			}
		}
	}

}
