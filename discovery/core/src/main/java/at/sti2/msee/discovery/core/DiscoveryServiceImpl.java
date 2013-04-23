package at.sti2.msee.discovery.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ontoware.aifbcommons.collection.ClosableIterable;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Statement;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilder;
import at.sti2.msee.triplestore.ServiceRepository;

/**
 * This class represents the concrete implementation of the {@link Discovery}
 * interface. It provides methods for retrieving registered services.
 * 
 */
public class DiscoveryServiceImpl implements Discovery {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());
	private ServiceRepository serviceRepository = null;
	private DiscoveryQueryBuilder discoveryQueryBuilder = null;

	public DiscoveryServiceImpl(ServiceRepository serviceRepository) {
		if (serviceRepository == null) {
			throw new IllegalArgumentException("Service repository cannot be null");
		}
		this.serviceRepository = serviceRepository;
		try {
			this.serviceRepository.init();
		} catch (RepositoryException e) {
			LOGGER.catching(e);
		}
		discoveryQueryBuilder = new DiscoveryQueryBuilder();
	}

	/**
	 * This method discovers all services that belong to a category of the
	 * argument {@literal categoryList}.
	 * 
	 * @see at.sti2.msee.discovery.api.webservice.Discovery#discover(java.lang.String[])
	 */
	@Override
	public String discover(String[] categoryList) throws DiscoveryException {
		LOGGER.debug("Starting discover()");

		checkCategoryList(categoryList);

		String query = discoveryQueryBuilder
				.getDiscoverQuery2Args(categoryList);

		Model rdfModel = serviceRepository.getModel();
		rdfModel.open();

		ClosableIterable<Statement> resultTable = rdfModel.sparqlConstruct(query);
		ClosableIterator<Statement> results = resultTable.iterator();

		String rdfxml = convertQueryResult2RDFXML(results);
		rdfModel.close();
		return rdfxml;
	}
	
	/**
	 * This method discovers all services that belong to a category of the
	 * argument {@literal categoryList}. It returns the services grouped by the
	 * category. The key of the returned map is the category and the value
	 * points out the services as String.
	 * 
	 */
	public Map<String, String>  discoverMap(String[] categoryList) throws DiscoveryException {
		Map<String, String> categoryMap = new HashMap<String, String>();
		for(String category : categoryList){
			categoryMap.put(category, discover(new String[]{category}));
		}
		return categoryMap;
	}

	/**
	 * This method converts the RDF statements called through an interator into
	 * rdf/xml format.
	 * 
	 * @param results
	 *            - Iterator of Statements
	 * @return the statements in rdf/xml format
	 */
	private String convertQueryResult2RDFXML(ClosableIterator<Statement> results) {
		OutputStream out = new ByteArrayOutputStream();
		RDFHandler rdfxmlWriter = new RDFXMLWriter(out);

		try {
			rdfxmlWriter.startRDF();
			while (results.hasNext()) {
				Statement statement = results.next();
				Resource s = new BNodeImpl(statement.getSubject().toString());
				URI p = new URIImpl(statement.getPredicate().toString());
				Resource o = new BNodeImpl(statement.getObject().toString());
				rdfxmlWriter.handleStatement(new StatementImpl(s, p, o));
			}
			rdfxmlWriter.endRDF();
		} catch (RDFHandlerException e) {
			try {
				throw new DiscoveryException("Converting had some error");
			} catch (DiscoveryException e1) {
			}
		}
		return out.toString();
	}

	/**
	 * Checks the category list to contain values.
	 * 
	 * @param categoryList
	 */
	private void checkCategoryList(String[] categoryList) {
		if (categoryList == null) {
			LOGGER.error("Category list is null");
			throw new NullPointerException("Category list is null");
		}
		if (categoryList.length < 1) {
			LOGGER.error("Category list is empty");
			throw new IllegalArgumentException("Category list is empty");
		}
	}

	@Override
	public String discoverAdvanced(String[] categoryList,
			String[] inputParamList, String[] outputParamList)
			throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

	@Override
	public String lookup(String namespace, String operationName)
			throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

	@Override
	public String getIServeModel(String serviceID) throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String[] getServiceCategories() throws IOException {
		String query = discoveryQueryBuilder.getAllCategoriesQuery();

		Model rdfModel = serviceRepository.getModel();
		rdfModel.open();

		ClosableIterable<QueryRow> resultTable = rdfModel.sparqlSelect(query);
		ClosableIterator<QueryRow> results = resultTable.iterator();

		LOGGER.debug("Querying all categories via query: " + query);

		ArrayList<String> categories = new ArrayList<String>();
		while (results.hasNext()) {
			categories.add(getValueOfQueryRow(results.next(), "category"));
		}
		LOGGER.debug("Categories: " + Arrays.toString(categories.toArray()));

		// Cast to array
		String returnArray[] = categories.toArray(new String[categories.size()]);

		return returnArray;
	}

	/**
	 * Returns the value of a QueryRow in the given column
	 */
	private String getValueOfQueryRow(QueryRow row, String column) {
		return row.getValue(column).toString();
	}

	/**
	 * Returns the service repository.
	 * 
	 * @return service repository
	 */
	public ServiceRepository getServiceRepository() {
		return serviceRepository;
	}

}
