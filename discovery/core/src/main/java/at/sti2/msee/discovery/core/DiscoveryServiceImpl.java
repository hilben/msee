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
import org.ontoware.rdf2go.vocabulary.RDF;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;
import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilder;
import at.sti2.msee.discovery.core.tree.DiscoveredCategory;
import at.sti2.msee.discovery.core.tree.DiscoveredOperation;
import at.sti2.msee.discovery.core.tree.DiscoveredOperationBase;
import at.sti2.msee.discovery.core.tree.DiscoveredService;
import at.sti2.msee.discovery.core.tree.DiscoveredServiceBase;
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

	private DiscoveryTreeHandler treeHandler = null;

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
		LOGGER.info(serviceRepository.getServiceRepositoryConfiguration().getRepositoryID() + " - "
				+ serviceRepository.getServiceRepositoryConfiguration().getServerEndpoint());
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

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * at.sti2.msee.discovery.api.webservice.Discovery#discoverAdvanced(java
//	 * .lang.String[], java.lang.String[], java.lang.String[])
//	 */
//	@Override
//	public String discoverAdvanced(String[] categoryList, String[] inputParamList,
//			String[] outputParamList) throws DiscoveryException {
//		throw new DiscoveryException("Not yet implemented");
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * at.sti2.msee.discovery.api.webservice.Discovery#lookup(java.lang.String,
//	 * java.lang.String)
//	 */
//	@Override
//	public String lookup(String namespace, String operationName) throws DiscoveryException {
//		throw new DiscoveryException("Not yet implemented");
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * at.sti2.msee.discovery.api.webservice.Discovery#getIServeModel(java.lang
//	 * .String)
//	 */
//	@Override
//	public String getIServeModel(String serviceID) throws DiscoveryException {
//		throw new DiscoveryException("Not yet implemented");
//	}

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
		String queryString = new DiscoveryQueryBuilder().getDiscoverCategoriesAndServices();
		Model rdfModel = serviceRepository.getModel();
		rdfModel.open();
		ClosableIterable<QueryRow> resultTable = rdfModel.sparqlSelect(queryString);
		ClosableIterator<QueryRow> results = resultTable.iterator();

		DiscoveryTreeBuilder treeBuilder = new DiscoveryTreeBuilder(results);
		Set<DiscoveredCategory> returnSet = new HashSet<DiscoveredCategory>();
		try {
			returnSet = treeBuilder.buildTree();
			treeHandler = new DiscoveryTreeHandler(returnSet);
		} catch (DiscoveryException e) {
			LOGGER.catching(e);
		}

		rdfModel.close();
		return returnSet;
	}

	/**
	 * This method discovers the related and stored information on the service
	 * based on the given service ID.
	 * 
	 * @param serviceID
	 * @return {@link DiscoveredService}
	 * @throws DiscoveryException
	 */
	public DiscoveredService discoverService(String serviceID) throws DiscoveryException {
		String queryString = new DiscoveryQueryBuilder().getDiscoverServiceOnServiceID(serviceID);
		Model rdfModel = serviceRepository.getModel();
		rdfModel.open();

		ClosableIterable<QueryRow> resultTable = rdfModel.sparqlSelect(queryString);
		ClosableIterator<QueryRow> results = resultTable.iterator();

		DiscoveryTreeBuilder treeBuilder = new DiscoveryTreeBuilder(results);
		// -> reuse of existing method that returns category set
		// -> but in this case it only returns one category with one
		// -> service or NULL
		Set<DiscoveredCategory> categorySet = new HashSet<DiscoveredCategory>();
		try {
			categorySet = treeBuilder.buildTree();
		} catch (DiscoveryException e) {
			LOGGER.catching(e);
			throw new DiscoveryException(e);
		}

		rdfModel.close();

		treeHandler = new DiscoveryTreeHandler(categorySet);
		DiscoveredService service = treeHandler.getService(serviceID);
		return (service != null) ? service : null;
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
		return getServiceList().size();
	}

	/**
	 * Returns the list of all service IDs.
	 */
	public List<String> getServiceList() {
		List<String> returnList = new ArrayList<String>();
		Model rdfModel = serviceRepository.getModel();
		rdfModel.open();
		String query = "select * where {?serviceID " + RDF.type.toSPARQL() + " "
				+ MSM.Service.toSPARQL() + " . }";
		ClosableIterable<QueryRow> resultTable = rdfModel.sparqlSelect(query);
		ClosableIterator<QueryRow> results = resultTable.iterator();
		while (results.hasNext()) {
			QueryRow row = results.next();
			String serviceID = row.getValue("serviceID").toString();
			returnList.add(serviceID);
		}
		rdfModel.close();
		return returnList;
	}

	/**
	 * Returns the input list for a given service ID and a given operation name.
	 * 
	 * @param serviceID
	 * @param operationName
	 * @throws DiscoveryException
	 */
	public List<String> getInputList(String serviceID, String operationName)
			throws DiscoveryException {
		return treeHandler.getInputList(serviceID, operationName);
	}

	/**
	 * Returns the output list for a given service ID and a given operation
	 * name.
	 * 
	 * @param serviceID
	 * @param operationName
	 * @throws DiscoveryException
	 */
	public List<String> getOutputList(String serviceID, String operationName)
			throws DiscoveryException {
		return treeHandler.getOutputList(serviceID, operationName);
	}

	public List<String> getInputList(final String operationName) throws DiscoveryException {
		return treeHandler.getInputList(operationName);
	}

	public List<String> getInputVaultList(final String operationName) throws DiscoveryException {
		return treeHandler.getInputVaultList(operationName);
	}

	public List<String> getOutputList(final String operationName) throws DiscoveryException {
		return treeHandler.getOutputList(operationName);
	}

	public List<String> getOutputVaultList(final String operationName) throws DiscoveryException {
		return treeHandler.getOutputVaultList(operationName);
	}

	/* ************************************************************************
	 * Old Methods
	 */
	@Deprecated
	public Set<DiscoveredService> discoverServices(String[] categoryList) {
		Set<DiscoveredService> returnSet = new HashSet<DiscoveredService>();
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
			DiscoveredServiceBase service = new DiscoveredServiceBase(serviceID);
			Iterator<DiscoveredService> its = returnSet.iterator();
			while (its.hasNext()) {
				DiscoveredServiceBase tmpService = (DiscoveredServiceBase) its.next();
				if (tmpService.equals(service)) {
					service = tmpService;
					break;
				}
			}
			returnSet.add(service);

			// operation
			DiscoveredOperationBase operation = new DiscoveredOperationBase(operationName);
			operation.addAddress(address);
			operation.addMethod(method);
			if (!service.getOperationSet().contains(operation)) {
				service.addDiscoveredOperation(operation);
			}

			Iterator<DiscoveredOperation> ito = service.getOperationSet().iterator();
			while (ito.hasNext()) {
				DiscoveredOperationBase tmpOperation = (DiscoveredOperationBase) ito.next();
				if (tmpOperation.equals(operation)) {
					tmpOperation.addInput(inputMessageContent);
					tmpOperation.addOutput(outputMessageContent);
				}
			}

		}
		results.close();
		rdfModel.close();

		return returnSet;
	}
}
