package at.sti2.msee.monitoring.availability;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.*;

import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.monitoring.api.availability.MonitoringAvailabilityCheckerHandler;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.availability.MonitoringAvailabilityCheckerHandlerImpl;

public class MonitoringAvailabilityCheckerHandlerImplTest {

	private MonitoringAvailabilityCheckerHandler handler;

	private String testWebService1 = new String("http://www.test.com/test");
	private URL testWebService1url = null;
	private String testWebService2 = new String("http://www.test.com/test2");
	private URL testWebService2url = null;


	@Before
	public void setUp() throws Exception {
		this.handler = MonitoringAvailabilityCheckerHandlerImpl.getInstance();
		List<URL> endpoints = this.handler.getCheckedEndpoints();
		for(URL endpoint : endpoints){
			this.handler.removeEndpoint(endpoint);
		}
		this.testWebService1url = new URL(testWebService1);
		this.testWebService2url = new URL(testWebService2);
	}

	@Test
	public void testAddEndpoint() {
		try {
			this.handler.addEndpoint(testWebService1url);
		} catch (MonitoringException e) {
			fail(e.getMessage());
		}

		assertTrue(this.handler.isCheckingEndpoint(testWebService1url));
		assertFalse(this.handler.isCheckingEndpoint(testWebService2url));

	}

	@Test
	public void testRemoveEndpoint() {
		try {
			this.handler.addEndpoint(testWebService1url);

			this.handler.addEndpoint(testWebService2url);

			assertTrue(this.handler.isCheckingEndpoint(testWebService1url));
			assertTrue(this.handler.isCheckingEndpoint(testWebService2url));

			this.handler.removeEndpoint(testWebService1url);
			this.handler.removeEndpoint(testWebService2url);
			this.handler.removeEndpoint(testWebService2url);

			assertFalse(this.handler.isCheckingEndpoint(testWebService1url));
			assertFalse(this.handler.isCheckingEndpoint(testWebService2url));

			this.handler.addEndpoint(testWebService2url);

			assertTrue(this.handler.isCheckingEndpoint(testWebService2url));
			assertFalse(this.handler.isCheckingEndpoint(testWebService1url));
		} catch (MonitoringException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetCheckedEndpoints() {
		try {
			int currentSize = this.handler.getCheckedEndpoints().size();
			Assert.assertThat(currentSize, is(0));
			this.handler.addEndpoint(testWebService1url);

			this.handler.addEndpoint(testWebService2url);

			this.handler.addEndpoint(testWebService1url);
			this.handler.addEndpoint(testWebService2url);

			currentSize = this.handler.getCheckedEndpoints().size();
			assertTrue(currentSize + " not 2", currentSize == 2);

		} catch (MonitoringException e) {
			fail(e.getMessage());
		}
	}
	

	@After
	public void tearDown() {
		this.handler.removeEndpoint(testWebService1url);
		this.handler.removeEndpoint(testWebService2url);
	}

}
