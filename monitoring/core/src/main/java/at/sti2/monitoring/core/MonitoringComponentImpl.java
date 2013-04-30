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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.monitoring.core.common.MonitoringConfig;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.MonitoringWSAvailabilityState;
import at.sti2.msee.monitoring.api.MonitoringWebserviceAvailability;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStored;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.api.qos.QoSParameter;

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
	private MonitoringParameterStoreHandler parameterStorageHandler;
	private MonitoringRepositoryHandler repositoryHandler;

	public MonitoringComponentImpl() throws IOException, RepositoryException {
		this.configuration = MonitoringConfig.getConfig();
		this.repositoryHandler = MonitoringRepositoryHandler
				.getMonitoringRepositoryHandler();
		this.parameterStorageHandler = new MonitoringParameterStoreHandler(this);
	}

	@Override
	public boolean isMonitoredWebService(URL webService)
			throws MonitoringException {
		try {
			return this.repositoryHandler.isMonitoredWebservice(webService);
		} catch (IOException e) {
			LOGGER.error("Error checking if Monitored "
					+ e.getLocalizedMessage());
			throw new MonitoringException("Error disabling monitoring ", e);
		}

	}

	@Override
	public MonitoringInvocationInstance createInvocationInstance(URL WebService)
			throws MonitoringException {
		MonitoringInvocationInstance instance = new MonitoringInvocationInstanceImpl(
				WebService, this);

		UUID invocationStateID = UUID.randomUUID();
		String invocationStateIDstr = invocationStateID.toString();

		String invocationInstanceIDstr = instance.getUUID().toString();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");

		String time = simpleDateFormat.format(new Date());
		String state = instance.getState().toString();

		try {
			this.repositoryHandler.updateInvocationInstanceState(WebService,
					invocationInstanceIDstr, invocationStateIDstr, state, time);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e) {
			LOGGER.error("Error MonitoringNoDataForQoSParameterTypecreating Invocation Instance"
					+ e.getLocalizedMessage());
			throw new MonitoringException(
					"Error creating Invocation Instance ", e);
		}

		return instance;
	}

	@Override
	public void updateInvocationInstance(MonitoringInvocationInstance instance)
			throws MonitoringException {

		UUID invocationStateID = UUID.randomUUID();
		String invocationStateIDstr = invocationStateID.toString();

		String invocationInstanceIDstr = instance.getUUID().toString();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		String time = simpleDateFormat.format(new Date());
		String state = instance.getState().toString();

		try {
			this.repositoryHandler.updateInvocationInstanceState(
					instance.getWebService(), invocationInstanceIDstr,
					invocationStateIDstr, state, time);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e) {
			LOGGER.error("Error creating Invocation Instance"
					+ e.getLocalizedMessage());
			throw new MonitoringException(
					"Error creating Invocation Instance ", e);
		}
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
	public void updateAvailabilityState(URL webService,
			MonitoringWSAvailabilityState state) throws MonitoringException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		String time = simpleDateFormat.format(new Date());

		try {
			this.repositoryHandler.updateAvailabilityState(webService, state,
					time);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e) {
			LOGGER.error("Error updating AvailabilityState" + e.getLocalizedMessage());
			throw new MonitoringException("Error updating AvailabilityState", e);
		}
	}

	@Override
	public MonitoringInvocationInstance getInvocationInstance(String UUID)
			throws MonitoringException {
		try {
			return this.repositoryHandler.getInvocationInstance(UUID, this);
		} catch (IOException e) {
			LOGGER.error("Error getting invocation instance: "
					+ e.getLocalizedMessage());
			throw new MonitoringException(
					"Error getting invocation instance: ", e);
		}
	}

	@Override
	public MonitoringWebserviceAvailability getAvailability(URL webService)
			throws MonitoringException {
		try {
			return this.repositoryHandler.getAvailability(webService);
		} catch (IOException | MonitoringNoDataStored | ParseException e) {
			LOGGER.error("Error getting Availability of webService: " + e.getLocalizedMessage());
			throw new MonitoringException("Error getting Availability of webService: ", e);
		}
	}

	@Override
	public void clearAllContentOfWebservice(URL webService)
			throws MonitoringException {
		try {
			this.repositoryHandler.clearAllContentForWebservice(webService);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e) {
			LOGGER.error("Error clearing context: " + e.getLocalizedMessage());
			throw new MonitoringException("Error disabling monitoring ", e);
		}
	}

	@Override
	public void addSuccessfulInvocationData(URL webService,
			double payloadSizeResponse, double payloadSizeRequest,
			double responseTime) throws MonitoringException {

		try {
			this.parameterStorageHandler.addSuccessfulInvocation(webService,
					payloadSizeResponse, payloadSizeRequest, responseTime);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | ParseException e) {

			LOGGER.error("Error adding successful invocation into repository: "
					+ e.getLocalizedMessage());
			throw new MonitoringException(
					"Error adding successful invocation into repository: ", e);
		}
	}

	@Override
	public void addUnsuccessfullInvocationData(URL webService)
			throws MonitoringException {
		try {
			this.parameterStorageHandler.addUnsuccessfulInvocation(webService);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | ParseException e) {

			LOGGER.error("Error adding unsuccessful invocation into repository: "
					+ e.getLocalizedMessage());
			throw new MonitoringException(
					"Error adding unsuccessful invocation into repository: ", e);
		}
	}

	@Override
	public QoSParameter getQoSParameter(URL webService, QoSType qostype)
			throws MonitoringException, MonitoringNoDataStored {
		QoSParameter ret = null;
		try {
			ret = this.repositoryHandler.getCurrentQoSParameter(webService,
					qostype);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | ParseException e) {
			LOGGER.error("Error loading QoSParameter: "
					+ e.getLocalizedMessage());
			throw new MonitoringException("Error loading QoSParameter: ", e);
		}

		return ret;
	}


}
