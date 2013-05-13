package at.sti2.msee.monitoring.core.repository;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailability;
import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.core.repository.MonitoringQueryBuilder;
import at.sti2.msee.monitoring.core.repository.MonitoringRepositoryHandler;

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
	public void testEnableMonitoringForWebservice() throws IOException,
			RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		repositoryHandler.enableMonitoringForWebservice(wsURL1, true);
		repositoryHandler.enableMonitoringForWebservice(wsURL2, true);
		repositoryHandler.enableMonitoringForWebservice(wsURL3, true);

	}

	@Test
	public void testIsMonitoredWebservice() throws IOException,
			RepositoryException, MalformedQueryException,
			UpdateExecutionException {
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
	public void testGetMonitoringRepositoryHandler() {
		assertNotNull(repositoryHandler.getServiceRepository());
	}

	@Test
	public void testAddAndGetCurrentQoSParameter() {
		Date now = new Date();
		QoSParameter send = new QoSParameter(QoSType.UnavailableTime, "123",
				now);
		try {

			this.repositoryHandler.addQoSParameter(wsURL1, UUID.randomUUID()
					.toString(), send);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e) {
			fail();
		}
		QoSParameter p = null;
		try {
			p = this.repositoryHandler.getCurrentQoSParameter(wsURL1,
					QoSType.UnavailableTime);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | ParseException
				| MonitoringNoDataStoredException e) {
			fail();
		}

		assertTrue(p.getTime().compareTo(send.getTime()) == 0);
		assertTrue(p.getType() == send.getType());
		assertTrue(p.getValue().compareTo(send.getValue()) == 0);
	}

	@Test
	public void testUpdateAvailabilityState() {
		Date now = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		String time = simpleDateFormat.format(now);

		try {
			this.repositoryHandler.updateAvailabilityState(wsURL1,
					MonitoringWebserviceAvailabilityState.Available, time);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e) {
			fail();
		}

		MonitoringWebserviceAvailability avail;
		try {
			avail = repositoryHandler.getAvailability(wsURL1);

			assertTrue(avail.getState() == MonitoringWebserviceAvailabilityState.Available);
			assertTrue(avail.getTime().compareTo(time) == 0);

		} catch (IOException | MonitoringNoDataStoredException | ParseException e) {
			fail();
		}
	}

	@Test
	public void testGetAllQoSParameterInTimeframe() {
		try {
			this.repositoryHandler.clearAllContentForWebservice(wsURL1);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e1) {
			fail();
		}

		ArrayList<QoSParameter> qosparams = new ArrayList<QoSParameter>();
		ArrayList<String> times = new ArrayList<String>();

		for (int i = 0; i < 10; i++) {
			Date now = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ssZ");
			String time = simpleDateFormat.format(now);

			times.add(time);

			QoSParameter p = null;
			try {
				p = new QoSParameter(QoSType.PayloadSizeResponseMinimum,
						String.valueOf(3 * i), time);
			} catch (ParseException e) {
				fail();
			}
			qosparams.add(p);

			try {
				this.repositoryHandler.addQoSParameter(wsURL1, UUID
						.randomUUID().toString(), p);
			} catch (RepositoryException | MalformedQueryException
					| UpdateExecutionException | IOException e) {
				fail();
			}
		}

		// TODO: Write test with several different timeframe delimiters
		ArrayList<QoSParameter> returnparams = null;

		try {
			returnparams = this.repositoryHandler
					.getAllQoSParameterInTimeframe(wsURL1,
							QoSType.PayloadSizeResponseMinimum);
		} catch (MonitoringNoDataStoredException | ParseException | IOException e) {
			fail();
		}

		assertNotNull(returnparams);

		for (int i = 0; i < 10; i++) {
			QoSParameter c = returnparams.get(9 - i);
			assertTrue(c.getTime().compareTo(times.get(i)) == 0);
			assertTrue(c.getType().compareTo(qosparams.get(i).getType()) == 0);
			assertTrue(c.getValue().compareTo(qosparams.get(i).getValue()) == 0);
		}

	}

	@Test
	public void testGetAllAvailabilityStatesInTimeframe() throws IOException,
			ParseException {
		try {
			this.repositoryHandler.clearAllContentForWebservice(wsURL1);
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException e1) {
			fail();
		}

		ArrayList<MonitoringWebserviceAvailability> availabilities = new ArrayList<MonitoringWebserviceAvailability>();
		ArrayList<String> times = new ArrayList<String>();

		for (int i = 0; i < 10; i++) {
			Date now = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ssZ");
			String time = simpleDateFormat.format(now);

			times.add(time);

			MonitoringWebserviceAvailability a = null;
			if (i != 5) {
				a = new MonitoringWebserviceAvailability(
						MonitoringWebserviceAvailabilityState.Available, now);
			} else {
				a = new MonitoringWebserviceAvailability(
						MonitoringWebserviceAvailabilityState.Unavailable, now);
			}

			availabilities.add(a);

			try {
				this.repositoryHandler.updateAvailabilityState(wsURL1,
						a.getState(), time);
			} catch (RepositoryException | MalformedQueryException
					| UpdateExecutionException | IOException e) {
				fail();
			}
		}

		// TODO: Write test with several different timeframe delimiters
		ArrayList<MonitoringWebserviceAvailability> returnparams = null;

		try {
			returnparams = this.repositoryHandler
					.getAllAvailabilityStatesInTimeframe(wsURL1);
		} catch (MonitoringNoDataStoredException e) {
			fail();
		}

		assertNotNull(returnparams);

		for (int i = 0; i < 10; i++) {

			MonitoringWebserviceAvailability c = returnparams.get(9 - i);
			assertTrue(c.getTime().compareTo(times.get(i)) == 0);
			assertTrue(c.getState().compareTo(availabilities.get(i).getState()) == 0);
		}
	}

	@Test
	@Ignore
	public void testGetAllInvocationsInstancesInTimeframe() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testUpdateInvocationInstanceState() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testGetInvocationInstance() {
		fail("Not yet implemented"); // TODO
	}

}
