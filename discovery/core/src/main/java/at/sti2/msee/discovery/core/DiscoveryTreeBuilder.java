package at.sti2.msee.discovery.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.Node;

import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.tree.DiscoveredCategory;
import at.sti2.msee.discovery.core.tree.DiscoveredCategoryImpl;
import at.sti2.msee.discovery.core.tree.DiscoveredOperationBase;
import at.sti2.msee.discovery.core.tree.DiscoveredService;
import at.sti2.msee.discovery.core.tree.DiscoveredServiceBase;

/**
 * This class represents a service tree builder with is strongly connected to
 * {@link DiscoveryServiceImpl}. The structure is illustrated below.
 * 
 * 
 * 
 * @author Christian Mayr
 * 
 */
// category1
// -service1.1
// --operation1.1.1
// --operation1.1.2
// -service1.2
// --operation1.2.1
// -service1.3
// --operation1.3.1
// category2
// -service2.1
// --operation2.1.1
/**
 * 
 *
 */
/**
 * @author chrmay
 * 
 */
public class DiscoveryTreeBuilder {

	private ClosableIterator<QueryRow> queryRows;
	/**
	 * each category has a list of services
	 */
	private Map<String, List<String>> helperServiceMap = new HashMap<String, List<String>>();
	/**
	 * each service has a list of operations
	 */
	private Map<String, List<String>> helperOperationMap = new HashMap<String, List<String>>();

	private Map<String, List<String>> helperInputMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> helperOutputMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> helperInputVaultMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> helperOutputVaultMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> helperAddressMap = new HashMap<String, List<String>>();
	private Map<String, List<String>> helperMethodMap = new HashMap<String, List<String>>();

	private Map<String, String> helperServiceEndpoint = new HashMap<String, String>();
	private Map<String, String> helperServiceNamespace = new HashMap<String, String>();

	public DiscoveryTreeBuilder(ClosableIterator<QueryRow> queryRows) {
		this.queryRows = queryRows;
	}

	public Set<DiscoveredCategory> buildTree() throws DiscoveryException {
		if (queryRows == null) {
			throw new DiscoveryException(this.getClass() + " not initialized");
		}
		createHelperMaps();
		Set<DiscoveredCategory> returnSet = buildTreeFromHelperMaps();
		return returnSet;
	}

	/**
	 * Creates the final tree of categories, services, operations from the given
	 * helper maps.
	 * 
	 * @param helperServiceMap
	 * @param helperOperationMap
	 * @return
	 */
	private Set<DiscoveredCategory> buildTreeFromHelperMaps() {
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
				if (helperServiceEndpoint.containsKey(serv)) {
					service.setEndpoint(helperServiceEndpoint.get(serv));
				}
				if (helperServiceNamespace.containsKey(serv)) {
					service.setNameSpace(helperServiceNamespace.get(serv));
				}

				// add operations
				for (String oper : helperOperationMap.get(serv)) {
					DiscoveredOperationBase operation = new DiscoveredOperationBase(oper);
					((DiscoveredServiceBase) service).addDiscoveredOperation(operation);

					// add additional information
					for (String input : helperInputMap.get(oper)) {
						operation.addInput(input);
					}
					for (String output : helperOutputMap.get(oper)) {
						operation.addOutput(output);
					}
					for (String inputVault : helperInputVaultMap.get(oper)) {
						operation.addInputVault(inputVault);
					}
					for (String outputVault : helperOutputVaultMap.get(oper)) {
						operation.addOutputVault(outputVault);
					}

					// HRESTS
					for (String address : helperAddressMap.get(oper)) {
						operation.addAddress(address);
					}
					for (String method : helperMethodMap.get(oper)) {
						operation.addMethod(method);
					}
				}
				category.addDiscoveredService(service);
			}

			returnSet.add(category);
		}
		return returnSet;
	}

	/**
	 * Splits up the given SPARQL query result into the helper maps.
	 * 
	 * @param tableOfQueryResult
	 * @param helperServiceMap
	 * @param helperOperationMap
	 */
	private void createHelperMaps() {
		while (queryRows.hasNext()) {
			QueryRow row = queryRows.next();
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

			// get endpoint address for WSDL service
			if (row.getValue("endpoint") != null) {
				String endpoint = row.getValue("endpoint").toString();
				if (endpoint != null) {
					helperServiceEndpoint.put(service, endpoint);
				}
			}

			// get namespace of service
			if (row.getValue("namespace") != null) {
				String namespace = row.getValue("namespace").toString();
				if (namespace != null) {
					helperServiceNamespace.put(service, namespace);
				}
			}

			// get operations
			List<String> operationList = helperOperationMap.get(service);
			String operation = row.getValue("operation").toString();
			if (operationList == null) {
				operationList = new ArrayList<String>();
			}
			if (!operationList.contains(operation)) {
				operationList.add(operation);
			}
			helperOperationMap.put(service, operationList);

			fillAdditionalOperationHelperMaps(row, operation, helperInputMap, "operationInput");
			fillAdditionalOperationHelperMaps(row, operation, helperOutputMap, "operationOutput");
			fillAdditionalOperationHelperMaps(row, operation, helperInputVaultMap,
					"operationInputVault");
			fillAdditionalOperationHelperMaps(row, operation, helperOutputVaultMap,
					"operationOutputVault");
			fillAdditionalOperationHelperMaps(row, operation, helperAddressMap, "operationAddress");
			fillAdditionalOperationHelperMaps(row, operation, helperMethodMap, "operationMethod");
		}
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

	/**
	 * Extracts additional data about an operation from the data row and fills
	 * the helper maps.
	 * 
	 * @param row
	 *            - {@link QueryRow} of a SPARQL query
	 * @param operation
	 *            - name of the operation
	 * @param helperMap
	 *            - the actual helper map
	 * @param variableName
	 *            - name of the variable (column), e.g. operationInput,
	 *            operationOutput, etc.
	 */
	private void fillAdditionalOperationHelperMaps(QueryRow row, String operation,
			Map<String, List<String>> helperMap, String variableName) {
		List<String> additionalList = helperMap.get(operation);
		if (additionalList == null) {
			additionalList = new ArrayList<String>();
		}
		Node variableNode = row.getValue(variableName);
		if (variableNode != null) {
			String operationInput = variableNode.toString();
			if (!additionalList.contains(operationInput)) {
				additionalList.add(operationInput);
			}

		}
		helperMap.put(operation, additionalList);
	}

}
