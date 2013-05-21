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
public class DiscoveryServiceTest_ForTree extends TestCase {
	private String resourceLocation = "/default.properties";
	private static Discovery discoveryService;
	private static ServiceRepository serviceRepository;

	@BeforeClass
	public void setUp() throws Exception {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();

		// Comment these 4 lines to force a in-memory repository
		// DiscoveryConfig config = new DiscoveryConfig();
		// config.setResourceLocation(resourceLocation);
		// serviceRepositoryConfiguration.setRepositoryID(config.getSesameRepositoryID());
		// serviceRepositoryConfiguration.setServerEndpoint(config.getSesameEndpoint());

		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();
		serviceRepository.clear();

		String serviceDescriptionURL = this.getClass().getResource("/services/HelloService.sawsdl")
				.toString();
		ServiceRegistrationImpl registrationService = new ServiceRegistrationImpl(serviceRepository);
		registrationService.register(serviceDescriptionURL);

		serviceDescriptionURL = this.getClass().getResource("/HelloServiceWith2Categories.sawsdl")
				.toString();
		registrationService.register(serviceDescriptionURL);

		serviceDescriptionURL = this.getClass().getResource("/services/hrests1.html").toString();
		registrationService.register(serviceDescriptionURL);

		serviceDescriptionURL = this.getClass().getResource("/services/hrests1.html").toString();
		registrationService.register(serviceDescriptionURL);

		discoveryService = new DiscoveryServiceImpl(serviceRepository);
	}

	@AfterClass
	public void tearDown() throws Exception {
		serviceRepository.clear();
		serviceRepository.shutdown();
	}

	@Test
	public void testGetTree() throws IOException, ServiceRegistrationException {
		System.out.println(((DiscoveryServiceImpl) discoveryService).discoverCategoryAndService());
	}

}
