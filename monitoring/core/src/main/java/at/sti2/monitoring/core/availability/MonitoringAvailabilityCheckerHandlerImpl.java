package at.sti2.monitoring.core.availability;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import at.sti2.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.availability.MonitoringAvailabilityCheckerHandler;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

public class MonitoringAvailabilityCheckerHandlerImpl implements
		MonitoringAvailabilityCheckerHandler {

	private ArrayList<URL> checkedEndpoints;

	private ExecutorService executor;

	private MonitoringComponent monitoringComponent;

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	public MonitoringAvailabilityCheckerHandlerImpl(
			MonitoringComponent monitoringComponent) {
		this.executor = Executors.newCachedThreadPool();
		this.checkedEndpoints = new ArrayList<URL>();
		this.monitoringComponent = monitoringComponent;
	}

	@Override
	public void addEndpoint(URL url) throws MonitoringException {

		if (!this.checkedEndpoints.contains(url)) {
			this.checkedEndpoints.add(url);
			
			Runnable checker = new MonitoringAvailabilityCheckerImpl(url, this,
					this.monitoringComponent);
			executor.execute(checker);
		}
	}

	@Override
	public void removeEndpoint(URL url) {
		this.checkedEndpoints.remove(url);
	}

	@Override
	public boolean isCheckingEndpoint(URL url) {
		return this.checkedEndpoints.contains(url);
	}

	@Override
	public ArrayList<URL> getCheckedEndpoints() {
		return new ArrayList<URL>(this.checkedEndpoints);
	}

	@Override
	public String toString() {
		return "MonitoringAvailabilityCheckerHandlerImpl [checkedEndpoints="
				+ checkedEndpoints + ", executor=" + executor + "]";
	}

}
