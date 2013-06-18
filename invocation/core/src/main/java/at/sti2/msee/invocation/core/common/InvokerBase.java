package at.sti2.msee.invocation.core.common;

import java.net.URL;
import java.util.List;
import org.apache.log4j.Logger;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

/**
 * Base class for all invocation types (SOAP, REST).
 * 
 * @author Christian Mayr
 * 
 */
public abstract class InvokerBase {
	protected Logger logger = Logger.getLogger(this.getClass());
	protected MonitoringComponent monitoring = null;
	MonitoringInvocationInstance invocationinstance = null;

	public InvokerBase(MonitoringComponent monitoring) {
		this.monitoring = monitoring;
	}

	/**
	 * Initializes the Monitoring for the given web service.
	 * 
	 * @param webserviceURL
	 * @throws ServiceInvokerException
	 */
	protected void initMonitoring(URL webserviceURL) throws ServiceInvokerException {
		if (this.monitoring == null) {
			// throw new ServiceInvokerException("Monitoring not set");
			logger.info("Monitoring not set");
			return;
		}
		try {
			if (monitoring.isMonitoredWebService(webserviceURL)) {
				logger.debug("Loading a invocationinstance into the invoker");
				invocationinstance = monitoring.createInvocationInstance(webserviceURL);
				invocationinstance.setState(MonitoringInvocationState.Instantiated);
			}
		} catch (MonitoringException e) {
			logger.error(e.toString(), e);
		}
	}

	/**
	 * This method is called when the invocation of a service starts.
	 * 
	 * @throws MonitoringException
	 */
	protected void startMonitoring() throws MonitoringException {
		// check if monitored
		if (invocationinstance != null) {
			invocationinstance.setState(MonitoringInvocationState.Started);
			logger.debug("Monitoring is activated");
		}
	}

	/**
	 * This method is called when the invocation of a service has completed
	 * successfully.
	 * 
	 * @param startTime
	 * @param requestMessageSize
	 * @param output
	 */
	protected void successfulInvocation(long startTime, long requestMessageSize, String output) {
		if (invocationinstance != null) {
			try {
				invocationinstance.setState(MonitoringInvocationState.Completed);
				long responseMessageSize = output.getBytes().length;
				long time = System.currentTimeMillis() - startTime;

				invocationinstance.sendSuccessfulInvocation(responseMessageSize,
						requestMessageSize, time);

				logger.debug("Performed monitoring. Invocation took: " + time + " ms");
			} catch (MonitoringException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is called when a invocation was not successful.
	 * 
	 * @throws ServiceInvokerException
	 */
	protected void monitorFailedService() throws ServiceInvokerException {
		if (invocationinstance != null) {
			try {
				invocationinstance.sendUnsuccessfulInvocation();
			} catch (MonitoringException e) {
				throw new ServiceInvokerException(e);
			}
		}
	}

	/**
	 * Returns the size of the parameters.
	 */
	protected long getParameterSize(List<Parameter> parameters) {
		long size = 0;
		for (Pair<String, String> entrySet : parameters) {
			size += entrySet.value().length();
		}
		return size;
	}

}
