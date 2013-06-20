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
package at.sti2.msee.ranking.common;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sti2.msee.config.Config;

/**
 * @author Benjamin Hiltpolt
 * 
 *         Manages the configuration file
 */
public class RankingConfig {

	private final Logger LOGGER = LogManager.getLogger(this.getClass().getName());

	private static RankingConfig instance = null;

	private String triplestoreEndpoint;
	private String triplestoreReposid;
	private String instancePrefix;

	public static RankingConfig getConfig() throws IOException {
		if (instance == null) {
			return new RankingConfig();
		} else {
			return instance;
		}
	}

	private RankingConfig() throws IOException {
		Config config = Config.INSTANCE;
		this.triplestoreEndpoint = config.getRankingRepositoryEndpoint();
		this.triplestoreReposid = config.getRankingRepositoryID();
		this.instancePrefix = config.getRankingInstancePrefix();

		LOGGER.debug("Loaded ranking configuration: " + this);

		if (this.triplestoreReposid == null) {
			throw new IOException("repository not available in ranking config file ");
		}

		if (this.triplestoreEndpoint == null) {
			throw new IOException("triplestoreEndpoint not available in ranking config file ");
		}
		if (this.instancePrefix == null) {
			throw new IOException("triplestoreEndpoint not available in ranking config file ");
		}
	}

	public String getTriplestoreEndpoint() {
		return this.triplestoreEndpoint;
	}

	public String getTriplestoreReposID() {
		return this.triplestoreReposid;
	}

	@Override
	public String toString() {
		return "Config [triplestoreEndpoint=" + triplestoreEndpoint + ", triplestoreReposid="
				+ triplestoreReposid + ", ranking.instance.prefixuri= " + instancePrefix + " ]";
	}

	public String getInstancePrefix() {
		return this.instancePrefix;
	}
}
