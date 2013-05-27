package at.sti2.msee.monitoring.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;

public class MonitoringComponentImplTest {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	private MonitoringComponent component;

	private String testServiceURL1 = "http://www.example.com/testwebservice";
	private String testServiceURL2 = "http://www.example.com/testwebservice2";

	private URL testService1 = null;
	private URL testService2 = null;

	@Before
	public void setUp() {
		try {
			component = MonitoringComponentImpl.getInstance();

			testService1 = new URL(testServiceURL1);
			testService2 = new URL(testServiceURL2);

			assertNotNull(component);
			assertNotNull(testService1);
			assertNotNull(testService2);

			component.clearAllContentOfWebservice(testService1);
			component.clearAllContentOfWebservice(testService2);
		} catch (MonitoringException | RepositoryException | IOException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Test
	public void testIsMonitoredWebService() {
		try {
			component.enableMonitoring(testService1);
			assertTrue(component.isMonitoredWebService(testService1));
		} catch (MonitoringException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Test
	public void testQueryParametersOnEmptyContext() {
		boolean catchNoDataException = false;
		try {
			component.clearAllContentOfWebservice(testService2);

			component.getQoSParameter(testService2, QoSType.RequestTotal);

		} catch (MonitoringException e) {
			fail(Arrays.toString(e.getStackTrace()));
		} catch (MonitoringNoDataStoredException e) {
			catchNoDataException = true;
		}

		assertTrue(catchNoDataException);
	}

	@Test
	public void testGetInvocationInstance() {
		try {
			MonitoringInvocationInstance instance = component
					.createInvocationInstance(testService1);

			assertNotNull(instance);

			assertTrue(instance.getState() == MonitoringInvocationState.None);

			MonitoringInvocationInstance back = component
					.getInvocationInstance(instance.getUUID().toString());

			assertTrue(back.getState() == instance.getState());
			assertTrue(back.getUUID().toString()
					.compareTo(instance.getUUID().toString()) == 0);

			instance.setState(MonitoringInvocationState.Terminated);

			back = component.getInvocationInstance(instance.getUUID()
					.toString());

			assertTrue(back.getState() == instance.getState());

			back.setState(MonitoringInvocationState.Started);

			back = component.getInvocationInstance(back.getUUID().toString());

			assertTrue(back.getState() == MonitoringInvocationState.Started);

			MonitoringInvocationInstance i = component
					.createInvocationInstance(new URL(
							"http://aaa.someurl.aaa/whichdonotexists"));
			assertNotNull(i);

		} catch (MonitoringException | MalformedURLException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Test
	public void testDisableMonitoring() {
		try {
			component.disableMonitoring(testService1);
			assertFalse(component.isMonitoredWebService(testService1));
		} catch (MonitoringException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Test
	public void testUpdateAvailabilityState() {
		MonitoringWebserviceAvailabilityState state1 = MonitoringWebserviceAvailabilityState.Available;
		MonitoringWebserviceAvailabilityState state2 = MonitoringWebserviceAvailabilityState.Unavailable;
		try {
			component.updateAvailabilityState(testService1, state1, 1234.0);
			component.updateAvailabilityState(testService1, state2, 4321.0);

			assertTrue("5555.0".compareTo(component.getQoSParameter(
					testService1, QoSType.MonitoredTime).getValue())==0);
			String availableTime = component.getQoSParameter(testService1, QoSType.AvailableTime).getValue();
			Assert.assertThat(availableTime, is("1234.0"));
			assertTrue("4321.0".compareTo(component.getQoSParameter(
					testService1, QoSType.UnavailableTime).getValue())==0);
			
		} catch (MonitoringException | MonitoringNoDataStoredException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Test
	public void testGetAvailability() {
		MonitoringWebserviceAvailabilityState state1 = MonitoringWebserviceAvailabilityState.NotChecked;
		MonitoringWebserviceAvailabilityState state2 = MonitoringWebserviceAvailabilityState.Unavailable;
		MonitoringWebserviceAvailabilityState state3 = MonitoringWebserviceAvailabilityState.Available;

		try {
			component.updateAvailabilityState(testService1, state1, 1);
			component.updateAvailabilityState(testService1, state2, 1);
			component.updateAvailabilityState(testService1, state3, 1);

			component.updateAvailabilityState(testService2, state2, 1);

			assertTrue(component.getAvailability(testService1).getState() == state3);
			assertTrue(component.getAvailability(testService2).getState() == state2);

			assertFalse(component.getAvailability(testService2).getState() == state1);

		} catch (MonitoringException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Test
	public void testClearAllContentOfWebservice() {
		try {
			component.clearAllContentOfWebservice(testService1);
			assertFalse(component.isMonitoredWebService(testService1));
		} catch (MonitoringException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
	}
	
	@Test
	public void testAddInvocationData() {
		try {
			component.addSuccessfulInvocationData(testService1, -100.0, 100.0, 100.0);
			component.addSuccessfulInvocationData(testService1, 50.0, 100.0, 100.0);
			component.addSuccessfulInvocationData(testService1, 33.0, 100.0, 100.0);
			component.addSuccessfulInvocationData(testService1, -9.7, 100.0, 100.0);

			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.RequestSuccessful)) == 4.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.RequestTotal)) == 4.0);

			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeRequest)) == 100.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeRequestTotal)) == 400.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeRequestAverage)) == 100.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeRequestMaximum)) == 100.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeRequestMinimum)) == 100.0);

			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponse)) == -9.7);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseTotal)) == -26.7);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseAverage)) == -6.675);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseMaximum)) == 50.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseMinimum)) == -100.0);
			assertFalse(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseTotal)) == -26.71);
			assertFalse(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseAverage)) == -6.674);

			component.addUnsuccessfullInvocationData(testService1);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseAverage)) == -6.675);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseMaximum)) == 50.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.PayloadSizeResponseMinimum)) == -100.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.RequestSuccessful)) == 4.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.RequestTotal)) == 5.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.RequestFailed)) == 1.0);

			component.addSuccessfulInvocationData(testService1, 33, 100, 100);
			component.addSuccessfulInvocationData(testService1, -9.7, 100, 100);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.ResponseTime)) == 100.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.ResponseTimeTotal)) == 600.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.ResponseTimeAverage)) == 100.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.ResponseTimeMaximum)) == 100.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.ResponseTimeMinimum)) == 100.0);

			component.addUnsuccessfullInvocationData(testService1);
			component.addUnsuccessfullInvocationData(testService1);
			component.addUnsuccessfullInvocationData(testService1);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.RequestTotal)) == 10.0);
			assertTrue(extractDoubleFromParameter(component.getQoSParameter(
					testService1, QoSType.RequestFailed)) == 4.0);

		} catch (MonitoringException | MonitoringNoDataStoredException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
	}

	@Ignore
	private double extractDoubleFromParameter(QoSParameter p) {
		double ret = Double.parseDouble(p.getValue());
		return ret;
	}

	@Test
	public void testGetQoSParameter() {
		try {
			component.addSuccessfulInvocationData(testService1, 101, 102, 103);

			QoSParameter payRespo = component.getQoSParameter(testService1,
					QoSType.PayloadSizeResponse);
			QoSParameter payReq = component.getQoSParameter(testService1,
					QoSType.PayloadSizeRequest);
			QoSParameter respTime = component.getQoSParameter(testService1,
					QoSType.ResponseTime);

			QoSParameter respTimeAve = component.getQoSParameter(testService1,
					QoSType.ResponseTimeAverage);

			assertTrue(payRespo.getValue().compareTo("101.0") == 0);
			assertTrue(payReq.getValue().compareTo("102.0") == 0);
			assertTrue(respTime.getValue().compareTo("103.0") == 0);

			assertTrue(payRespo.getType() == QoSType.PayloadSizeResponse);
			assertTrue(payReq.getType() == QoSType.PayloadSizeRequest);
			assertTrue(respTime.getType() == QoSType.ResponseTime);
			assertTrue(respTimeAve.getType() == QoSType.ResponseTimeAverage);

		} catch (MonitoringException | MonitoringNoDataStoredException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}

	}

	@Test
	public void testAddSuccessfullInvocationData() {

		long timeStart = System.currentTimeMillis();
		long timeDuration = 0;
		
		try {
			component.clearAllContentOfWebservice(testService1);
			component.addSuccessfulInvocationData(testService1, 1, 2, 3);
			timeDuration = System.currentTimeMillis() - timeStart;
			
		} catch (MonitoringException e) {
			fail(Arrays.toString(e.getStackTrace()));
		}
		
		LOGGER.debug("Add successfull invocation in " + timeDuration + " ms");
		//System.out.println("Add successfull invocation in " + timeDuration + " ms");
	}

}
