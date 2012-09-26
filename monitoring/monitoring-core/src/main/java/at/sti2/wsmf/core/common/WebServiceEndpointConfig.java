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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.core.EndpointHandler;

/**
 * @author Alex Oberhauser
 * @author Benjamin Hiltpolt
 * 
 *         Stores and manages a Config file for every Endpoint
 */
public class WebServiceEndpointConfig {

	private static HashMap<String, WebServiceEndpointConfig> instances = new HashMap<String, WebServiceEndpointConfig>();

	private final Properties prop;

	private String masterEndpointURL;

	private String masterEndpointNamespace;

	private String webServiceName;

	private String webServiceNamespace;

	private String triplestoreEndpoint;

	private String triplestoreReposid;

	private String instancePrefix;

	private EndpointHandler endpointhandler;

	/**
	 * 
	 * Returns or creates a config file for an endpoint
	 * 
	 * @param endpointURL
	 * @return
	 * @throws IOException
	 */
	public static WebServiceEndpointConfig getConfig(String endpointURL)
			throws IOException {
		System.out.println("Get config for " + endpointURL);
		System.out.println(instances);
		if (!instances.containsKey((endpointURL))) {
			System.out.println("not contained");
			WebServiceEndpointConfig newCfg = new WebServiceEndpointConfig(
					endpointURL);
			// instances.put(endpointURL, newCfg);
			return newCfg;

		} else {
			return instances.get(endpointURL);
		}
	}

	public static WebServiceEndpointConfig getConfig(URL endpointURL)
			throws IOException {
		return WebServiceEndpointConfig.getConfig(endpointURL.toExternalForm());
	}

	private WebServiceEndpointConfig(String masterEndpointURL)
			throws IOException {
		this.prop = new Properties();
		// InputStream configIS =
		// PersistentHandler.class.getResourceAsStream("/default.properties");
		// change back only for testing reasons TODO
		InputStream configIS = new FileInputStream(
				"C:/Users/benhil.STI/workspace/sesa-core/monitoring/monitoring-core/src/main/resources/default.properties");
		if (configIS != null) {
			this.prop.load(configIS);

			this.masterEndpointNamespace = this.prop
					.getProperty("endpoint.master.url");
			this.masterEndpointURL = this.prop
					.getProperty("endpoint.master.url");
			this.webServiceName = this.prop.getProperty("endpoint.servicename");

			this.triplestoreReposid = this.prop
					.getProperty("triplestore.reposid");
			this.instancePrefix = this.prop.getProperty("instance.prefixuri");
			this.webServiceNamespace = this.prop
					.getProperty("endpoint.namespace");
			
			configIS.close();
		}

		this.masterEndpointURL = masterEndpointURL;

		instances.put(masterEndpointURL, this);

		try {
			this.endpointhandler = new EndpointHandler(this);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	public String getTripleStoreEndpoint() {
		return this.triplestoreEndpoint;
	}

	public String getTripleStoreReposID() {
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

	public String getEndpointMaster() {
		return this.masterEndpointURL;
	}

	public String getEndpointMasterNamespace() {
		return this.masterEndpointNamespace;
	}

	public String getMasterEndpointNamespace() {
		return masterEndpointNamespace;
	}

	public void setMasterEndpointNamespace(String masterEndpointNamespace) {
		this.masterEndpointNamespace = masterEndpointNamespace;
	}

	public String getTriplestoreEndpoint() {
		return triplestoreEndpoint;
	}

	public String getTriplestoreReposid() {
		return triplestoreReposid;
	}

	public void setTriplestoreReposid(String triplestoreReposid) {
		this.triplestoreReposid = triplestoreReposid;
	}

	public String getMasterEndpointURL() {
		return masterEndpointURL;
	}

	public void setWebServiceNamespace(String webServiceNamespace) {
		this.webServiceNamespace = webServiceNamespace;
	}

	public void setInstancePrefix(String instancePrefix) {
		this.instancePrefix = instancePrefix;
	}

	public EndpointHandler getEndPointHandler() {
		return this.endpointhandler;
	}
	
	public static void main(String args[]) throws IOException {
		System.out.println(WebServiceEndpointConfig.getConfig("http://asd").getWebServiceNamespace());
	}
}
