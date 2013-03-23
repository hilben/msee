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
package at.sti2.util.triplestore;

import java.io.IOException;
import java.util.Properties;



public class RepositoryConfig {
	private Properties properties;
	private String resourceLocation = "/default.properties";
	private String sesameEndpointName = "repository.sesame.endpoint";
	private String sesameRepositoryIDName = "repository.sesame.reposid";
	
	public RepositoryConfig() throws IOException {
		this.properties = new Properties();
		this.properties.load(RepositoryConfig.class.getResourceAsStream(resourceLocation)); 
	}
	
	public void setResourceLocation (String resourceLocation){
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
