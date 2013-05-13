package at.sti2.msee.monitoring.availability;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.availability.MonitoringAvailabilityCheckerHandler;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.monitoring.core.availability.MonitoringAvailabilityCheckerHandlerImpl;

public class MonitoringAvailabilityCheckerHandlerImplTest {

	private MonitoringAvailabilityCheckerHandler handler;

	private String testWebService1 = new String("http://www.test.com/test");
	private URL testWebService1url = null;
	private String testWebService2 = new String("http://www.test.com/test2");
	private URL testWebService2url = null;

	private MonitoringComponent monitoringComponent;

	@Before
	public void setUp() throws Exception {
		this.monitoringComponent = new MonitoringComponentImpl();
		this.handler = new MonitoringAvailabilityCheckerHandlerImpl(
				this.monitoringComponent);
		this.testWebService1url = new URL(testWebService1);
		this.testWebService2url = new URL(testWebService2);
	}

	@Test
	public void testAddEndpoint() {
		try {
			this.handler.addEndpoint(testWebService1url);
		} catch (MonitoringException e) {
			fail();
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
			fail();
		}
	}

	@Test
	public void testGetCheckedEndpoints() {
		try {
			this.handler.addEndpoint(testWebService1url);

			this.handler.addEndpoint(testWebService2url);

			this.handler.addEndpoint(testWebService1url);
			this.handler.addEndpoint(testWebService2url);

			assertTrue(this.handler.getCheckedEndpoints().size() == 2);

		} catch (MonitoringException e) {
			fail();
		}
	}
	

	@After
	public void tearDown() {
		this.handler.removeEndpoint(testWebService1url);
		this.handler.removeEndpoint(testWebService2url);
	}

}
