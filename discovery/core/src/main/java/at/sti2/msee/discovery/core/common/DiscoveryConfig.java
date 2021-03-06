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
package at.sti2.msee.discovery.core.common;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class DiscoveryConfig {
	private final static Logger LOGGER = LogManager.getLogger(DiscoveryConfig.class.getName());
	
	private Properties properties;
	private String resourceLocation = "/default.properties";
	private String sesameEndpointName = "discovery.sesame.endpoint";
	private String sesameRepositoryIDName = "discovery.sesame.reposid";
	
	public DiscoveryConfig() throws IOException {
		LOGGER.debug("Discovery Config created");
		this.properties = new Properties();
		this.properties.load(DiscoveryConfig.class.getResourceAsStream(resourceLocation)); 
	}
	
	public void setResourceLocation (String resourceLocation){
		LOGGER.debug("Updating resource location");
		this.resourceLocation = resourceLocation;
		this.properties = new Properties();
		try {
			this.properties.load(DiscoveryConfig.class.getResourceAsStream(resourceLocation));
		} catch (IOException e) {
			LOGGER.catching(e);
		} 
	}
	
	public void setSesameEndpointName(String sesameEndpointName) {
		this.sesameEndpointName = sesameEndpointName;
	}

	public void setSesameRepositoryIDName(String sesameRepositoryIDName) {
		this.sesameRepositoryIDName = sesameRepositoryIDName;
	}
	
	public String getSesameEndpoint() {
		//if(this.properties.getProperty(sesameEndpointName)==null){
			//return "http://sesa.sti2.at:8080/openrdf-sesame";
		//}
		return this.properties.getProperty(sesameEndpointName);
	}
	
	public String getSesameRepositoryID() {
		//if(this.properties.getProperty(sesameRepositoryIDName)==null){
			//return "msee";
		//}
		return this.properties.getProperty(sesameRepositoryIDName);
	}
	
}
