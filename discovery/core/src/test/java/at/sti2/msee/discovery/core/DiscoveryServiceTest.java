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

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.core.common.DiscoveryConfig;
import at.sti2.msee.registration.core.ServiceRegistrationImpl;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 * 
 *         TODO: use better logging
 */
public class DiscoveryServiceTest extends TestCase {
	private String resourceLocation = "/default.properties";
	private static Discovery discoveryService;
	private static ServiceRepository serviceRepository;

	@Before
	public void setUp() throws Exception {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		
		DiscoveryConfig config = new DiscoveryConfig();
		config.setResourceLocation(resourceLocation);		
		
		//Comment these 2 lines to force a in-memory repository
		serviceRepositoryConfiguration.setRepositoryID(config.getSesameRepositoryID());
		serviceRepositoryConfiguration.setServerEndpoint(config.getSesameEndpoint());
	
		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();
		serviceRepository.clear();

		String serviceDescriptionURL = this.getClass().getResource("/HelloService.sawsdl").toString();	
		ServiceRegistrationImpl registrationService = new ServiceRegistrationImpl(serviceRepository);
		registrationService.register(serviceDescriptionURL);

		discoveryService = new DiscoveryServiceImpl(serviceRepository);
	}
	
	@After
	public void tearDown() throws Exception {
		serviceRepository.shutdown();
	}

	@Test
	public void testDiscoverQuery2Args() throws Exception {
		final String[] categoryList = new String[1];
		categoryList[0] = "http://msee.sti2.at/categories#business";
		assertTrue(discoveryService.discover(categoryList).length()>0);
		//System.out.println(discoveryService.discover(categoryList));
	}

	@Test
	public void testGetServiceCategoriesHasElements() throws IOException  {

		String[] categories = ((DiscoveryServiceImpl)discoveryService).getServiceCategories();

		TestCase.assertTrue(categories.length>0);
		// TODO: write test for real function after registration is working?
	}
/*
//	@Test
//	public void testGetServiceCategories() throws QueryEvaluationException, RepositoryException, MalformedQueryException, TupleQueryResultHandlerException, UnsupportedQueryResultFormatException, IOException {
//		String[] categories = discoveryService.getServiceCategories();
//		TestCase.assertTrue(categories.length>0);
//		// TODO: write test for real function after registration is working?
//	}

	@Test
	public void testAlreadyInTripleStore() throws QueryEvaluationException,
			RepositoryException, MalformedQueryException,
			TupleQueryResultHandlerException,
			UnsupportedQueryResultFormatException, IOException {
		Assert.assertFalse(discoveryService
				.alreadyInTripleStore("http://xyz.com#one"));
	}
*/
}
