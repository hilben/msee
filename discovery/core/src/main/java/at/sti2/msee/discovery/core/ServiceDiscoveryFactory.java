package at.sti2.msee.discovery.core;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.core.common.ServiceDiscoveryConfiguration;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

public class ServiceDiscoveryFactory {

	public static Discovery createDiscoveryService(
			ServiceDiscoveryConfiguration serviceDiscoveryConfiguration) {
		ServiceRepository serviceRepository = ServiceRepositoryFactory
				.newInstance(serviceDiscoveryConfiguration
						.getRepositoryConfiguration());
		return new DiscoveryServiceImpl(serviceRepository);
	}
}