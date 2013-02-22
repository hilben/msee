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
package at.sti2.msee.registration.core.common;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Alex Oberhauser, Benjamin Hiltpolt
 *
 */
public class RegistrationConfig {
	private final Properties properties;
	
	private String propertyLocation = "/default.properties";
	
	/**
	 * Use other property file as in default (mainly testing purposes)
	 * 
	 * @param location
	 * @throws IOException
	 */
	public RegistrationConfig(String location) throws IOException {
		this.propertyLocation = location;
		this.properties = new Properties();
		this.properties.load(RegistrationConfig.class.getResourceAsStream(propertyLocation));
	}
	
	public RegistrationConfig() throws IOException {
		this.properties = new Properties();
		this.properties.load(RegistrationConfig.class.getResourceAsStream(propertyLocation));
	}
	
	/**
	 * Returns the Sesame Endpoint defined in the configuration file
	 * @return
	 */
	public String getSesameEndpoint() {
		return this.properties.getProperty("registration.sesame.endpoint");
	}

	/**
	 * returns the Sesame Repository defined in the configuration file
	 * @return
	 */
	public String getSesameReposID() {
		return this.properties.getProperty("registration.sesame.reposid");
	}
	
}
