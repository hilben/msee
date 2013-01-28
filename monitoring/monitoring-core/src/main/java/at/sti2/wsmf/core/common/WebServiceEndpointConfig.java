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
package at.sti2.wsmf.core.common;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.core.data.WebServiceEndpoint;

/**
 * @author Alex Oberhauser
 * @author Benjamin Hiltpolt
 * 
 *         Stores and manages a Config file for every Endpoint TODO: absoulute
 *         file path of config is BAD! TODO: obsolete? find a way to remove...
 */
public class WebServiceEndpointConfig {

	private static HashMap<String, WebServiceEndpointConfig> instances = new HashMap<String, WebServiceEndpointConfig>();

	private final Properties prop;
	private String endpointURL;
	private String masterEndpointNamespace;
	private String webServiceName;
	private String webServiceNamespace;
	private String triplestoreEndpoint;
	private String triplestoreReposid;
	private String instancePrefix;
	private WebServiceEndpoint webserviceendpoint;

	/**
	 * 
	 * Returns or creates a config file for an endpoint
	 * 
	 * @param endpointURL
	 * @return
	 * @throws IOException
	 * @throws RepositoryException
	 */
	public static WebServiceEndpointConfig getConfig(String endpointURL)
			throws IOException, RepositoryException {

		if (!instances.containsKey((endpointURL))) {

			WebServiceEndpointConfig newCfg = new WebServiceEndpointConfig(
					endpointURL);

			return newCfg;

		} else {
			return instances.get(endpointURL);
		}
	}

	public static WebServiceEndpointConfig getConfig(URL endpointURL)
			throws IOException, RepositoryException {
		return WebServiceEndpointConfig.getConfig(endpointURL.toExternalForm());
	}

	private WebServiceEndpointConfig(String endpointURL) throws IOException,
			RepositoryException {
		this.prop = new Properties();

		this.prop.load(WebServiceEndpointConfig.class
				.getResourceAsStream("/default.properties"));

		System.out.println("this.prop.size(): " + this.prop.size());

		this.masterEndpointNamespace = this.prop
				.getProperty("endpoint.master.url");
		this.endpointURL = endpointURL;
		this.webServiceName = this.prop
				.getProperty("monitoring.endpoint.servicename");

		this.triplestoreReposid = this.prop
				.getProperty("monitoring.triplestore.reposid");
		this.triplestoreEndpoint = this.prop
				.getProperty("monitoring.triplestore.endpoint");
		this.instancePrefix = this.prop
				.getProperty("monitoring.instance.prefixuri");
		this.webServiceNamespace = this.prop
				.getProperty("monitoring.endpoint.namespace");

		instances.put(endpointURL, this);

		this.webserviceendpoint = new WebServiceEndpoint(new URL(
				this.endpointURL));

	}

	public String getTripleStorendpoint() {
		return this.triplestoreEndpoint;
	}

	public String getTripleStorereposID() {
		return this.triplestoreReposid;
	}

	public String getInstancePrefix() {
		return this.instancePrefix;
	}

	public String getWebServiceName() {
		return this.webServiceName;
	}

	public void setWebServiceName(String webServiceName) {
		this.webServiceName = webServiceName;
	}

	public String getWebServiceNamespace() {
		return webServiceNamespace;
	}

	public String getEndpointURL() {
		return this.endpointURL;
	}

	public void setTriplestoreReposid(String triplestoreReposid) {
		this.triplestoreReposid = triplestoreReposid;
	}

	public WebServiceEndpoint getWebServiceEndpoint() {
		return this.webserviceendpoint;
	}

	@Override
	public String toString() {
		return "WebServiceEndpointConfig [prop=" + prop
				+ ", masterEndpointURL=" + endpointURL
				+ ", masterEndpointNamespace=" + masterEndpointNamespace
				+ ", webServiceName=" + webServiceName
				+ ", webServiceNamespace=" + webServiceNamespace
				+ ", triplestoreEndpoint=" + triplestoreEndpoint
				+ ", triplestoreReposid=" + triplestoreReposid
				+ ", instancePrefix=" + instancePrefix
				+ ", webserviceendpoint=" + this.webserviceendpoint + "]";
	}
}
