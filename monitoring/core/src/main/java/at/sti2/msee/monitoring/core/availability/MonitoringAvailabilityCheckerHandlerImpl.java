package at.sti2.msee.monitoring.core.availability;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.availability.MonitoringAvailabilityCheckerHandler;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;

public class MonitoringAvailabilityCheckerHandlerImpl implements
		MonitoringAvailabilityCheckerHandler {

	private List<URL> checkedEndpoints;

	private ExecutorService executor;

	private MonitoringComponent monitoringComponent;

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	private static MonitoringAvailabilityCheckerHandlerImpl availChecker = null;
	
	private MonitoringAvailabilityCheckerHandlerImpl() throws RepositoryException, IOException {
		this.executor = Executors.newCachedThreadPool();
		this.checkedEndpoints = Collections.synchronizedList(new ArrayList<URL>());
		this.monitoringComponent = MonitoringComponentImpl.getInstance();
	}
	
	public static MonitoringAvailabilityCheckerHandlerImpl getInstance() throws RepositoryException, IOException {
		if (availChecker==null) {
			availChecker = new MonitoringAvailabilityCheckerHandlerImpl();
		}
		
		return availChecker;
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
