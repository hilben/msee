package at.sti2.monitoring.core.availability;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.reasoner.rulesys.builtins.Min;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.availability.MonitoringAvailabilityChecker;
import at.sti2.msee.monitoring.api.availability.MonitoringAvailabilityCheckerHandler;
import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

public class MonitoringAvailabilityCheckerImpl implements
		MonitoringAvailabilityChecker {

//	public static long TIMEOUT_MS = 30*1000;
//	public static long INTERVALL_SECONDS = 60*60;
	
	public static int TIMEOUT_MS =2000;
	public static long INTERVALL_SECONDS =5;
	
	private MonitoringAvailabilityCheckerHandler handler;
	private URL url;
	private MonitoringComponent monitoringComponent;

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	public MonitoringAvailabilityCheckerImpl(URL url,
			MonitoringAvailabilityCheckerHandler handler,
			MonitoringComponent monitoringComponent) throws MonitoringException {
		this.url = url;
		this.handler = handler;
		this.monitoringComponent = monitoringComponent;

		this.monitoringComponent.updateAvailabilityState(this.url,
				MonitoringWebserviceAvailabilityState.NotChecked);
	}

	@Override
	public void run() {
		try {
			while (this.handler.isCheckingEndpoint(this.url)) {
				if (this.isAdressIsAvailable(url)) {
					System.out.println(url + " is available");
					this.monitoringComponent.updateAvailabilityState(this.url,
							MonitoringWebserviceAvailabilityState.Available);

				} else {
					System.out.println(url + " is not available");
					this.monitoringComponent.updateAvailabilityState(this.url,
							MonitoringWebserviceAvailabilityState.Unavailable);

				}
				
				Thread.sleep(INTERVALL_SECONDS*1000);
			}
			this.monitoringComponent.updateAvailabilityState(this.url,
					MonitoringWebserviceAvailabilityState.NotChecked);
			
		} catch (MonitoringException | InterruptedException e) {
			try {
				LOGGER.error("Error updating availability of webservice ", e);
				this.monitoringComponent.updateAvailabilityState(url,
						MonitoringWebserviceAvailabilityState.NotChecked);
			} catch (MonitoringException e1) {
				LOGGER.error("Error checking availability of webservice ", e);
			}
		}
	}

	private boolean isAdressIsAvailable(URL url) {

		HttpURLConnection httpUrlConn = null;

		try {
			httpUrlConn = (HttpURLConnection) url.openConnection();

//			httpUrlConn.setRequestMethod("HEAD");

			// Set timeouts in milliseconds
			httpUrlConn.setConnectTimeout(TIMEOUT_MS);
			httpUrlConn.setReadTimeout(TIMEOUT_MS);


			LOGGER.info("Response Code: "
					+ httpUrlConn.getResponseCode());
			LOGGER.info("Response Message: "
					+ httpUrlConn.getResponseMessage());
			
			System.out.println(httpUrlConn.getResponseCode());
			System.out.println(httpUrlConn.getResponseMessage());
			System.out.println(httpUrlConn.getContent().toString());
			System.out.println(httpUrlConn.getContentLengthLong());
			System.out.println(httpUrlConn.getContentType());

			boolean isAvailable = (httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK);
			
			httpUrlConn.disconnect();
			
			return isAvailable;
		} catch (IOException e) {
			
			httpUrlConn.disconnect();
			return false;
		}
	}
	
}