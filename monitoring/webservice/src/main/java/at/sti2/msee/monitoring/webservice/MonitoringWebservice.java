package at.sti2.msee.monitoring.webservice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.jws.WebService;

import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;

@WebService(targetNamespace = "http://msee.sti2.at/delivery/monitoring/", endpointInterface = "at.sti2.msee.monitoring.webservice.MonitoringWebservice", portName = "MonitoringServicePort", serviceName = "service")
public class MonitoringWebservice {
	MonitoringComponent monitoringComponent;

	public MonitoringWebservice() {
		try {
			monitoringComponent = MonitoringComponentImpl.getInstance();
		} catch (RepositoryException | IOException e) {
			e.printStackTrace();
		}
	}

	public boolean enableMonitoring(String serviceURI) throws MonitoringException {
		monitoringComponent.enableMonitoring(getURL(serviceURI));
		return monitoringComponent.isMonitoredWebService(getURL(serviceURI));
	}

	public boolean disableMonitoring(String serviceURI) throws MonitoringException {
		monitoringComponent.disableMonitoring(getURL(serviceURI));
		return monitoringComponent.isMonitoredWebService(getURL(serviceURI));
	}

	public String getMetrics(String serviceURI) throws MonitoringException,
			MonitoringNoDataStoredException {
		StringBuilder sb = new StringBuilder();
		for (QoSType t : QoSType.values()) {
			sb.append(monitoringComponent.getQoSParameter(getURL(serviceURI), t));
			sb.append("\n");
		}
		return sb.toString();
	}

	private URL getURL(String serviceURI) throws MonitoringException {
		try {
			return new URL(serviceURI);
		} catch (MalformedURLException e) {
			throw new MonitoringException(e.getCause());
		}
	}
}
