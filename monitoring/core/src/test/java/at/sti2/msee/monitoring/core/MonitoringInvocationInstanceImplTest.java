package at.sti2.msee.monitoring.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.monitoring.core.MonitoringInvocationInstanceImpl;

public class MonitoringInvocationInstanceImplTest {

	private MonitoringInvocationInstanceImpl invo;
	private URL webservice = null;
	private String webserviceURL = "http://someservice.com/doesntmatter";

	@Before
	public void setUp() {

		try {
			this.webservice = new URL(webserviceURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateInvocationState() {

		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		this.invo = new MonitoringInvocationInstanceImpl(webservice,
				monitoringComponent);

		try {
			this.invo.updateInvocationState(MonitoringInvocationState.None);

			this.invo.updateInvocationState(MonitoringInvocationState.Started);

			this.invo
					.updateInvocationState(MonitoringInvocationState.Terminated);

			this.invo.updateInvocationState(MonitoringInvocationState.Started);

			verify(monitoringComponent, times(4))
					.updateInvocationInstance(invo);

		} catch (MonitoringException e) {
			fail();
		}
	}

	@Test
	public void testSendSuccessfulInvocation() {
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		this.invo = new MonitoringInvocationInstanceImpl(webservice,
				monitoringComponent);

		try {
			this.invo.sendSuccessfulInvocation(123, 456, 789);

			verify(monitoringComponent).addSuccessfulInvocationData(
					this.webservice, 123, 456, 789);

			this.invo.sendSuccessfulInvocation(-987, -654, -321);

			verify(monitoringComponent).addSuccessfulInvocationData(
					this.webservice, -987, -654, -321);

		} catch (MonitoringException e) {
			fail();
		}
	}

	@Test
	public void testSendUnsuccessfulInvocation() {
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		this.invo = new MonitoringInvocationInstanceImpl(webservice,
				monitoringComponent);
		try {
			this.invo.sendUnsuccessfulInvocation();
			this.invo.sendUnsuccessfulInvocation();
			this.invo.sendUnsuccessfulInvocation();
			this.invo.sendUnsuccessfulInvocation();

			verify(monitoringComponent, times(4))
					.addUnsuccessfullInvocationData(this.webservice);

		} catch (MonitoringException e) {
			fail();
		}
	}

	@Test
	public void testGetWebService() {
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		this.invo = new MonitoringInvocationInstanceImpl(webservice,
				monitoringComponent);

		assertTrue(this.invo.getWebService().toExternalForm()
				.compareTo(this.webserviceURL) == 0);

	}

	@Test
	public void testGetState() {
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		this.invo = new MonitoringInvocationInstanceImpl(webservice,
				monitoringComponent);

		assertTrue(this.invo.getWebService().toExternalForm()
				.compareTo(this.webserviceURL) == 0);
	}

	@Test
	public void testSetState() {
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);

		this.invo = new MonitoringInvocationInstanceImpl(webservice,
				monitoringComponent);

		try {
			this.invo.setState(MonitoringInvocationState.Started);
		} catch (MonitoringException e) {
			fail();
		}

		assertEquals(this.invo.getState(), MonitoringInvocationState.Started);

		try {
			this.invo.setState(MonitoringInvocationState.Terminated);
		} catch (MonitoringException e) {
			fail();
		}

		assertEquals(this.invo.getState(), MonitoringInvocationState.Terminated);

		try {
			verify(monitoringComponent, times(2)).updateInvocationInstance(
					this.invo);
		} catch (MonitoringException e) {
			fail();
		}
	}

}
