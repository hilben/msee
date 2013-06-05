package at.sti2.msee.discovery.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	private final Properties properties;
	private String propertyLocation = "/default.properties";

	public Configuration() throws IOException {
		this.properties = new Properties();
		load();
	}

	public void load() throws IOException {
		this.properties.load(this.getClass().getResourceAsStream(propertyLocation));
	}

	public void load(String propertyLocation) throws IOException {
		this.propertyLocation = propertyLocation;
		load();
	}

	public void load(InputStream resourceAsStream) {
		try {
			properties.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getSesameEndpoint() {
		String endpoint = this.properties.getProperty("discovery.sesame.endpoint");
		if (endpoint == null) {
			throw new IllegalArgumentException(endpoint);
		}
		return endpoint;
	}

	public String getSesameReposID() {
		String repository = this.properties.getProperty("default.sesame.repository.name");
		if (repository == null) {
			repository = this.properties.getProperty("discovery.sesame.reposid");
		}
		if (repository == null) {
			throw new IllegalArgumentException(
					"\"default.sesame.repository.name\" or \"discovery.sesame.reposid\" not set");
		}
		return repository;
	}

	@Override
	public String toString() {
		return "Configuration [propertyLocation=" + propertyLocation + ", endp="
				+ getSesameEndpoint() + ", repo=" + getSesameReposID() + "]";
	}

}
