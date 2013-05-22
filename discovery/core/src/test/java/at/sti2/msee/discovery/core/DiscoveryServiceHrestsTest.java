/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICUeLAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.msee.discovery.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.core.common.DiscoveryConfig;
import at.sti2.msee.discovery.core.tree.DiscoveredServiceHrests;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.ServiceRegistrationImpl;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

/**
 * @author Benjamin Hiltpolt
 * @author Christian Mayr
 * 

 */
public class DiscoveryServiceHrestsTest extends TestCase {
	private String resourceLocation = "/default.properties";
	private static Discovery discoveryService;
	private static ServiceRepository serviceRepository;

	@BeforeClass
	public void setUp() throws Exception {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
				
		//Comment these 4 lines to force a in-memory repository
		DiscoveryConfig config = new DiscoveryConfig();
		config.setResourceLocation(resourceLocation);	
		//serviceRepositoryConfiguration.setRepositoryID(config.getSesameRepositoryID());
		//serviceRepositoryConfiguration.setServerEndpoint(config.getSesameEndpoint());
	
		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();
		serviceRepository.clear();

		String serviceDescriptionURL = this.getClass().getResource("/services/hrests1.html").toString();	
		ServiceRegistrationImpl registrationService = new ServiceRegistrationImpl(serviceRepository);
		registrationService.register(serviceDescriptionURL);

		discoveryService = new DiscoveryServiceImpl(serviceRepository);
	}
	
	@AfterClass
	public void tearDown() throws Exception {
		serviceRepository.clear();
		//serviceRepository.shutdown();
	}

	@Test
	public void testDiscoverQuery2Args() throws Exception {
		final String[] categoryList = new String[1];
		categoryList[0] = "http://msee.sti2.at/categories#business";
		String result = discoveryService.discover(categoryList);
		
		assertTrue("Result is empty", result.length() > 0);
		assertContains(result, "#svc");
		assertContains(result, "#op1");
		assertContains(result, "http://msee.sti2.at/categories#business");
		
		assertNotContains(result, "wsdl.service");
	}

	private void assertContains(String haystack, String needle) {
		assertTrue(needle + " not found in " + haystack, haystack.contains(needle));
	}
	
	private void assertNotContains(String haystack, String needle) {
		assertTrue(needle + " FALSE found in " + haystack, !haystack.contains(needle));
	}
	
	private void assertContainsStrings(String haystack, String[] needleList){
		for(String needle : needleList){
			assertContains(haystack, needle);
		}
	}

	@Test
	public void testGetServiceCategoriesHasElements() throws IOException  {
		String[] expectedCategories = {"http://msee.sti2.at/categories#business"};
		String[] categories = ((DiscoveryServiceImpl)discoveryService).getServiceCategories();
		for(String category : categories){
			assertContainsStrings(category, expectedCategories);
		}
		assertTrue(categories.length==1);
	}
	
	@Test
	public void testGetServiceCategoriesTwoElements() throws IOException, ServiceRegistrationException  {
		String serviceDescriptionURL = this.getClass().getResource("/services/hrests1With2Categories.html").toString();	
		ServiceRegistrationImpl registrationService = new ServiceRegistrationImpl(serviceRepository);
		registrationService.register(serviceDescriptionURL);
		
		String[] expectedCategories = {"http://msee.sti2.at/categories#business", "http://msee.sti2.at/categories#otherbusiness"};
		String[] categories = ((DiscoveryServiceImpl)discoveryService).getServiceCategories();
		for(String expectedCategory : expectedCategories){
			assertContains(Arrays.toString(categories), expectedCategory);
			assertFalse(expectedCategory.equals("http://msee.sti2.at/categories#WRONGbusiness"));
		}
		assertTrue(categories.length==2);
	}
	
	
	
	@Test
	@Deprecated
	public void testDiscoverServices(){
		String[] categories = {"http://msee.sti2.at/categories#business"};
		Set<DiscoveredServiceHrests> tree = ((DiscoveryServiceImpl)discoveryService).discoverServices(categories);
	}

}
