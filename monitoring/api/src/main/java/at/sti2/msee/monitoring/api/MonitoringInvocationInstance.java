package at.sti2.msee.monitoring.api;

import java.net.URL;
import java.util.UUID;

import at.sti2.msee.monitoring.api.exception.MonitoringException;

public interface MonitoringInvocationInstance {

	public void updateInvocationState(MonitoringInvocationState state)
			throws MonitoringException;

	public void updateAndCloseSuccessfullInvocation(double payloadSizeResponse,
			double payloadSizeRequest, double responseTime)
			throws MonitoringException;

	public void updateAndCloseUnsuccessfullInvocation()
			throws MonitoringException;
	
	public URL getWebService();
	
	public UUID getUUID();
	
	public MonitoringInvocationState getState();
	
	public void setState(MonitoringInvocationState state) throws MonitoringException;

}
