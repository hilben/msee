package at.sti2.msee.monitoring.api;

public interface MonitoringComponent {

	public boolean isMonitoredWebService(/*TODO: how to identify webservices?*/);
	public MonitoringInvocationInstance getInvocationInstance(/*TODO: how to identify webservices?*/);
	
	public void monitorWebService(/*TODO: how to identify webservices?*/);
	
	public void unmonitorWebService(/*TODO: how to identify webservices?*/);
	
	public void getMonitoringData(/*TODO: how to identify webservices?*/);
	
}
