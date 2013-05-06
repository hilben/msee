package at.sti2.msee.monitoring.api.availability;

import java.net.URL;
import java.util.ArrayList;

import at.sti2.msee.monitoring.api.exception.MonitoringException;

public interface MonitoringAvailabilityCheckerHandler {

	public void addEndpoint(URL url) throws MonitoringException;

	public void removeEndpoint(URL url);

	public boolean isCheckingEndpoint(URL url);

	public ArrayList<URL> getCheckedEndpoints();

}
