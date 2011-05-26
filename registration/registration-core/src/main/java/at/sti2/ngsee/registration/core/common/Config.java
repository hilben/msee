/**
 * Config.java - at.sti2.ngsee.registration.common
 */
package at.sti2.ngsee.registration.core.common;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Alex Oberhauser
 *
 */
public class Config {
	private final Properties properties;
	
	public Config() throws IOException {
		this.properties = new Properties();
		this.properties.load(Config.class.getResourceAsStream("/default.properties")); 
	}
	
	public String getSesameEndpoint() {
		return this.properties.getProperty("sesame.endpoint");
	}
	
	public String getSesameReposID() {
		return this.properties.getProperty("sesame.reposid");
	}
	
}
