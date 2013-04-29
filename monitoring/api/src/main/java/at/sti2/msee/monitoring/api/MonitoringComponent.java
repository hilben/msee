package at.sti2.msee.monitoring.api;

import java.net.URL;

import at.sti2.msee.monitoring.api.exception.MonitoringException;

public interface MonitoringComponent {

	/**
	 * Return whether a webservice is currently monitored
	 * 
	 * @return
	 */
	public boolean isMonitoredWebService(URL WebService) throws MonitoringException;

	/**
	 * Returns an invocation instance generated for the web service. Returns
	 * null if no such webservice is monitored
	 * 
	 * @return
	 */
	public MonitoringInvocationInstance createInvocationInstance(URL WebService)
			throws MonitoringException;

	public MonitoringInvocationInstance getInvocationInstance(
			String UUID) throws MonitoringException;
	
	void updateInvocationInstance(MonitoringInvocationInstance instance)
			throws MonitoringException; 

	public void enableMonitoring(URL WebService) throws MonitoringException;

	public void disableMonitoring(URL WebService) throws MonitoringException;

	public void getMonitoringData(URL WebService) throws MonitoringException;
	
	public void updateAvailabilityState(URL WebService)
			throws MonitoringException;

	public MonitoringWSAvailabilityState getAvailabilityState(URL webService)
			throws MonitoringException;
	
	public void clearAllContentOfWebservice(URL webService) throws MonitoringException;



}
