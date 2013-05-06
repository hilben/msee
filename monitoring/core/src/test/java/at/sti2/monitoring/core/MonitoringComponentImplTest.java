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
	public void setUp() throws Exception {
		component = new MonitoringComponentImpl();
		testService1 = new URL(testServiceURL1);
		testService2 = new URL(testServiceURL2);

		assertNotNull(component);
		assertNotNull(testService1);
		assertNotNull(testService2);

		component.clearAllContentOfWebservice(testService1);
		component.clearAllContentOfWebservice(testService2);
	}

	@Test
	public void testIsMonitoredWebService() {
		try {
			component.enableMonitoring(testService1);
			assertTrue(component.isMonitoredWebService(testService1));
		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testQueryParametersOnEmptyContext() {
		boolean catchNoDataException = false;
		try {
			component.clearAllContentOfWebservice(testService2);

			component.getQoSParameter(testService2, QoSType.RequestTotal);

		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
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

		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testDisableMonitoring() {
		try {
			component.disableMonitoring(testService1);
			assertFalse(component.isMonitoredWebService(testService1));
		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testUpdateAvailabilityState() {
		MonitoringWebserviceAvailabilityState state1 = MonitoringWebserviceAvailabilityState.NotChecked;
		MonitoringWebserviceAvailabilityState state2 = MonitoringWebserviceAvailabilityState.NotChecked;
		try {
			component.updateAvailabilityState(testService1, state1);
			component.updateAvailabilityState(testService1, state2);
		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGetAvailability() {
		MonitoringWebserviceAvailabilityState state1 = MonitoringWebserviceAvailabilityState.NotChecked;
		MonitoringWebserviceAvailabilityState state2 = MonitoringWebserviceAvailabilityState.Unavailable;
		MonitoringWebserviceAvailabilityState state3 = MonitoringWebserviceAvailabilityState.Available;

		try {
			component.updateAvailabilityState(testService1, state1);
			component.updateAvailabilityState(testService1, state2);
			component.updateAvailabilityState(testService1, state3);

			component.updateAvailabilityState(testService2, state2);

			assertTrue(component.getAvailability(testService1).getState() == state3);
			assertTrue(component.getAvailability(testService2).getState() == state2);

			assertFalse(component.getAvailability(testService2).getState() == state1);

		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testClearAllContentOfWebservice() {
		try {
			component.clearAllContentOfWebservice(testService1);
			assertFalse(component.isMonitoredWebService(testService1));
		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testAddInvocationData() {
		try {
			component.addSuccessfulInvocationData(testService1, -100, 100, 100);
			component.addSuccessfulInvocationData(testService1, 50, 100, 100);
			component.addSuccessfulInvocationData(testService1, 33, 100, 100);
			component.addSuccessfulInvocationData(testService1, -9.7, 100, 100);

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
			e.printStackTrace();
			fail();
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
			fail();
			e.printStackTrace();
		}

	}

	@Test
	public void testAddSuccessfullInvocationData() {

		try {
			component.clearAllContentOfWebservice(testService1);
			component.addSuccessfulInvocationData(testService1, 1, 2, 3);
		} catch (MonitoringException e) {
			e.printStackTrace();
			fail();
		}
	}

}
