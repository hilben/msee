package at.sti2.msee.discovery.core;

import org.junit.Test;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.common.ServiceDiscoveryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;

public class DiscoveryServiceTestForDashboard {

	@Test
	public void testDiscover() {
		String serverEndpoint = "http://sesa.sti2.at:8080/openrdf-sesame";
		String repositoryId = "msee";

		ServiceRepositoryConfiguration repositoryConfiguration = new ServiceRepositoryConfiguration();
		repositoryConfiguration.setRepositoryID(repositoryId);
		repositoryConfiguration.setServerEndpoint(serverEndpoint);

		ServiceDiscoveryConfiguration serviceDiscoveryConfiguration = new ServiceDiscoveryConfiguration(
				repositoryConfiguration);
		Discovery discovery = ServiceDiscoveryFactory
				.createDiscoveryService(serviceDiscoveryConfiguration);

		String category1 = "http://msee.sti2.at/categories#business";
		String[] categories = { category1 };
		String result = null;
		try {
			result = discovery.discover(categories);
		} catch (DiscoveryException e) {
			e.printStackTrace();
		}
		//System.out.println(result);
	}
}
