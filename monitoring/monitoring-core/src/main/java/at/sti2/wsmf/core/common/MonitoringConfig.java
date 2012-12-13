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
import java.net.URL;
import java.util.Properties;

/**
 * @author Benjamin Hiltpolt
 * 
 *         Manages the config file
 */
public class MonitoringConfig {

	private final Properties prop;

	private static MonitoringConfig instance = null;

	private String triplestoreEndpoint;

	private String triplestoreReposid;

	private String instanceprefix;

	public static MonitoringConfig getConfig() throws IOException {
		if (instance == null) {
			return new MonitoringConfig();
		} else {
			return instance;
		}
	}

	private MonitoringConfig() throws IOException {
		this.prop = new Properties();
		URL config = this.getClass().getResource("default.properties");
		// URL config = new
		// URL("file://C:/Users/benhil.STI/workspace/sesa-core/monitoring/monitoring-core/src/main/resources/default.properties");
		URL l = ClassLoader.getSystemResource("default.properties");
		 System.out.println("MonitoringConfig: Loading "+ l);
		
		this.prop.load(new FileInputStream(l.getFile()));

		

		this.triplestoreEndpoint = this.prop
				.getProperty("triplestore.endpoint");
		this.triplestoreReposid = this.prop.getProperty("triplestore.reposid");

		this.instanceprefix = this.prop.getProperty("instance.prefixuri");

		System.out.println(this);
		if (this.triplestoreReposid == null) {
			throw new IOException("repository not available in config file");
		}

		if (this.triplestoreEndpoint == null) {
			throw new IOException(
					"triplestoreEndpoint not available in config file");
		}

	}

	public String getInstancePrefix() {
		return this.instanceprefix;
	}

	public String getTriplestoreEndpoint() {
		return this.triplestoreEndpoint;
	}

	public String getTriplestoreReposID() {
		return this.triplestoreReposid;
	}

	@Override
	public String toString() {
		return "Config [prop=" + prop + ", triplestoreEndpoint="
				+ triplestoreEndpoint + ", triplestoreReposid="
				+ triplestoreReposid + ", instanceprefix=" + instanceprefix
				+ "]";
	}

	/**
	 * @throws IOException 
	 * @deprecated
	 */
	public static void main(String args[]) throws IOException {
		MonitoringConfig.getConfig();
	}

}
