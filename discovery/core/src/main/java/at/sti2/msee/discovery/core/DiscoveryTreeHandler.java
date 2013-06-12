package at.sti2.msee.discovery.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.tree.DiscoveredCategory;
import at.sti2.msee.discovery.core.tree.DiscoveredOperation;
import at.sti2.msee.discovery.core.tree.DiscoveredOperationBase;
import at.sti2.msee.discovery.core.tree.DiscoveredService;

/**
 * This class contains all methods for handling a tree of
 * {@link DiscoveredCategory} and {@link DiscoveredService} elements.
 * 
 */
public class DiscoveryTreeHandler {

	/**
	 * The tree of {@link DiscoveredService} to be used
	 */
	private Set<DiscoveredCategory> discoveredServiceTree = null;

	/**
	 * Constructs an instance with the given tree of discovered categories.
	 * 
	 * @param discoveredServiceTree
	 * @throws DiscoveryException
	 */
	DiscoveryTreeHandler(Set<DiscoveredCategory> discoveredServiceTree) throws DiscoveryException {
		this.discoveredServiceTree = discoveredServiceTree;
		assertTreeNotNull();
	}

	/**
	 * Returns a list of discovered categories.
	 * 
	 * @throws DiscoveryException
	 */
	public List<DiscoveredCategory> getCategoryList() throws DiscoveryException {
		assertTreeNotNull();
		List<DiscoveredCategory> categoryList = new ArrayList<DiscoveredCategory>();
		Iterator<DiscoveredCategory> itc = discoveredServiceTree.iterator();
		while (itc.hasNext()) {
			DiscoveredCategory category = itc.next();
			categoryList.add(category);
		}
		return categoryList;
	}

	/**
	 * Returns a category with the given name.
	 * 
	 * @param categoryName
	 * @throws DiscoveryException
	 */
	public DiscoveredCategory getCategory(String categoryName) throws DiscoveryException {
		assertTreeNotNull();
		Iterator<DiscoveredCategory> itc = discoveredServiceTree.iterator();
		while (itc.hasNext()) {
			DiscoveredCategory category = itc.next();
			if (category.getName().equalsIgnoreCase(categoryName)) {
				return category;
			}
		}
		throw new DiscoveryException("Category \"" + categoryName + "\" not found");
	}

	/**
	 * Returns a list of services for the given category name.
	 * 
	 * @param categoryName
	 * @throws DiscoveryException
	 */
	public List<DiscoveredService> getServiceList(String categoryName) throws DiscoveryException {
		assertTreeNotNull();
		List<DiscoveredService> serviceList = new ArrayList<DiscoveredService>();
		DiscoveredCategory category = getCategory(categoryName);
		Iterator<DiscoveredService> its = category.getServiceSet().iterator();
		while (its.hasNext()) {
			DiscoveredService service = its.next();
			serviceList.add(service);
		}
		return serviceList;
	}

	/**
	 * Returns the {@link DiscoveredService} for the given service ID.
	 * 
	 * @param serviceID
	 * @throws DiscoveryException
	 */
	public DiscoveredService getService(String serviceID) throws DiscoveryException {
		assertTreeNotNull();
		List<DiscoveredCategory> categoryList = getCategoryList();
		for (DiscoveredCategory category : categoryList) {
			List<DiscoveredService> serviceList = getServiceList(category.getName());
			for (DiscoveredService service : serviceList) {
				if (service.getName().equals(serviceID)) {
					return service;
				}
			}
		}
		throw new DiscoveryException("Service \"" + serviceID + "\" not found");
	}

	/**
	 * Returns the operation list for a given service ID.
	 * 
	 * @param serviceID
	 * @throws DiscoveryException
	 */
	public List<DiscoveredOperation> getOperationList(final String serviceID)
			throws DiscoveryException {
		assertTreeNotNull();
		List<DiscoveredOperation> operationList = new ArrayList<>();
		DiscoveredService service = getService(serviceID);
		Iterator<DiscoveredOperation> ito = service.getOperationSet().iterator();
		while (ito.hasNext()) {
			DiscoveredOperation operation = ito.next();
			operationList.add(operation);
		}
		return operationList;
	}

	/**
	 * Returns the discovered operation for a given service ID and a given
	 * operation name.
	 * 
	 * @param serviceID
	 * @param operation
	 * @throws DiscoveryException
	 */
	public DiscoveredOperation getOperation(final String serviceID, final String operationName)
			throws DiscoveryException {
		assertTreeNotNull();
		List<DiscoveredOperation> operationList = getOperationList(serviceID);
		for (DiscoveredOperation op : operationList) {
			if (op.getName().equals(operationName)) {
				return op;
			}
		}
		throw new DiscoveryException("Operation \"" + operationName + "\" not found");
	}

	/**
	 * Returns a list of inputs for a given service ID and a given operation
	 * name.
	 * 
	 * @param serviceID
	 * @param operation
	 * @return
	 * @throws DiscoveryException
	 */
	public List<String> getInputList(final String serviceID, final String operationName)
			throws DiscoveryException {
		assertTreeNotNull();
		List<String> inputList = new ArrayList<>();
		DiscoveredOperationBase operation = (DiscoveredOperationBase) getOperation(serviceID,
				operationName);

		Iterator<String> iti = operation.getInputSet().iterator();
		while (iti.hasNext()) {
			String input = iti.next();
			inputList.add(input);
		}
		return inputList;
	}

	/**
	 * Returns a list of outputs for a given service ID and a given operation
	 * name.
	 * 
	 * @param serviceID
	 * @param operation
	 * @return
	 * @throws DiscoveryException
	 */
	public List<String> getOutputList(String serviceID, String operationName)
			throws DiscoveryException {
		assertTreeNotNull();
		List<String> outputList = new ArrayList<>();
		DiscoveredOperationBase operation = (DiscoveredOperationBase) getOperation(serviceID,
				operationName);

		Iterator<String> ito = operation.getOutputSet().iterator();
		while (ito.hasNext()) {
			String output = ito.next();
			outputList.add(output);
		}
		return outputList;
	}

	/**
	 * Returns a list of input vaults for a given service ID and a given
	 * operation name.
	 * 
	 * @param serviceID
	 * @param operation
	 * @return
	 * @throws DiscoveryException
	 */
	public List<String> getInputVaultList(final String serviceID, final String operationName)
			throws DiscoveryException {
		assertTreeNotNull();
		List<String> inputVaultList = new ArrayList<>();
		DiscoveredOperationBase operation = (DiscoveredOperationBase) getOperation(serviceID,
				operationName);

		Iterator<String> iti = operation.getInputVaultSet().iterator();
		while (iti.hasNext()) {
			String inputVault = iti.next();
			inputVaultList.add(inputVault);
		}
		return inputVaultList;
	}

	/**
	 * Returns a list of output vaults for a given service ID and a given
	 * operation name.
	 * 
	 * @param serviceID
	 * @param operation
	 * @return
	 * @throws DiscoveryException
	 */
	public List<String> getOutputVaultList(String serviceID, String operationName)
			throws DiscoveryException {
		assertTreeNotNull();
		List<String> outputVaultList = new ArrayList<>();
		DiscoveredOperationBase operation = (DiscoveredOperationBase) getOperation(serviceID,
				operationName);

		Iterator<String> ito = operation.getOutputVaultSet().iterator();
		while (ito.hasNext()) {
			String outputVault = ito.next();
			outputVaultList.add(outputVault);
		}
		return outputVaultList;
	}

	/**
	 * Checks that the discovered service tree is not null.
	 * 
	 * @throws DiscoveryException
	 */
	private void assertTreeNotNull() throws DiscoveryException {
		if (discoveredServiceTree == null) {
			throw new DiscoveryException("Tree is NULL (you should run first: "
					+ "discoverCategoryAndService() in DiscoveryServiceImpl");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DiscoveryTreeHandler [discoveredServiceTree=" + discoveredServiceTree + "]";
	}

}
