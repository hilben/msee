/**
 * EndpointHandler.java - at.sti2.wsmf.core
 */
package at.sti2.wsmf.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.core.common.Config;
import at.sti2.wsmf.core.data.WebServiceEndpoint;

/**
 * @author Alex Oberhauser
 *
 */
public class EndpointHandler {
	private static Logger log = Logger.getLogger(EndpointHandler.class);
	
	private PersistentHandler persHandler;
	
	private static EndpointHandler instance = null;
	private String instancePrefix;
	private String webserviceID;
	private WebServiceEndpoint masterWS;
	/**
	 * The Web Service that is currently used for invocation.
	 */
	private WebServiceEndpoint currentActiveWS;
	
	public static EndpointHandler getInstance() throws IOException, RepositoryException {
		if ( null == instance )
			instance = new EndpointHandler();
		return instance;
	}
	
	private EndpointHandler() throws IOException, RepositoryException {
		Config cfg = Config.getInstance();
		this.persHandler = PersistentHandler.getInstance();
		this.masterWS = new WebServiceEndpoint(new URL(cfg.getEndpointMaster()));
		this.instancePrefix = cfg.getInstancePrefix();
		this.webserviceID = cfg.getWebServiceName();
		this.currentActiveWS = this.masterWS;
	}
	
	public void changeCurrentActiveWS(WebServiceEndpoint _instanceWS) {
		if ( !this.currentActiveWS.equals(_instanceWS) ) {
			log.info("Changing current active web service from '" + this.currentActiveWS.getEndpoint() + "' to '" + _instanceWS.getEndpoint() + "'");
			this.currentActiveWS = _instanceWS;
		}
	}
	
	public synchronized String getServiceID() {
		return this.webserviceID;
	}
	
	public synchronized WebServiceEndpoint getCurrentActiveWS() {
		return this.currentActiveWS;
	}
	
	public synchronized WebServiceEndpoint getMasterWS() {
		return this.masterWS;
	}
	
	public synchronized Vector<URL> getFallbackWS() throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		return this.persHandler.getEndpointWS(this.instancePrefix + this.webserviceID);
	}

	/**
	 * @param _endpoint
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws RepositoryException 
	 */
	public synchronized void removeFallbackWS(URL _endpoint) throws RepositoryException, FileNotFoundException, IOException {
		this.persHandler.deleteEndpoint(_endpoint);
		this.persHandler.commit();
	}
}