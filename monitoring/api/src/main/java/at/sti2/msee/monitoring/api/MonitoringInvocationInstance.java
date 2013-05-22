package at.sti2.msee.monitoring.api;

import java.net.URL;
import java.util.UUID;

import at.sti2.msee.monitoring.api.exception.MonitoringException;

/**
 * @author benni
 * 
 *         Objects of this class are used by the invoker to communicate with the
 *         monitoring component
 * 
 */
public interface MonitoringInvocationInstance {

	/**
	 * Updates the invocation state
	 * 
	 * @param state
	 * @throws MonitoringException
	 */
	public void updateInvocationState(MonitoringInvocationState state)
			throws MonitoringException;

	/**
	 * Informs the monitoring component about a successful invocation
	 * 
	 * @param payloadSizeResponse
	 * @param payloadSizeRequest
	 * @param responseTime
	 * @throws MonitoringException
	 */
	public void sendSuccessfulInvocation(double payloadSizeResponse,
			double payloadSizeRequest, double responseTime)
			throws MonitoringException;

	/**
	 * Informs the monitoring component about an unsuccessful invocation
	 * 
	 * @throws MonitoringException
	 */
	public void sendUnsuccessfulInvocation() throws MonitoringException;

	/**
	 * @return
	 */
	public URL getWebService();

	/**
	 * @return
	 */
	public UUID getUUID();

	/**
	 * @return
	 */
	public MonitoringInvocationState getState();

	/**
	 * @param state
	 * @throws MonitoringException
	 */
	public void setState(MonitoringInvocationState state)
			throws MonitoringException;

}
