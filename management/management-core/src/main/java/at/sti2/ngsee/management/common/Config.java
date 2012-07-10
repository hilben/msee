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
package at.sti2.ngsee.management.common;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Alex Oberhauser
 *
 */
public class Config {
	private final Properties properties;
	
	public Config() throws IOException {
		this.properties = new Properties();
		this.properties.load(Config.class.getResourceAsStream("/default.properties")); 
	}
	
	public String getSesameEndpoint() {
		return this.properties.getProperty("sesame.endpoint");
	}
	
	public String getSesameReposID() {
		return this.properties.getProperty("sesame.reposid");
	}
	
}
