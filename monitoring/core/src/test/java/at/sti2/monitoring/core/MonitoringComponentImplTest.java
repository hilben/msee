package at.sti2.monitoring.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

public class MonitoringComponentImplTest {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());
	
	private MonitoringComponent component;

	private String testServiceURL = "http://www.example.com/testwebservice";

	private URL testService = null;

	@Before
	public void setUp() throws Exception {
		System.out.println("before");
		
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
			MonitoringInvocationInstance instance = component.createInvocationInstance(testService);
			
			assertNotNull(instance);
			
			assertTrue(instance.getState()==MonitoringInvocationState.None);
			
			MonitoringInvocationInstance back =  component.getInvocationInstance(instance.getUUID().toString());
			
			assertTrue(back.getState()==instance.getState());
			assertTrue(back.getUUID().toString().compareTo(instance.getUUID().toString())==0);
			
			instance.setState(MonitoringInvocationState.Terminated);
			
			back  =  component.getInvocationInstance(instance.getUUID().toString());
			
			assertTrue(back.getState()==instance.getState());
			
			back.setState(MonitoringInvocationState.Started);
			
			back = component.getInvocationInstance(back.getUUID().toString());;
			
			assertTrue(back.getState()==MonitoringInvocationState.Started);
			
		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testDisableMonitoring() {
		try {
			component.disableMonitoring(testService);
			assertFalse(component.isMonitoredWebService(testService));
		} catch (MonitoringException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMonitoringData() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateAvailabilityState() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testClearAllContentOfWebservice() {
		try {
			component.clearAllContentOfWebservice(testService);
			assertFalse(component.isMonitoredWebService(testService));
		} catch (MonitoringException e) {
			e.printStackTrace();
		}
	}

}
