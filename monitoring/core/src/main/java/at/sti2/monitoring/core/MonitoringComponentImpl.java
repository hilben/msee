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
package at.sti2.monitoring.core;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.monitoring.core.common.MonitoringConfig;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.MonitoringWSAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.qos.QoSParamKey;

/**
 * 
 * 
 * @author Benjamin Hiltpolt
 * 
 */
public class MonitoringComponentImpl implements MonitoringComponent {
	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	private MonitoringConfig configuration;

	private MonitoringRepositoryHandler repositoryHandler;

	public MonitoringComponentImpl() throws IOException, RepositoryException {
		this.configuration = MonitoringConfig.getConfig();
		this.repositoryHandler = MonitoringRepositoryHandler
				.getMonitoringRepositoryHandler();
	}

	@Override
	public boolean isMonitoredWebService(URL webService)
			throws MonitoringException {
		try {
			return this.repositoryHandler.isMonitored(webService);
		} catch (IOException e) {
			LOGGER.error("Error checking if Monitored "
					+ e.getLocalizedMessage());
			throw new MonitoringException("Error disabling monitoring ", e);
		}

	}

	@Override
	public MonitoringInvocationInstance getInvocationInstance(URL WebService)
			throws MonitoringException {
		throw new MonitoringException("not implemented");
	}

	@Override
	public void enableMonitoring(URL webService) throws MonitoringException {
		try {
			this.repositoryHandler.enableMonitoringForWebservice(webService,
					true);
		} catch (IOException | RepositoryException | MalformedQueryException
				| UpdateExecutionException e) {
			LOGGER.error("Error enabling Monitoring " + e.getLocalizedMessage());
			throw new MonitoringException("Error enabling monitoring ", e);
		}
	}

	@Override
	public void disableMonitoring(URL webService) throws MonitoringException {
		try {
			this.repositoryHandler.enableMonitoringForWebservice(webService,
					false);
		} catch (IOException | RepositoryException | MalformedQueryException
				| UpdateExecutionException e) {
			LOGGER.error("Error enabling Monitoring " + e.getLocalizedMessage());
			throw new MonitoringException("Error disabling monitoring ", e);
		}
	}

	@Override
	public void updateAvailabilityState(URL WebService)
			throws MonitoringException {
		throw new MonitoringException("not implemented");
	}

	@Override
	public MonitoringInvocationState getInvocationInstanceInvocationState(
			MonitoringInvocationInstance instance) throws MonitoringException {
		throw new MonitoringException("not implemented");
	}

	@Override
	public void getMonitoringData(URL WebService) throws MonitoringException {
		throw new MonitoringException("not implemented");
	}

	@Override
	public MonitoringWSAvailabilityState getAvailabilityState(URL WebService)
			throws MonitoringException {
		throw new MonitoringException("not implemented");
	}

	@Override
	public void clearAllContentOfWebservice(URL webService) throws MonitoringException {
		try {
			this.repositoryHandler.clearAllContentForWebservice(webService);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e) {
			LOGGER.error("Error clearing context: " + e.getLocalizedMessage());
			throw new MonitoringException("Error disabling monitoring ", e);
		}
	}

}
