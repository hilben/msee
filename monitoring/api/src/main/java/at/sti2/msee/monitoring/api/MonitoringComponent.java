package at.sti2.msee.monitoring.api;

public interface MonitoringComponent {

	
	/**
	 * Return whether a webservice is currently monitored
	 * @return
	 */
	public boolean isMonitoredWebService(/*TODO: how to identify webservices?*/);
	
	/**
	 * Returns an invocation instance generated for the web service.
	 * Returns null if no such webservice is monitored
	 * @return
	 */
	public MonitoringInvocationInstance getInvocationInstance(/*TODO: how to identify webservices?*/);
	
	public void enableMonitoring(/*TODO: how to identify webservices?*/);
	
	public void disableMonitoring(/*TODO: how to identify webservices?*/);
	
	public void getMonitoringData(/*TODO: how to identify webservices?*/);
	
	public void updateAvailabilityState(/*TODO: how to identify webservices?*/);
	
}
