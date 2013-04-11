package at.sti2.msee.registration.webservice.configuration;

import java.io.IOException;
import java.util.Properties;

public class Configuration
{	
	private final Properties properties;	
	private String propertyLocation = "/default.properties";
	
	public Configuration() throws IOException {
		this.properties = new Properties();
	}
	
	public void load() throws IOException
	{
		this.properties.load(Configuration.class.getResourceAsStream(propertyLocation));
	}

	public void load(String propertyLocation) throws IOException
	{
		this.propertyLocation = propertyLocation;
		this.properties.load(Configuration.class.getResourceAsStream(propertyLocation));
	}
	
	public String getSesameEndpoint() {
		return this.properties.getProperty("registration.sesame.endpoint");
	}

	public String getSesameReposID() {
		return this.properties.getProperty("registration.sesame.reposid");
	}	
}
