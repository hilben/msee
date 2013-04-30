package at.sti2.msee.monitoring.api;

import java.net.URL;

import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStored;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;

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

	
	
	public void enableMonitoring(URL WebService) throws MonitoringException;

	public void disableMonitoring(URL WebService) throws MonitoringException;

	public MonitoringWebserviceAvailability getAvailability(URL webService)
			throws MonitoringException;

	public void clearAllContentOfWebservice(URL webService)
			throws MonitoringException;

	public void addSuccessfulInvocationData(URL webService,
			double payloadSizeResponse, double payloadSizeRequest,
			double responseTime) throws MonitoringException;

	public void addUnsuccessfullInvocationData(URL webService)
			throws MonitoringException;

	public QoSParameter getQoSParameter(URL webService, QoSType qostype)
			throws MonitoringException, MonitoringNoDataStored;

	void updateAvailabilityState(URL WebService,
			MonitoringWSAvailabilityState state) throws MonitoringException;
	
}
