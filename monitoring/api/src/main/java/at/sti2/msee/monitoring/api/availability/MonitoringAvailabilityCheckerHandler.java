package at.sti2.msee.monitoring.api.availability;

import java.net.URL;
import java.util.List;

import at.sti2.msee.monitoring.api.exception.MonitoringException;

public interface MonitoringAvailabilityCheckerHandler {

	/**
	 * @param url
	 * @throws MonitoringException
	 */
	public void addEndpoint(URL url) throws MonitoringException;

	/**
	 * @param url
	 */
	public void removeEndpoint(URL url);

	/**
	 * @param url
	 * @return
	 */
	public boolean isCheckingEndpoint(URL url);

	/**
	 * @return
	 */
	public List<URL> getCheckedEndpoints();

}
