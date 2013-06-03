import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.core.DiscoveryServiceImpl;
import at.sti2.msee.discovery.core.ServiceDiscoveryFactory;
import at.sti2.msee.discovery.core.common.DiscoveryConfig;
import at.sti2.msee.discovery.core.common.ServiceDiscoveryConfiguration;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;

public class ServiceDiscoveryFactoryTest {
	private String resourceLocation = "/test.properties";

	@Test
	public final void testCreateDiscoveryServiceServiceDiscoveryConfiguration() throws IOException {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		DiscoveryConfig config;
		config = new DiscoveryConfig();
		config.setResourceLocation(resourceLocation);
		String repositoryId = config.getSesameRepositoryID();
		String endpoint = config.getSesameEndpoint();
		serviceRepositoryConfiguration.setRepositoryID(repositoryId);
		serviceRepositoryConfiguration.setServerEndpoint(endpoint);
		ServiceDiscoveryConfiguration serviceDiscoveryConfiguration = new ServiceDiscoveryConfiguration(
				serviceRepositoryConfiguration);
		Discovery service = ServiceDiscoveryFactory
				.createDiscoveryService(serviceDiscoveryConfiguration);
		assertNotNull(service);
		ServiceRepository repository = ((DiscoveryServiceImpl) service).getServiceRepository();
		assertEquals(repository.getServiceRepositoryConfiguration().getRepositoryID(), repositoryId);
		assertEquals(repository.getServiceRepositoryConfiguration().getServerEndpoint(), endpoint);
	}

	@Test
	public final void testCreateDiscoveryService() {
		Discovery service = ServiceDiscoveryFactory.createDiscoveryService();
		assertNotNull(service);
		ServiceRepository repository = ((DiscoveryServiceImpl) service).getServiceRepository();
		assertEquals(repository.getServiceRepositoryConfiguration().getRepositoryID(), "msee-test");
		assertTrue(repository.getServiceRepositoryConfiguration().getServerEndpoint().contains("openrdf-sesame"));
	}

	@Test
	public final void testCreateDiscoveryServiceStringString() {
		String repositoryId = "theRepoId";
		String endpoint = "theEndpoint";
		Discovery service = ServiceDiscoveryFactory.createDiscoveryService(endpoint, repositoryId);
		assertNotNull(service);
		ServiceRepository repository = ((DiscoveryServiceImpl) service).getServiceRepository();
		assertEquals(repository.getServiceRepositoryConfiguration().getRepositoryID(), repositoryId);
		assertEquals(repository.getServiceRepositoryConfiguration().getServerEndpoint(), endpoint);
	}

}
