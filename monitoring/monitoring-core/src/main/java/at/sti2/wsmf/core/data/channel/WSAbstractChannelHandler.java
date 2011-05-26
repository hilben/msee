/**
 * WSAbstractChannelHandler.java - at.sti2.wsmf.core.data.channel
 */
package at.sti2.wsmf.core.data.channel;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.util.triplestore.QueryHelper;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.common.Config;

/**
 * @author Alex Oberhauser
 *
 */
public abstract class WSAbstractChannelHandler {
	private PersistentHandler persHandler;
	private Config cfg;
	private String channelURL;
	private final String channelPostfix;
	
	protected WSAbstractChannelHandler(String _channelPostfix) throws IOException {
		this.channelPostfix = _channelPostfix;
		this.persHandler = PersistentHandler.getInstance();
		this.cfg = Config.getInstance();
		this.channelURL = this.cfg.getInstancePrefix() + "_" + cfg.getWebServiceName() + "_" + _channelPostfix;
	}
	
	protected String getContextURL(URL _endpoint) {
		return _endpoint + "_" + this.cfg.getWebServiceName() + "_" + this.channelPostfix;
	}
	
	public Vector<ChannelSubscriber> getSubscriber() throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		return this.persHandler.getSubscriber(this.channelURL);
	}
	
	public void subscribe(URL _endpoint, String _namespace, String _operationName, String _soapAction) throws RepositoryException {
		String subscribeContext = this.getContextURL(_endpoint);
		String endpointURL = subscribeContext;
		
		this.persHandler.addResourceTriple(this.channelURL, QueryHelper.getRDFURI("type"),
				QueryHelper.getWSMFURI("EventChannel"), subscribeContext);
		
		this.persHandler.addResourceTriple(this.channelURL, QueryHelper.getWSMFURI("hasSubscriber"), 
				endpointURL, subscribeContext);
		
		this.persHandler.addResourceTriple(endpointURL, QueryHelper.getRDFURI("type"),
				QueryHelper.getWSMFURI("Subscriber"), subscribeContext);
		
		this.persHandler.updateLiteralTriple(endpointURL, QueryHelper.getWSMFURI("namespace"),
				_namespace, subscribeContext);
		
		this.persHandler.updateLiteralTriple(endpointURL, QueryHelper.getWSMFURI("operation"),
				_operationName, subscribeContext);

		if ( _soapAction != null )
			this.persHandler.updateLiteralTriple(endpointURL, QueryHelper.getWSMFURI("soapAction"),
					_soapAction, subscribeContext);
	}
	
	public void unsubsribe(URL _endpoint) throws RepositoryException {
		this.persHandler.deleteContext(this.getContextURL(_endpoint));
	}
	
}
