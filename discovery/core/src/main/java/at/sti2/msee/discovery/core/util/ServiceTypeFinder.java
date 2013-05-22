package at.sti2.msee.discovery.core.util;

import java.util.Set;

import at.sti2.msee.discovery.core.tree.DiscoveredOperation;
import at.sti2.msee.discovery.core.tree.DiscoveredOperationHrests;
import at.sti2.msee.discovery.core.tree.DiscoveredService;

public class ServiceTypeFinder {
	public static ServiceType getType(DiscoveredService discoveredService) {
		if (discoveredService.getName().contains("wsdl.service")) {
			return ServiceType.WSDL;
		} else {
			Set<DiscoveredOperation> operations = discoveredService.getOperationSet();
			if (operations.iterator().hasNext()) {
				try {
					DiscoveredOperationHrests rest = (DiscoveredOperationHrests) operations
							.iterator().next();
					rest.getMethod();
				} catch (ClassCastException e) {
					return ServiceType.OTHERORUNKNOWN;
				}
				return ServiceType.REST;
			}
		}
		return ServiceType.OTHERORUNKNOWN;
	}
}
