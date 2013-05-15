package at.sti2.msee.monitoring.core.repository;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringParameterStoreHandler;

//TODO: Write some meaningful tests
public class MonitoringParameterStoreHandlerTest {

	private MonitoringParameterStoreHandler handler;

	private String someService = "http://someurl.com/doesntmatter";
	private URL webservice;


	@Before
	public void setUp() {
		try {
			this.webservice = new URL(someService);
			this.handler = new MonitoringParameterStoreHandler();
			
		} catch (RepositoryException | IOException e) {
			fail();
		}
	}

	@Test
	public void testAddUnsuccessfulInvocation() {

		try {
			this.handler.addUnsuccessfulInvocation(webservice);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e) {
			fail();
		}

	}

	@Test
	public void testAddSuccessfulInvocation() {

		try {
			this.handler.addSuccessfulInvocation(webservice, 123, 456, 789);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e) {
			fail();
		}

	}

}
