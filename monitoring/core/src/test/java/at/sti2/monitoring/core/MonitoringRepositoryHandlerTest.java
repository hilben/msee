package at.sti2.monitoring.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

public class MonitoringRepositoryHandlerTest {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	MonitoringRepositoryHandler repositoryHandler;

	String ws1 = "http://www.example.com/examplews1";
	String ws2 = "http://www.example.com/examplews2";
	String ws3 = "http://www.example.com/examplews3";
	URL wsURL1 = null;
	URL wsURL2 = null;
	URL wsURL3 = null;

	@Before
	public void setUp() throws Exception {
		repositoryHandler = MonitoringRepositoryHandler
				.getMonitoringRepositoryHandler();
		wsURL1 = new URL(ws1);
		wsURL2 = new URL(ws2);
		wsURL3 = new URL(ws3);
	}
	

	@Test
	public void testSetMonitoredWebservice() throws IOException,
			RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		repositoryHandler.enableMonitoringForWebservice(wsURL1, true);
		repositoryHandler.enableMonitoringForWebservice(wsURL2, true);
		repositoryHandler.enableMonitoringForWebservice(wsURL3, true);
	}

	@Test
	public void testIsMonitored() throws IOException, RepositoryException,
			MalformedQueryException, UpdateExecutionException {
		repositoryHandler.enableMonitoringForWebservice(wsURL1, true);
		repositoryHandler.enableMonitoringForWebservice(wsURL2, true);
		repositoryHandler.enableMonitoringForWebservice(wsURL3, true);

		assertTrue(repositoryHandler.isMonitoredWebservice(wsURL1));
		assertTrue(repositoryHandler.isMonitoredWebservice(wsURL2));
		assertTrue(repositoryHandler.isMonitoredWebservice(wsURL3));

		repositoryHandler.enableMonitoringForWebservice(wsURL1, false);
		repositoryHandler.enableMonitoringForWebservice(wsURL2, false);
		repositoryHandler.enableMonitoringForWebservice(wsURL3, false);

		assertFalse(repositoryHandler.isMonitoredWebservice(wsURL1));
		assertFalse(repositoryHandler.isMonitoredWebservice(wsURL2));
		assertFalse(repositoryHandler.isMonitoredWebservice(wsURL3));

		repositoryHandler.enableMonitoringForWebservice(wsURL1, true);
		repositoryHandler.enableMonitoringForWebservice(wsURL2, true);
		repositoryHandler.enableMonitoringForWebservice(wsURL3, true);

		assertTrue(repositoryHandler.isMonitoredWebservice(wsURL1));
		assertTrue(repositoryHandler.isMonitoredWebservice(wsURL2));
		assertTrue(repositoryHandler.isMonitoredWebservice(wsURL3));
	}

	@Test
	public void testClearAllContentForWebservice() throws RepositoryException,
			MalformedQueryException, UpdateExecutionException, IOException {
		Model m1full = repositoryHandler.getServiceRepository().getModel();
		m1full.open();
		QueryResultTable t1full = m1full.sparqlSelect(MonitoringQueryBuilder
				.getAllTriplesOfContext(wsURL1));
		m1full.close();
		assertTrue(t1full.iterator().hasNext());

		Model m2full = repositoryHandler.getServiceRepository().getModel();
		m2full.open();
		QueryResultTable t2full = m2full.sparqlSelect(MonitoringQueryBuilder
				.getAllTriplesOfContext(wsURL2));
		m2full.close();
		assertTrue(t2full.iterator().hasNext());

		Model m3full = repositoryHandler.getServiceRepository().getModel();
		m3full.open();
		QueryResultTable t3full = m3full.sparqlSelect(MonitoringQueryBuilder
				.getAllTriplesOfContext(wsURL3));
		m3full.close();
		assertTrue(t3full.iterator().hasNext());

		ClosableIterator<QueryRow> it = t3full.iterator();

		while (it.hasNext()) {
			QueryRow current = it.next();
			for (String s : t3full.getVariables()) {
				LOGGER.debug(current.getValue(s));
				assertNotNull(s);
			}

		}

		repositoryHandler.clearAllContentForWebservice(wsURL1);
		repositoryHandler.clearAllContentForWebservice(wsURL2);
		repositoryHandler.clearAllContentForWebservice(wsURL3);

		Model m1 = repositoryHandler.getServiceRepository().getModel();
		m1.open();
		QueryResultTable t1 = m1.sparqlSelect(MonitoringQueryBuilder
				.getAllTriplesOfContext(wsURL1));
		m1.close();
		assertFalse(t1.iterator().hasNext());

		Model m2 = repositoryHandler.getServiceRepository().getModel();
		m2.open();
		QueryResultTable t2 = m2.sparqlSelect(MonitoringQueryBuilder
				.getAllTriplesOfContext(wsURL2));
		m2.close();
		assertFalse(t2.iterator().hasNext());

		Model m3 = repositoryHandler.getServiceRepository().getModel();
		m3.open();
		QueryResultTable t3 = m3.sparqlSelect(MonitoringQueryBuilder
				.getAllTriplesOfContext(wsURL3));
		m3.close();
		assertFalse(t3.iterator().hasNext());
	}

	@Test
	public void testGetServiceRepository() {
		assertNotNull(repositoryHandler.getServiceRepository());
	}

}
