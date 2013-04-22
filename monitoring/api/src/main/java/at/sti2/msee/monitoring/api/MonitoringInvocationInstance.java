package at.sti2.msee.monitoring.api;

import at.sti2.msee.monitoring.api.exception.MonitoringInvocationInstanceException;

public interface MonitoringInvocationInstance {

	public void updateInvocationState(MonitoringInvocationState state)
			throws MonitoringInvocationInstanceException;

	public void updateAndCloseSuccessfullInvocation(double payloadSizeResponse,
			double payloadSizeRequest, double responseTime)
			throws MonitoringInvocationInstanceException;

	public void updateAndCloseUnsuccessfullInvocation()
			throws MonitoringInvocationInstanceException;

}
