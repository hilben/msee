package at.sti2.monitoring.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSType;


//TODO: Write some meaningful tests
public class MonitoringParameterStoreHandlerTest {

	private MonitoringParameterStoreHandler handler;

	private String someService = "http://someurl.com/doesntmatter";
	private URL webservice;

	@Before
	public void setUp() throws Exception {
		this.webservice = new URL(someService);
		MonitoringComponentImpl monitoringComponent = new MonitoringComponentImpl();
		monitoringComponent.clearAllContentOfWebservice(this.webservice);
	}

	@Test
	public void testMonitoringParameterStoreHandler() {
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);
		try {
			monitoringComponent = new MonitoringComponentImpl();
		} catch (RepositoryException | IOException e1) {
			fail();
		}

		try {
			this.handler = new MonitoringParameterStoreHandler(
					monitoringComponent);
		} catch (RepositoryException | IOException e) {
			fail();
		}
	}

	@Test
	public void testAddUnsuccessfulInvocation() {
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);
		try {
			monitoringComponent = new MonitoringComponentImpl();
		} catch (RepositoryException | IOException e1) {
			fail();
		}
		try {
			this.handler = new MonitoringParameterStoreHandler(
					monitoringComponent);
		} catch (RepositoryException | IOException e) {
			fail();
		}

		try {
			this.handler.addUnsuccessfulInvocation(webservice);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e) {
			fail();
		}
//
//		try {
//			verify(monitoringComponent, times(3)).getQoSParameter(webservice,
//					any(QoSType.class));
//		} catch (MonitoringException | MonitoringNoDataStoredException e) {
//			fail();
//		}

	}

	@Test
	public void testAddSuccessfulInvocation() {
		MonitoringComponentImpl monitoringComponent = mock(MonitoringComponentImpl.class);
		try {
			monitoringComponent = new MonitoringComponentImpl();
		} catch (RepositoryException | IOException e1) {
			fail();
		}

		try {
			this.handler = new MonitoringParameterStoreHandler(
					monitoringComponent);
		} catch (RepositoryException | IOException e) {
			fail();
		}

		try {
			this.handler.addSuccessfulInvocation(webservice, 123, 456, 789);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e) {
			fail();
		}

	}

}
