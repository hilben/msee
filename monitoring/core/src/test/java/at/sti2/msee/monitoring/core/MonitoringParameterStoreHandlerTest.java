package at.sti2.msee.monitoring.core;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

//TODO: Write some meaningful tests ?
public class MonitoringParameterStoreHandlerTest {


	private String someService = "http://someurl.com/doesntmatter";
	private URL webservice;


	@Before
	public void setUp() {
			try {
				this.webservice = new URL(someService);
			} catch (MalformedURLException e) {
				fail();
			}
	}

	@Test
	public void testAddUnsuccessfulInvocation() {

		try {
			MonitoringParameterStoreHandler.addUnsuccessfulInvocation(webservice);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e) {
			fail();
		}

	}

	@Test
	public void testAddSuccessfulInvocation() {

		try {
			MonitoringParameterStoreHandler.addSuccessfulInvocation(webservice, 123, 456, 789);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e) {
			fail();
		}
	}
	
	@Test
	public void testUpdateMonitoredTime() {
		try {
			MonitoringParameterStoreHandler.updateMonitoredTime(webservice, MonitoringWebserviceAvailabilityState.Available, 123);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e) {
			fail();
		}
	}

}
