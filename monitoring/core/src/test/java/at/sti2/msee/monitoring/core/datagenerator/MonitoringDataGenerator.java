package at.sti2.msee.monitoring.core.datagenerator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;

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
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = null;
		try {
			data = new Object[][] {
					{ new MonitoringDataGeneratorParameters(new URL(
							"http://www.example.com/testdataws1"), 25, 50,
							1200, 50, 1200, 100, 1500, 0.05) },
					{ new MonitoringDataGeneratorParameters(new URL(
							"http://www.example.com/testdataws2"), 25, 50,
							1200, 50, 1200, 100, 1500, 0.05) } };
		} catch (MalformedURLException e) {
			fail();
		}
		return Arrays.asList(data);
	}

	@Ignore //Do not run (takes forever)
	@Test
	public void createDataTest() {
		MonitoringComponent m = null;
		try {
			m = MonitoringComponentImpl.getInstance();
		} catch (RepositoryException | IOException e) {
			fail();
		}

		for (int i = 0; i < this.invocationCount; i++) {
			if (Math.random() > this.failRate) {

				double payloadSizeResponse = this.minPayloadSizeResponse
						+ (Math.random() * (this.maxPayloadSizeResponse - this.minPayloadSizeResponse));
				double payloadSizeRequest = this.minPayloadSizeRequest
						+ (Math.random() * (this.maxPayloadSizeRequest - this.minPayloadSizeRequest));
				double responseTime = this.minResponseTime
						+ (Math.random() * (this.maxResponseTime - this.minResponseTime));

				try {
					m.addSuccessfulInvocationData(this.ws, payloadSizeResponse,
							payloadSizeRequest, responseTime);
				} catch (MonitoringException e) {
					fail();
				}
			} else {
				try {
					m.addUnsuccessfullInvocationData(this.ws);
				} catch (MonitoringException e) {
					fail();
				}
			}
		}
	}

}