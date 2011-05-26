/**
 * Config.java - at.sti2.wsmf.core.data.common
 */
package at.sti2.wsmf.core.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import at.sti2.wsmf.core.PersistentHandler;

/**
 * @author Alex Oberhauser
 *
 */
public class Config {
	private static Config instance = null;
	private final Properties prop;
	
	public static Config getInstance() throws IOException {
		if ( null == instance )
			instance = new Config();
		return instance;
	}
	
	private Config() throws IOException {
		this.prop = new Properties();
		InputStream configIS = PersistentHandler.class.getResourceAsStream("/default.properties");
		if ( configIS != null ) 
			this.prop.load(configIS);
	}
	
	public String getTripleStoreEndpoint() {
		return this.prop.getProperty("triplestore.endpoint");
	}
	
	public String getTripleStoreReposID() {
		return this.prop.getProperty("triplestore.reposid");
	}
	
	public String getInstancePrefix() {
		return this.prop.getProperty("instance.prefixuri");
	}
	
	public String getWebServiceName() {
		return this.prop.getProperty("endpoint.servicename");
	}
	
	public String getWebServiceNamespace() {
		return this.prop.getProperty("endpoint.namespace");
	}
	
	public String getEndpointMaster() {
		return this.prop.getProperty("endpoint.master.url");
	}
	
	public String getEndpointMasterNamespace() {
		return this.prop.getProperty("endpoint.master.url");
	}
}
