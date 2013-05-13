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
package at.sti2.msee.invocation.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;
import org.xml.sax.SAXException;

import at.sti2.msee.invocation.api.ServiceInvocation;
import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.monitoring.core.MonitoringInvocationInstanceImpl;

/**
 * @author Benjamin Hiltpolt
 * 
 *         TODO: Documentation
 */
public class ServiceInvocationImpl implements ServiceInvocation {
	protected static Logger logger = Logger
			.getLogger(ServiceInvocationImpl.class);

	private MonitoringComponent monitoring = null;

	public ServiceInvocationImpl() {
		try {
			this.monitoring = new MonitoringComponentImpl();
		} catch (RepositoryException | IOException e) {
			logger.error("Invocation could not initialize the MonitoringComponent");
			this.monitoring = null;
		}
	}

	/**
	 * 
	 * TODO: lowering, lifting TODO: monitoring, TODO: use service IDs not
	 * endpoints
	 * 
	 * @param webserviceURL
	 * @param operationName
	 * @param inputData
	 * @return
	 * @throws MonitoringException
	 * @throws ServiceException
	 * @throws MalformedURLException
	 * @throws SAXException
	 * @throws AxisFault
	 */
	public String invoke(URL webserviceURL, String operationName,
			String inputData) throws ServiceInvokerException {

		//Setup monitoring data and prepare stuff
		long requestMessageSize = inputData.getBytes().length;
		long startTime = System.currentTimeMillis();
		MonitoringInvocationInstance invocationinstance = initMonitoring(webserviceURL);

		String results = "";
		URL endpoint = webserviceURL;
		String soapFile = inputData;

		Service service = new Service();
		Call call;

		logger.debug("Invoking " + webserviceURL);
		logger.debug("Using SOAP-Message " + soapFile);
		logger.debug("Operation: " + operationName);

		try {

			//check if monitored
			if (invocationinstance != null) {
				invocationinstance.setState(MonitoringInvocationState.Started);
				logger.debug("Monitoring is activated");
			} 

			call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint);

			ByteArrayInputStream soapMessageStream = new ByteArrayInputStream(
					soapFile.getBytes("UTF-8"));

			SOAPEnvelope env = new SOAPEnvelope(soapMessageStream);

			results = call.invoke(env).toString();
			logger.debug("Obtainer results" + results);

			if (invocationinstance != null) {
				invocationinstance
						.setState(MonitoringInvocationState.Completed);
				long responseMessageSize = results.getBytes().length;
				long time = System.currentTimeMillis() - startTime;

				invocationinstance.sendSuccessfulInvocation(
						responseMessageSize, requestMessageSize, time);

				logger.debug("Performed monitoring. Invocation took: " + time
						+ " ms");
			}

		} catch (ServiceException | SAXException | AxisFault
				| UnsupportedEncodingException | MonitoringException e) {
			logger.error(e);
			if (invocationinstance != null) {
				try {
					invocationinstance.sendUnsuccessfulInvocation();
				} catch (MonitoringException e1) {
					throw new ServiceInvokerException(e1);
				}
			}
			throw new ServiceInvokerException(e);
		}

		return results;
	}

	private MonitoringInvocationInstance initMonitoring(URL webserviceURL) {

		if (this.monitoring == null) {
			return null;
		}
		try {
			if (monitoring.isMonitoredWebService(webserviceURL)) {
				System.out
						.println("Loading a invocationinstance into the invoker");
				MonitoringInvocationInstance invocationinstance = monitoring
						.createInvocationInstance(webserviceURL);
				invocationinstance
						.setState(MonitoringInvocationState.Instantiated);
				return invocationinstance;
			}
		} catch (MonitoringException e) {
			return null;
		}

		return null;
	}

	public MonitoringComponent getMonitoring() {
		return monitoring;
	}

	public void setMonitoring(MonitoringComponent monitoring) {
		this.monitoring = monitoring;
	}

}
