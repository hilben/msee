package at.sti2.monitoring.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

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

import at.sti2.monitoring.core.common.MonitoringConfig;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
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

		String query = MonitoringQueries.removeMonitoredWebserviceAndData(url);

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

		String query = MonitoringQueries.setInvocationInstanceState(webService,
				invocationInstanceID, invocationStateID, statename, time);

		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);
		m.close();
	}

	public void enableMonitoringForWebservice(URL url, boolean monitored)
			throws IOException, RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueries.insertMonitoredWebservice(url,
				monitored);

		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);

		m.close();
	}

	public boolean isMonitored(URL url) throws IOException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueries
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

	//TODO: refactor
	public MonitoringInvocationInstance getInvocationInstance(String UUID,
			MonitoringComponent component) throws IOException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = MonitoringQueries.getInvocationInstance(UUID);

		LOGGER.debug("QUERY: " + query);

		QueryResultTable t = m.sparqlSelect(query);

		LOGGER.debug("result vars: " + t.getVariables());

		ClosableIterator<QueryRow> res = t.iterator();

		String currentstate = null;
		String time = null;
		String webservice = null;

		String result = null;
		if (res.hasNext()) {
			QueryRow qr = res.next();

			currentstate = qr.getLiteralValue("statename");
			time = qr.getLiteralValue("time");
			webservice = qr.getValue("webservice").asURI().toString();

			System.out.println("READ OUT OF DB: " + currentstate + " " + time
					+ " " + webservice);
		}

		LOGGER.debug("results: " + result);

		m.close();

		MonitoringInvocationState s = MonitoringInvocationState
				.valueOf(currentstate);

		return new MonitoringInvocationInstanceImpl(new URL(webservice), component, s, UUID);
	}
}
