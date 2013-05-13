package at.sti2.msee.monitoring.core.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailability;
import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.core.MonitoringInvocationInstanceImpl;
import at.sti2.msee.monitoring.core.common.MonitoringConfig;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;
import at.sti2.msee.triplestore.impl.SesameServiceRepositoryImpl;

public class MonitoringRepositoryHandler {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	private SesameServiceRepositoryImpl serviceRepository = null;

	private MonitoringOntology ontology = null;
	private MonitoringConfig config = null;
	private static MonitoringRepositoryHandler handler = null;

	public static MonitoringRepositoryHandler getMonitoringRepositoryHandler()
			throws RepositoryException, IOException {
		if (handler == null) {
			handler = new MonitoringRepositoryHandler();
		}

		return handler;
	}

	private MonitoringRepositoryHandler() throws IOException,
			RepositoryException {
		ServiceRepositoryConfiguration repositoryConfig = new ServiceRepositoryConfiguration();

		this.ontology = MonitoringOntology.getMonitoringOntology();

		this.config = MonitoringConfig.getConfig();
		this.config.getInstancePrefix();

		repositoryConfig.setServerEndpoint(config.getTriplestoreEndpoint());
		repositoryConfig.setRepositoryID(config.getTriplestoreReposID());

		ServiceRepository sr = ServiceRepositoryFactory
				.newInstance(repositoryConfig);
		if (sr instanceof SesameServiceRepositoryImpl) {
			this.serviceRepository = (SesameServiceRepositoryImpl) sr;
		} else {
			throw new RepositoryException(
					"Monitoring component only supports SesameServiceRepositoryImpl");
		}

		this.serviceRepository.init();

		this.init();
	}

	private void init() throws IOException {
		Model m = RDF2Go.getModelFactory().createModel();
		m.open();

		URI ns = m.createURI(this.ontology.NS);
		String rdf = this.ontology.getOntologyAsRDFXML();

		m.readFrom(new ByteArrayInputStream(rdf.getBytes()));

		this.serviceRepository.insertModel(m, ns);

		m.close();
	}

	public void clearAllContentForWebservice(URL url) throws IOException,
			RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueryBuilder
				.removeMonitoredWebserviceAndData(url);

		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);

		m.close();
	}

	public void updateInvocationInstanceState(URL webService,
			String invocationInstanceID, String invocationStateID,
			String statename, String time) throws IOException,
			RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueryBuilder.setInvocationInstanceState(
				webService, invocationInstanceID, invocationStateID, statename,
				time);

		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);
		m.close();
	}

	public void enableMonitoringForWebservice(URL url, boolean monitored)
			throws IOException, RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueryBuilder.insertMonitoredWebservice(url,
				monitored);

		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);

		m.close();
	}

	public boolean isMonitoredWebservice(URL url) throws IOException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueryBuilder
				.getIsWebServiceMonitoredSPARQLQuery(url);

		LOGGER.debug("QUERY: " + query);

		QueryResultTable t = m.sparqlSelect(query);

		LOGGER.debug("result vars: " + t.getVariables());

		ClosableIterator<QueryRow> res = t.iterator();

		String result = null;
		if (res.hasNext()) {
			result = res.next().getLiteralValue(t.getVariables().get(0));
		}

		LOGGER.debug("results: " + result);

		m.close();
		return Boolean.parseBoolean(result);
	}

	public SesameServiceRepositoryImpl getServiceRepository() {

		return (SesameServiceRepositoryImpl) serviceRepository;
	}

	public MonitoringInvocationInstance getInvocationInstance(String UUID,
			MonitoringComponent component) throws IOException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueryBuilder.getInvocationInstance(UUID);

		LOGGER.debug("QUERY: " + query);

		QueryResultTable t = m.sparqlSelect(query);

		LOGGER.debug("result vars: " + t.getVariables());

		ClosableIterator<QueryRow> res = t.iterator();

		String currentstate = null;
		String webservice = null;

		String result = null;
		if (res.hasNext()) {
			QueryRow qr = res.next();
			currentstate = qr.getLiteralValue("statename");
			webservice = qr.getValue("webservice").asURI().toString();

		}

		LOGGER.debug("results: " + result);

		m.close();

		MonitoringInvocationState s = MonitoringInvocationState
				.valueOf(currentstate);

		return new MonitoringInvocationInstanceImpl(new URL(webservice),
				component, s, UUID);
	}

	public void addQoSParameter(URL url, String UUID, QoSParameter qosparam)
			throws IOException, RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueryBuilder.addQoSParam(url, UUID, qosparam);

		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);

		m.close();
	}

	public QoSParameter getCurrentQoSParameter(URL url, QoSType qosparamtype)
			throws IOException, RepositoryException, MalformedQueryException,
			UpdateExecutionException, ParseException,
			MonitoringNoDataStoredException {
		Model m = this.serviceRepository.getModel();
		m.open();

		QoSParameter returnParameter = null;

		String query = MonitoringQueryBuilder.getCurrentQoSParameter(url,
				qosparamtype);

		LOGGER.debug("QUERY: " + query);

		QueryResultTable t = m.sparqlSelect(query);

		LOGGER.debug("result vars: " + t.getVariables());

		ClosableIterator<QueryRow> res = t.iterator();

		String time = null;
		String value = null;

		if (res.hasNext()) {
			QueryRow qr = res.next();
			time = qr.getLiteralValue("time");
			value = qr.getLiteralValue("value").toString();
		}

		if (time == null || value == null) {
			m.close();
			throw new MonitoringNoDataStoredException(qosparamtype
					+ " does not exist for " + url);
		}

		returnParameter = new QoSParameter(qosparamtype, value, time);

		LOGGER.debug("QUERY: " + query);

		m.close();

		return returnParameter;
	}

	public void updateAvailabilityState(URL webService,
			MonitoringWebserviceAvailabilityState state, String time)
			throws IOException, RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String id = UUID.randomUUID().toString();
		String query = MonitoringQueryBuilder.updateAvailabilityState(
				webService, id, time, state.toString());

		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);
		m.close();
	}

	public ArrayList<QoSParameter> getAllQoSParameterInTimeframe(
			URL webService, QoSType type)
			throws MonitoringNoDataStoredException, ParseException, IOException {
		return this.getAllQoSParameterInTimeframe(webService, type, null, null);
	}

	public ArrayList<QoSParameter> getAllQoSParameterInTimeframe(
			URL webService, QoSType type, String begin, String end)
			throws MonitoringNoDataStoredException, ParseException, IOException {
		Model m = this.serviceRepository.getModel();
		m.open();

		ArrayList<QoSParameter> returnParameter = new ArrayList<QoSParameter>();

		String query = MonitoringQueryBuilder.getAllQoSParametersInTimeframe(
				webService, type, begin, end);

		LOGGER.debug("QUERY: " + query);
		QueryResultTable t = m.sparqlSelect(query);

		LOGGER.debug("result vars: " + t.getVariables());

		ClosableIterator<QueryRow> res = t.iterator();

		String time = null;
		String value = null;

		while (res.hasNext()) {
			QueryRow qr = res.next();
			time = qr.getLiteralValue("time");
			value = qr.getLiteralValue("value").toString();
			QoSParameter p = new QoSParameter(type, value, time);
			returnParameter.add(p);
		}

		LOGGER.debug("QUERY: " + query);

		m.close();

		return returnParameter;
	}

	public ArrayList<MonitoringWebserviceAvailability> getAllAvailabilityStatesInTimeframe(
			URL webService)
			throws MonitoringNoDataStoredException, IOException, ParseException {
		return this.getAllAvailabilityStatesInTimeframe(webService, null,
				null);
	}

	public ArrayList<MonitoringWebserviceAvailability> getAllAvailabilityStatesInTimeframe(
			URL webService, String begin, String end)
			throws MonitoringNoDataStoredException, IOException, ParseException {
		Model m = this.serviceRepository.getModel();
		m.open();

		ArrayList<MonitoringWebserviceAvailability> returnParameter = new ArrayList<MonitoringWebserviceAvailability>();

		String query = MonitoringQueryBuilder.getAllAvailabilityStates(
				webService, begin, end);

		LOGGER.debug("QUERY: " + query);

		QueryResultTable t = m.sparqlSelect(query);

		LOGGER.debug("result vars: " + t.getVariables());

		ClosableIterator<QueryRow> res = t.iterator();

		String time = null;
		String state = null;

		while (res.hasNext()) {
			QueryRow qr = res.next();
			time = qr.getLiteralValue("time");
			state = qr.getLiteralValue("state").toString();
			MonitoringWebserviceAvailability p = new MonitoringWebserviceAvailability(
					state, time);
			returnParameter.add(p);
		}

		LOGGER.debug("QUERY: " + query);

		m.close();

		return returnParameter;
	}

	public ArrayList<MonitoringInvocationInstance> getAllInvocationsInstancesInTimeframe(
			URL webService, QoSType type)
			throws MonitoringNoDataStoredException {
		return this.getAllInvocationsInstancesInTimeframe(webService, type);
	}

	public ArrayList<MonitoringInvocationInstance> getAllInvocationsInstancesInTimeframe(
			URL webService, QoSType type, String begin, String end)
			throws MonitoringNoDataStoredException {
		throw new MonitoringNoDataStoredException("Not implemendet!");
	}

	public MonitoringWebserviceAvailability getAvailability(URL webService)
			throws IOException, MonitoringNoDataStoredException, ParseException {
		Model m = this.serviceRepository.getModel();
		m.open();

		MonitoringWebserviceAvailability monitoringAvailability = null;

		String query = MonitoringQueryBuilder
				.getCurrentAvailabilityState(webService);

		LOGGER.debug("QUERY: " + query);

		QueryResultTable t = m.sparqlSelect(query);

		LOGGER.debug("result vars: " + t.getVariables());

		ClosableIterator<QueryRow> res = t.iterator();

		String time = null;
		String state = null;

		if (res.hasNext()) {
			QueryRow qr = res.next();
			time = qr.getLiteralValue("time");
			state = qr.getLiteralValue("state").toString();
		}

		if (time == null || state == null) {
			m.close();
			throw new MonitoringNoDataStoredException(
					"availability state does not exist for " + webService);
		}

		monitoringAvailability = new MonitoringWebserviceAvailability(state,
				time);

		LOGGER.debug("QUERY: " + query);

		m.close();

		return monitoringAvailability;
	}

	@Override
	public String toString() {
		return "MonitoringRepositoryHandler [LOGGER=" + LOGGER
				+ ", serviceRepository=" + serviceRepository + ", ontology="
				+ ontology + ", config=" + config + "]";
	}
}
