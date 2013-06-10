package at.sti2.msee.invocation.webservice;

import java.io.IOException;
import java.util.Properties;

public class RepositoryConfig {
	private Properties properties;
	private String resourceLocation = "/default.properties";
	private String sesameEndpointName = "invocation.sesame.endpoint";
	private String sesameRepositoryIDName = "invocation.sesame.reposid";

	public RepositoryConfig() throws IOException {
		this.properties = new Properties();
		this.properties.load(RepositoryConfig.class.getResourceAsStream(resourceLocation));
	}

	public void setResourceLocation(String resourceLocation) {
		this.resourceLocation = resourceLocation;
		this.properties = new Properties();
		try {
			this.properties.load(RepositoryConfig.class.getResourceAsStream(resourceLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSesameEndpointName(String sesameEndpointName) {
		this.sesameEndpointName = sesameEndpointName;
	}

	public void setSesameRepositoryIDName(String sesameRepositoryIDName) {
		this.sesameRepositoryIDName = sesameRepositoryIDName;
	}

	public String getSesameEndpoint() {
		return this.properties.getProperty(sesameEndpointName);
	}

	public String getSesameRepositoryID() {
		return this.properties.getProperty(sesameRepositoryIDName);
	}

}
