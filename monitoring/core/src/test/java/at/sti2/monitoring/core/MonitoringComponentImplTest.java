package at.sti2.monitoring.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

public class MonitoringComponentImplTest {

	private MonitoringComponent component;
	
	private String testServiceURL = "http://www.example.com/testwebservice";
	
	private URL testService = null;
	
	@Before
	public void setUp() throws Exception {
		component = new MonitoringComponentImpl();
		testService = new URL(testServiceURL);
		
		assertNotNull(component);
		assertNotNull(testService);
		
		component.enableMonitoring(testService);
	}

	@Test
	public void testIsMonitoredWebService() {
		try {
			assertTrue(component.isMonitoredWebService(testService));
		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGetInvocationInstance() {
		try {
			assertNotNull(component.getInvocationInstance(testService));
		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testDisableMonitoring() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMonitoringData() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAvailabilityState() {
		fail("Not yet implemented");
	}

}
