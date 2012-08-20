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
//		InputStream configIS = PersistentHandler.class.getResourceAsStream("/default.properties");
		//change back only for testing reasons TODO
		InputStream configIS = new FileInputStream("C:/Users/benhil.STI/workspace/sesa-core/monitoring/monitoring-core/src/main/resources/default.properties");
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
	
	@Deprecated
	public static void main(String argss[]) {
		try {
			System.out.println(Config.getInstance().toString());
			System.out.println(Config.getInstance().getTripleStoreEndpoint());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
