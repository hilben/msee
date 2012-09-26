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
 * @author Benjamin Hiltpolt
 * 
 *         Stores and manages a Config file for every Endpoint
 */
public class Config {

	private final Properties prop;

	private static Config instance = null;

	private String triplestoreEndpoint;

	private String triplestoreReposid;

	private String instanceprefix;

	public static Config getConfig() throws IOException {
		if (instance == null) {
			return new Config();
		} else {
			return instance;
		}
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

			this.triplestoreEndpoint = this.prop
					.getProperty("triplestore.endpoint");
			this.triplestoreReposid = this.prop
					.getProperty("triplestore.reposid");

			this.instanceprefix = this.prop.getProperty("instance.prefixuri");

			configIS.close();
		}
	}

	public String getInstancePrefix() {
		return this.instanceprefix;
	}

	public String getTripleStoreEndpoint() {
		return this.triplestoreEndpoint;
	}

	public String getTripleStoreReposID() {
		return this.triplestoreReposid;
	}

}
