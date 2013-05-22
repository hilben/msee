package at.sti2.msee.monitoring.core.datagenerator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.monitoring.core.MonitoringParameterStoreHandler;

import static org.junit.Assert.*;

/**
 * JUnit Parameterized Test
 * 
 * 
 */
@RunWith(value = Parameterized.class)
public class MonitoringDataGenerator {

	private URL ws;

	private long invocationCount;

	private long minPayloadSizeResponse;
	private long maxPayloadSizeResponse;

	private long minPayloadSizeRequest;
	private long maxPayloadSizeRequest;

	private long minResponseTime;
	private long maxResponseTime;

	private double failRate;

	private Date startDate;
	private long timeMinutes;

	public MonitoringDataGenerator(MonitoringDataGeneratorParameters gen) {
		this.ws = gen.ws;
		this.invocationCount = gen.invocationCount;
		this.minPayloadSizeResponse = gen.minPayloadSizeResponse;
		this.maxPayloadSizeResponse = gen.maxPayloadSizeResponse;
		this.minPayloadSizeRequest = gen.minPayloadSizeRequest;
		this.maxPayloadSizeRequest = gen.maxPayloadSizeRequest;
		this.minResponseTime = gen.minResponseTime;
		this.maxResponseTime = gen.maxResponseTime;
		this.failRate = gen.failRate;
		this.startDate = gen.startDate;
		this.timeMinutes = gen.timeMinutes;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = null;
		try {
			data = new Object[][] {
					{ new MonitoringDataGeneratorParameters(new URL(
							"http://www.example.com/testdataws1"), 25, 50,
							1200, 50, 1200, 100, 1500, 0.05, new Date(),
							10000) },
					{ new MonitoringDataGeneratorParameters(new URL(
							"http://www.example.com/testdataws2"), 25, 50,
							1200, 50, 1200, 100, 1500, 0.05, new Date(),
							10000) } };
		} catch (MalformedURLException e) {
			fail();
		}
		return Arrays.asList(data);
	}



	@Test
	public void createDataTest() {
		MonitoringComponent m = null;
		try {
			m = MonitoringComponentImpl.getInstance();
		} catch (RepositoryException | IOException e) {
			fail();
		}

		for (int i = 0; i < this.invocationCount; i++) {
			
			
			//Create a random date within the range
			long dtime = (long)(Math.random()*(this.timeMinutes*1000*60));
			Date rndDate = new Date(this.startDate.getTime() + dtime);
			
			
			if (Math.random() > this.failRate) {

				double payloadSizeResponse = this.minPayloadSizeResponse
						+ (Math.random() * (this.maxPayloadSizeResponse - this.minPayloadSizeResponse));
				double payloadSizeRequest = this.minPayloadSizeRequest
						+ (Math.random() * (this.maxPayloadSizeRequest - this.minPayloadSizeRequest));
				double responseTime = this.minResponseTime
						+ (Math.random() * (this.maxResponseTime - this.minResponseTime));


				
				
				try {
					MonitoringParameterStoreHandler.addSuccessfulInvocation(
							this.ws, payloadSizeResponse, payloadSizeRequest,
							responseTime,rndDate);
				} catch (MonitoringException | RepositoryException
						| MalformedQueryException | UpdateExecutionException
						| IOException | ParseException e) {
					fail();
				}
			} else {
				try {
					MonitoringParameterStoreHandler.addUnsuccessfulInvocation(this.ws,rndDate);
				} catch (MonitoringException | RepositoryException | MalformedQueryException | UpdateExecutionException | ParseException | IOException e) {
					fail();
				}
			}
		}
	}

	public static void main(String args[]) {

		Date d = new Date();
		long dtime = 1000;
		for (int i = 0; i < 100; i++) {
			long z = (long)(Math.random()*(dtime*1000*60));
			Date x = new Date(d.getTime() + z);
			System.out.println(x);
		}
	}

}