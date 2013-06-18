package at.sti2.msee.discovery.core.util;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.discovery.core.tree.DiscoveredCategory;
import at.sti2.msee.discovery.core.tree.DiscoveredCategoryImpl;
import at.sti2.msee.discovery.core.tree.DiscoveredOperation;
import at.sti2.msee.discovery.core.tree.DiscoveredOperationBase;
import at.sti2.msee.discovery.core.tree.DiscoveredService;
import at.sti2.msee.discovery.core.tree.DiscoveredServiceBase;

public class ServiceTypeFinderTest {
	private static DiscoveredCategory category;

	@Before
	public void setup() {
		category = new DiscoveredCategoryImpl("someCategory");
	}

	@Test
	public void testGetTypeRest() {
		DiscoveredOperationBase restOperation1 = new DiscoveredOperationBase("operation1");
		DiscoveredService restService1 = new DiscoveredServiceBase("service1");
		restService1.addDiscoveredOperation(restOperation1);
		category.addDiscoveredService(restService1);

		Set<DiscoveredService> services = category.getServiceSet();
		Iterator<DiscoveredService> its = services.iterator();
		while (its.hasNext()) {
			assertEquals(ServiceType.REST, ServiceTypeFinder.getType(its.next()));
		}
	}
	
	@Test
	public void testGetTypeWsdl() {
		DiscoveredOperation operation1 = new DiscoveredOperationBase("wsdl.service(service1)/operation1");
		DiscoveredService service1 = new DiscoveredServiceBase("wsdl.service(service1)");
		service1.addDiscoveredOperation(operation1);
		category.addDiscoveredService(service1);

		Set<DiscoveredService> services = category.getServiceSet();
		Iterator<DiscoveredService> its = services.iterator();
		while (its.hasNext()) {
			assertEquals(ServiceType.WSDL, ServiceTypeFinder.getType(its.next()));
		}
	}
	
	@Test
	public void testGetTypeOther() {
		DiscoveredOperation operation1 = new DiscoveredOperationBase("abc");
		DiscoveredService service1 = new DiscoveredServiceBase("abc");
		service1.addDiscoveredOperation(operation1);
		category.addDiscoveredService(service1);

		Set<DiscoveredService> services = category.getServiceSet();
		Iterator<DiscoveredService> its = services.iterator();
		while (its.hasNext()) {
			assertEquals(ServiceType.OTHERORUNKNOWN, ServiceTypeFinder.getType(its.next()));
		}
	}

}
