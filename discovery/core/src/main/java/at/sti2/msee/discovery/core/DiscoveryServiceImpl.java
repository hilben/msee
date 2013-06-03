package at.sti2.msee.discovery.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilder;
import at.sti2.msee.discovery.core.tree.DiscoveredCategory;
import at.sti2.msee.discovery.core.tree.DiscoveredCategoryImpl;
import at.sti2.msee.discovery.core.tree.DiscoveredOperation;
import at.sti2.msee.discovery.core.tree.DiscoveredOperationBase;
import at.sti2.msee.discovery.core.tree.DiscoveredOperationHrests;
import at.sti2.msee.discovery.core.tree.DiscoveredService;
import at.sti2.msee.discovery.core.tree.DiscoveredServiceBase;
import at.sti2.msee.discovery.core.tree.DiscoveredServiceHrests;
import at.sti2.msee.triplestore.ServiceRepository;

/**
 * This class represents the concrete implementation of the {@link Discovery}
 * interface. It provides methods for retrieving registered services.
 * 
 */
public class DiscoveryServiceImpl implements Discovery {

	private final Logger LOGGER = LogManager.getLogger(this.getClass().getName());
	private ServiceRepository serviceRepository = null;
	private DiscoveryQueryBuilder discoveryQueryBuilder = null;

	/**
	 * The constructor takes the service repository as argument, which can not
	 * be NULL.
	 * 
	 * @param serviceRepository
	 */
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

		String query = discoveryQueryBuilder.getDiscoverQuery2Args(categoryList);

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
	public Map<String, String> discoverMap(String[] categoryList) throws DiscoveryException {
		Map<String, String> categoryMap = new HashMap<String, String>();
		for (String category : categoryList) {
			categoryMap.put(category, discover(new String[] { category }));
		}
		return categoryMap;
	}

	/**
	 * This method converts the RDF statements called through an iterator into
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
				rdfxmlWriter.handleStatement(convertStatement(statement));
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
	 * Converts the given statement of type {@link Statement} (from RDF2GO) into
	 * {@link StatementImpl} (from openrdf).
	 */
	private StatementImpl convertStatement(Statement statement) {
		Resource s = new BNodeImpl(statement.getSubject().toString());
		URI p = new URIImpl(statement.getPredicate().toString());
		Resource o = new BNodeImpl(statement.getObject().toString());
		return new StatementImpl(s, p, o);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.msee.discovery.api.webservice.Discovery#discoverAdvanced(java
	 * .lang.String[], java.lang.String[], java.lang.String[])
	 */
	@Override
	public String discoverAdvanced(String[] categoryList, String[] inputParamList,
			String[] outputParamList) throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.msee.discovery.api.webservice.Discovery#lookup(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String lookup(String namespace, String operationName) throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.msee.discovery.api.webservice.Discovery#getIServeModel(java.lang
	 * .String)
	 */
	@Override
	public String getIServeModel(String serviceID) throws DiscoveryException {
		throw new DiscoveryException("Not yet implemented");
	}

	/**
	 * Returns the service categories as a String list.
	 * 
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
	 * This method builds a tree of services of the following structure:
	 * 
	 */
	// category1 -service1.1
	// --operation1.1.1
	// --operation1.1.2
	// -service1.2
	// --operation1.2.1
	// -service1.3
	// --operation1.3.1
	// category2
	// -service2.1
	// --operation2.1.1
	public Set<DiscoveredCategory> discoverCategoryAndService() {
		// each category has a list of services
		Map<String, List<String>> helperServiceMap = new HashMap<String, List<String>>();
		// each service has a list of operations
		Map<String, List<String>> helperOperationMap = new HashMap<String, List<String>>();

		String queryString = new DiscoveryQueryBuilder().getDiscoverCategoriesAndServices();
		Model rdfModel = serviceRepository.getModel();
		rdfModel.open();
		ClosableIterable<QueryRow> resultTable = rdfModel.sparqlSelect(queryString);
		ClosableIterator<QueryRow> results = resultTable.iterator();

		createHelperMaps(results, helperServiceMap, helperOperationMap);
		Set<DiscoveredCategory> returnSet = buildTreeFromHelperMaps(helperServiceMap,
				helperOperationMap);

		rdfModel.close();
		return returnSet;
	}

	/**
	 * Splits up the given SPARQL query result into the helper maps.
	 * 
	 * @param tableOfQueryResult
	 * @param helperServiceMap
	 * @param helperOperationMap
	 */
	private void createHelperMaps(ClosableIterator<QueryRow> tableOfQueryResult,
			Map<String, List<String>> helperServiceMap, Map<String, List<String>> helperOperationMap) {
		while (tableOfQueryResult.hasNext()) {
			QueryRow row = tableOfQueryResult.next();
			String category = row.getValue("category").toString();

			// get current category
			List<String> serviceList = helperServiceMap.get(category);
			String service = row.getValue("serviceID").toString();
			if (serviceList == null) {
				serviceList = new ArrayList<String>();
			}
			if (!serviceList.contains(service)) {
				serviceList.add(service);
			}
			helperServiceMap.put(category, serviceList);

			List<String> operationList = helperOperationMap.get(service);
			String operation = row.getValue("operation").toString();
			if (operationList == null) {
				operationList = new ArrayList<String>();
			}
			if (!operationList.contains(operation)) {
				operationList.add(operation);
			}
			helperOperationMap.put(service, operationList);
		}
	}

	/**
	 * Creates the final tree of categories, services, operations from the given
	 * helper maps.
	 * 
	 * @param helperServiceMap
	 * @param helperOperationMap
	 * @return
	 */
	private Set<DiscoveredCategory> buildTreeFromHelperMaps(
			Map<String, List<String>> helperServiceMap, Map<String, List<String>> helperOperationMap) {
		Set<DiscoveredCategory> returnSet = new HashSet<DiscoveredCategory>();
		Object[] categories = helperServiceMap.keySet().toArray();
		for (Object cate : categories) {
			String cat = (String) cate;

			DiscoveredCategoryImpl category = new DiscoveredCategoryImpl(cat);
			DiscoveredCategory storedCategory = getCategoryFromSet(category, returnSet);
			if (storedCategory != null) {
				category = (DiscoveredCategoryImpl) storedCategory;
			}
			// add services
			for (String serv : helperServiceMap.get(cat)) {
				DiscoveredService service = new DiscoveredServiceBase(serv);
				// add operations
				for (String oper : helperOperationMap.get(serv)) {
					DiscoveredOperation operation = new DiscoveredOperationBase(oper);
					((DiscoveredServiceBase) service).addDiscoveredOperation(operation);
				}
				category.addDiscoveredService(service);
			}

			returnSet.add(category);
		}
		return returnSet;
	}

	/**
	 * Returns the {@link DiscoveredCategory} if found in the set of
	 * {@link DiscoveredCategory} otherwise <code>null</code>.
	 * 
	 * @param category
	 * @param categorySet
	 * @return
	 */
	private DiscoveredCategory getCategoryFromSet(DiscoveredCategory category,
			Set<DiscoveredCategory> categorySet) {
		Iterator<DiscoveredCategory> cit = categorySet.iterator();
		while (cit.hasNext()) {
			DiscoveredCategory cat = cit.next();
			if (cat.equals(category)) {
				return cat;
			}
		}
		return null;
	}

	@Deprecated
	public Set<DiscoveredServiceHrests> discoverServices(String[] categoryList) {
		Set<DiscoveredServiceHrests> returnSet = new HashSet<DiscoveredServiceHrests>();
		LOGGER.debug("Starting discover()");
		Model rdfModel = serviceRepository.getModel();
		rdfModel.open();

		String queryString = new DiscoveryQueryBuilder().getDiscoverServices(categoryList);

		ClosableIterable<QueryRow> resultTable = rdfModel.sparqlSelect(queryString);
		ClosableIterator<QueryRow> results = resultTable.iterator();

		while (results.hasNext()) {
			QueryRow row = results.next();
			String serviceID = row.getValue("serviceID").toString();
			String operationName = row.getValue("operation").toString();
			String method = null;
			String address = null;
			String outputMessageContent = null;
			String inputMessageContent = null;
			if (row.getValue("method") != null && row.getValue("address") != null) {
				method = row.getValue("method").toString();
				address = row.getValue("address").toString();
			}
			if (row.getValue("outputMessageContent") != null) {
				outputMessageContent = row.getValue("outputMessageContent").toString();
			}
			if (row.getValue("inputMessageContent") != null) {
				inputMessageContent = row.getValue("inputMessageContent").toString();
			}
			DiscoveredServiceHrests service = new DiscoveredServiceHrests(serviceID);
			Iterator<DiscoveredServiceHrests> its = returnSet.iterator();
			while (its.hasNext()) {
				DiscoveredServiceHrests tmpService = its.next();
				if (tmpService.equals(service)) {
					service = tmpService;
					break;
				}
			}
			returnSet.add(service);

			// operation
			DiscoveredOperationHrests operation = new DiscoveredOperationHrests(operationName);
			operation.setAddress(address);
			operation.setMethod(method);
			if (!service.getOperationSet().contains(operation)) {
				service.addDiscoveredOperation(operation);
			}

			Iterator<DiscoveredOperation> ito = service.getOperationSet().iterator();
			while (ito.hasNext()) {
				DiscoveredOperation tmpOperation = ito.next();
				if (tmpOperation.equals(operation)) {
					((DiscoveredOperationHrests) tmpOperation).addInput(inputMessageContent);
					((DiscoveredOperationHrests) tmpOperation).addOutput(outputMessageContent);
				}
			}

		}
		results.close();
		rdfModel.close();

		return returnSet;
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

	/**
	 * Returns the number of registered services.
	 */
	public int countServices() {
		Model rdfModel = serviceRepository.getModel();
		rdfModel.open();
		String query = "select ?s WHERE {?s <" + RDF.TYPE + "> <" + MSM.SERVICE + "> }";
		ClosableIterable<QueryRow> resultTable = rdfModel.sparqlSelect(query);
		int counter = 0;
		ClosableIterator<QueryRow> it = resultTable.iterator();
		while (it.hasNext()) {
			it.next();
			counter++;
		}
		rdfModel.close();
		return counter;
	}
}
