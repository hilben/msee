package at.sti2.msee.ranking.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import at.sti2.msee.monitoring.core.repository.MonitoringQueryBuilder;
import at.sti2.msee.ranking.api.exception.RankingException;
import at.sti2.msee.ranking.common.RankingConfig;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;
import at.sti2.msee.triplestore.impl.SesameServiceRepositoryImpl;

public class RankingRepositoryHandler {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	private SesameServiceRepositoryImpl serviceRepository = null;

	private RankingOntology ontology = null;
	private RankingConfig config = null;
	private static RankingRepositoryHandler handler = null;

	public static RankingRepositoryHandler getInstance()
			throws RepositoryException, IOException {
		if (handler == null) {
			handler = new RankingRepositoryHandler();
		}

		return handler;
	}

	private RankingRepositoryHandler() throws IOException, RepositoryException {
		ServiceRepositoryConfiguration repositoryConfig = new ServiceRepositoryConfiguration();

		this.ontology = RankingOntology.getRankingOntology();

		this.config = RankingConfig.getConfig();
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

	public void setRulesForWebservice(URL webservice, String rules)
			throws RankingException, IOException, RepositoryException, MalformedQueryException, UpdateExecutionException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = null;

		query = RankingQueryBuilder.insertRankedWebserviceAndRulesQuery(
				webservice, rules);
		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);
		m.close();
	}

	public String getRulesForWebservice(URL webservice)
			throws RankingException, IOException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query;

		query = RankingQueryBuilder.getWebServiceRuleQuery(webservice);

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
		return result;
	}

	public void clearAllContentForWebservice(URL url) throws IOException,
			RepositoryException, MalformedQueryException,
			UpdateExecutionException {
		Model m = this.serviceRepository.getModel();
		m.open();

		String query = RankingQueryBuilder.removeRankedWebserviceAndData(url);

		this.serviceRepository.performSPARQLUpdate(query);

		LOGGER.debug("QUERY: " + query);

		m.close();
	}

	public SesameServiceRepositoryImpl getServiceRepository() {

		return (SesameServiceRepositoryImpl) serviceRepository;
	}

	@Override
	public String toString() {
		return "MonitoringRepositoryHandler [LOGGER=" + LOGGER
				+ ", serviceRepository=" + serviceRepository + ", ontology="
				+ ontology + ", config=" + config + "]";
	}
}
