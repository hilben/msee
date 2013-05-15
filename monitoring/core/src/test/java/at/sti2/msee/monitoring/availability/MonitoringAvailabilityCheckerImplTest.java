package at.sti2.msee.monitoring.availability;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.monitoring.core.availability.MonitoringAvailabilityCheckerHandlerImpl;
import at.sti2.msee.monitoring.core.availability.MonitoringAvailabilityCheckerImpl;

public class MonitoringAvailabilityCheckerImplTest {

	private String notOnline = "http://www.a.aaa/a";
	private String isOnline = "http://msee.sti2.at/discovery-webservice/service?wsdl";
	private URL notOnlineURL;
	private URL isOnlineURL;

	@Before
	public void setUp() throws Exception {
		this.isOnlineURL = new URL(isOnline);
		this.notOnlineURL = new URL(notOnline);

	}

	@Test
	public void testMonitoringAvailabilityCheckerImpl() {

		MonitoringAvailabilityCheckerHandlerImpl mockedHandler = mock(MonitoringAvailabilityCheckerHandlerImpl.class);
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		when(mockedHandler.isCheckingEndpoint(isOnlineURL)).thenReturn(true);
		when(mockedHandler.isCheckingEndpoint(notOnlineURL)).thenReturn(true);

		try {
			MonitoringAvailabilityCheckerImpl checker = new MonitoringAvailabilityCheckerImpl(
					isOnlineURL, mockedHandler, monitoringComponent);
			checker.toString();
		} catch (MonitoringException e) {
			fail();
		}

		try {
			verify(monitoringComponent).updateAvailabilityState(isOnlineURL,
					MonitoringWebserviceAvailabilityState.NotChecked,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);
		} catch (MonitoringException e) {
			fail();
		}

	}

	@Test
	public void testRunOnline() {
		MonitoringAvailabilityCheckerHandlerImpl mockedHandler = mock(MonitoringAvailabilityCheckerHandlerImpl.class);
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		when(mockedHandler.isCheckingEndpoint(isOnlineURL)).thenReturn(true);
		when(mockedHandler.isCheckingEndpoint(notOnlineURL)).thenReturn(true);

		MonitoringAvailabilityCheckerImpl checker = null;
		try {
			checker = new MonitoringAvailabilityCheckerImpl(isOnlineURL,
					mockedHandler, monitoringComponent);
		} catch (MonitoringException e) {
			fail();
		}

		MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS = 5;

		Thread m = new Thread(checker);

		m.start();

		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			fail();
		}

		try {
			verify(monitoringComponent).updateAvailabilityState(isOnlineURL,
					MonitoringWebserviceAvailabilityState.NotChecked,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);

			verify(monitoringComponent).updateAvailabilityState(isOnlineURL,
					MonitoringWebserviceAvailabilityState.Available,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);

			verify(monitoringComponent, never()).updateAvailabilityState(
					isOnlineURL,
					MonitoringWebserviceAvailabilityState.Unavailable,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);
		} catch (MonitoringException e) {
			fail();
		}
		
		reset(mockedHandler);
		assertTrue(m.isAlive());
		when(mockedHandler.isCheckingEndpoint(isOnlineURL)).thenReturn(false);

		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			fail();
		}

		try {
			verify(monitoringComponent, atMost(2)).updateAvailabilityState(
					isOnlineURL,
					MonitoringWebserviceAvailabilityState.NotChecked,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);
			verify(monitoringComponent, never()).updateAvailabilityState(
					isOnlineURL,
					MonitoringWebserviceAvailabilityState.Unavailable,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);

		} catch (MonitoringException e) {
			fail();
		}

		if (m.getState() != Thread.State.TERMINATED) {
			fail();
		}

	}

	
	//TODO: REWRITE THE TEST AND REWRITE THE CHECKER!
	@Ignore
	@Test
	public void testRunOffline() {
		MonitoringAvailabilityCheckerHandlerImpl mockedHandler = mock(MonitoringAvailabilityCheckerHandlerImpl.class);
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		when(mockedHandler.isCheckingEndpoint(isOnlineURL)).thenReturn(true);
		when(mockedHandler.isCheckingEndpoint(notOnlineURL)).thenReturn(true);

		MonitoringAvailabilityCheckerImpl checker = null;
		try {
			checker = new MonitoringAvailabilityCheckerImpl(notOnlineURL,
					mockedHandler, monitoringComponent);
		} catch (MonitoringException e) {
			fail();
		}

		MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS = 5;

		Thread m = new Thread(checker);

		m.start();

		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			fail();
		}

		try {
			verify(monitoringComponent).updateAvailabilityState(notOnlineURL,
					MonitoringWebserviceAvailabilityState.NotChecked,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);

			verify(monitoringComponent, never()).updateAvailabilityState(
					notOnlineURL,
					MonitoringWebserviceAvailabilityState.Available,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);

			verify(monitoringComponent).updateAvailabilityState(notOnlineURL,
					MonitoringWebserviceAvailabilityState.Unavailable,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);
		} catch (MonitoringException e) {
			fail();
		}
		reset(mockedHandler);
		assertTrue(m.isAlive());
		when(mockedHandler.isCheckingEndpoint(isOnlineURL)).thenReturn(false);

		try {
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			fail();
		}

		try {
			verify(monitoringComponent, atMost(2)).updateAvailabilityState(
					isOnlineURL,
					MonitoringWebserviceAvailabilityState.NotChecked,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);
			verify(monitoringComponent, never()).updateAvailabilityState(
					isOnlineURL,
					MonitoringWebserviceAvailabilityState.Available,MonitoringAvailabilityCheckerImpl.INTERVALL_SECONDS);

		} catch (MonitoringException e) {
			fail();
		}

		if (m.getState() != Thread.State.TERMINATED) {
			fail();
		}
	}

}
