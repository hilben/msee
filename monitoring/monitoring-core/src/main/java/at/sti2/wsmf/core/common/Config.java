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
import java.util.Properties;

/**
 * @author Alex Oberhauser
 * 
 */
public class Config {
	private static Config instance = null;
	private final Properties prop;

	private String masterEndpointURL;
	private String masterEndpointNamespace;
	private String webServiceName;
	
	private String webServiceNamespace;

	private String triplestoreEndpoint;
	private String triplestoreReposid;
	
	private String instancePrefix;

	public static Config getDefaultConfig() throws IOException {
		if (null == instance)
			instance = new Config();
		return instance;
	}
	
	private Config() throws IOException {
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
			
			this.triplestoreEndpoint =this.prop.getProperty("triplestore.endpoint");
			this.triplestoreReposid = this.prop.getProperty("triplestore.reposid");
			this.instancePrefix = this.prop.getProperty("instance.prefixuri");
			this.webServiceNamespace = this.prop.getProperty("endpoint.namespace");
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

	public void setEndpointMasterURL(String masterEndpointURL) {
		this.masterEndpointURL = masterEndpointURL;
	}

	public String getEndpointMasterNamespace() {
		return this.masterEndpointNamespace;
	}

	@Deprecated
	public static void main(String argss[]) {
		try {
			System.out.println(Config.getDefaultConfig().toString());
			System.out.println(Config.getDefaultConfig().getTripleStoreEndpoint());

			System.out.println(Config.getDefaultConfig().getEndpointMaster());
			System.out.println(Config.getDefaultConfig().getWebServiceName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
