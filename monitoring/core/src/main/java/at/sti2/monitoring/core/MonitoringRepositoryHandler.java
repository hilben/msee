package at.sti2.monitoring.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ontoware.aifbcommons.collection.ClosableIterable;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.repository.RepositoryException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.RDFWriter;

import at.sti2.monitoring.core.common.MonitoringConfig;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

public class MonitoringRepositoryHandler {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	private ServiceRepository serviceRepository = null;

	private MonitoringOntology ontology = null;
	private MonitoringConfig config = null;
	private String ns = null;

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

		this.config = MonitoringConfig.getConfig();
		ns = this.config.getInstancePrefix();

		repositoryConfig.setServerEndpoint(config.getTriplestoreEndpoint());
		repositoryConfig.setRepositoryID(config.getTriplestoreReposID());

		this.serviceRepository = ServiceRepositoryFactory
				.newInstance(repositoryConfig);
		this.serviceRepository.init();

		this.ontology = MonitoringOntology.getMonitoringOntology();
	}

	public void setMonitoredWebservice(URL url, boolean monitored)
			throws IOException {
		Model m = RDF2Go.getModelFactory().createModel();
		m.open();
		URI webserviceURI = m.createURI(url.toExternalForm());

		String rdf = this.ontology.getTriplesForWebService(url, monitored);

		m.readFrom(new ByteArrayInputStream(rdf.getBytes()));

		this.serviceRepository.insertModel(m, webserviceURI);

		m.close();
	}

	// public String[] getServiceCategories() throws IOException {
	//
	// String query = "somequery";
	//
	// Model rdfModel = serviceRepository.getModel();
	// rdfModel.open();
	//
	// ClosableIterable<QueryRow> resultTable = rdfModel.sparqlSelect(query);
	// ClosableIterator<QueryRow> results = resultTable.iterator();
	//
	// LOGGER.debug("Querying all categories via");
	// LOGGER.debug(query);
	//
	// ArrayList<String> categories = new ArrayList<String>();
	//
	// while (results.hasNext()) {
	// QueryRow nextElement = results.next();
	// String category = nextElement.getValue("category").toString();
	// categories.add(category);
	// LOGGER.debug("Category : " + category);
	// }
	//
	// // Cast to array
	// String returnArray[] = new String[categories.size()];
	// int i = 0;
	// for (String c : categories) {
	// returnArray[i] = c;
	// i++;
	// }
	//
	// return returnArray;
	// }

	public ServiceRepository getServiceRepository() {
		return serviceRepository;
	}
}
