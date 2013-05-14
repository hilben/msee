package at.sti2.msee.monitoring.api;

import java.net.URL;
import java.util.Date;
import java.util.List;

import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailability;
import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;

/**
 * @author benni
 * 
 */
public interface MonitoringComponent {

	/**
	 * Return whether a webservice is currently monitored
	 * 
	 * @return
	 */
	public boolean isMonitoredWebService(URL WebService)
			throws MonitoringException;

	/**
	 * Returns an invocation instance generated for a web service.
	 * 
	 * @return
	 */
	public MonitoringInvocationInstance createInvocationInstance(URL WebService)
			throws MonitoringException;

	/**
	 * 
	 * Returns an invocation instance accordin to its id
	 * 
	 * @param UUID
	 * @return
	 * @throws MonitoringException
	 */
	public MonitoringInvocationInstance getInvocationInstance(String UUID)
			throws MonitoringException;

	/**
	 * Stores the data of the instance in the db.
	 * 
	 * @param instance
	 * @throws MonitoringException
	 */
	void updateInvocationInstance(MonitoringInvocationInstance instance)
			throws MonitoringException;

	/**
	 * Enables the monitoring for a webservice. Therefore whenever the
	 * invocation component is invoking a webservice it is sending monitoring
	 * data for this ws. Also a thread is started which periodically checks the
	 * availability of the webservice
	 * 
	 * @param WebService
	 * @throws MonitoringException
	 */
	public void enableMonitoring(URL WebService) throws MonitoringException;

	/**
	 * Removes the webservice from the monitoring component
	 * 
	 * @param WebService
	 * @throws MonitoringException
	 */
	public void disableMonitoring(URL WebService) throws MonitoringException;

	/**
	 * @param webService
	 * @return
	 * @throws MonitoringException
	 */
	public MonitoringWebserviceAvailability getAvailability(URL webService)
			throws MonitoringException;

	/**
	 * @param webService
	 * @throws MonitoringException
	 */
	public void clearAllContentOfWebservice(URL webService)
			throws MonitoringException;

	/**
	 * @param webService
	 * @param payloadSizeResponse
	 * @param payloadSizeRequest
	 * @param responseTime
	 * @throws MonitoringException
	 */
	public void addSuccessfulInvocationData(URL webService,
			double payloadSizeResponse, double payloadSizeRequest,
			double responseTime) throws MonitoringException;

	/**
	 * @param webService
	 * @throws MonitoringException
	 */
	public void addUnsuccessfullInvocationData(URL webService)
			throws MonitoringException;

	/**
	 * @param webService
	 * @param qostype
	 * @return
	 * @throws MonitoringException
	 * @throws MonitoringNoDataStoredException
	 */
	public QoSParameter getQoSParameter(URL webService, QoSType qostype)
			throws MonitoringException, MonitoringNoDataStoredException;

	/**
	 * @param webService
	 * @param qostype
	 * @param start
	 * @param end
	 * @return
	 * @throws MonitoringException
	 * @throws MonitoringNoDataStoredException
	 */
	public List<QoSParameter> getQoSParametersInTimeframe(URL webService,
			QoSType qostype, Date start, Date end) throws MonitoringException,
			MonitoringNoDataStoredException;

	/**
	 * @param webService
	 * @param start
	 * @param end
	 * @return
	 * @throws MonitoringException
	 * @throws MonitoringNoDataStoredException
	 */
	public List<MonitoringWebserviceAvailabilityState> getAvailabilityStatesInTimeframe(
			URL webService, Date start, Date end) throws MonitoringException,
			MonitoringNoDataStoredException;

	/**
	 * @param webService
	 * @param start
	 * @param end
	 * @return
	 * @throws MonitoringException
	 * @throws MonitoringNoDataStoredException
	 */
	public List<MonitoringInvocationInstance> getInvocationInstancesInTimeframe(
			URL webService, Date start, Date end) throws MonitoringException,
			MonitoringNoDataStoredException;

	/**
	 * @param WebService
	 * @param state
	 * @throws MonitoringException
	 */
	void updateAvailabilityState(URL WebService,
			MonitoringWebserviceAvailabilityState state)
			throws MonitoringException;

}
