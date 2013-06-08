package at.sti2.msee.discovery.core;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.core.common.DiscoveryConfig;
import at.sti2.msee.discovery.core.common.ServiceDiscoveryConfiguration;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

/**
 * This factory class creates Discovery instances.
 * 
 * @author Christian Mayr
 * 
 */
public final class ServiceDiscoveryFactory {

	private static final Logger LOGGER = LogManager.getLogger(ServiceDiscoveryFactory.class
			.getName());
	private static String resourceLocation = "/default.properties";

	/**
	 * Static class
	 */
	private ServiceDiscoveryFactory() {
	};

	/**
	 * Creates a default {@link Discovery} instance.
	 */
	public static Discovery createDiscoveryService() {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		DiscoveryConfig config;
		try {
			config = new DiscoveryConfig();
			config.setResourceLocation(resourceLocation);
			serviceRepositoryConfiguration.setRepositoryID(config.getSesameRepositoryID());
			serviceRepositoryConfiguration.setServerEndpoint(config.getSesameEndpoint());
		} catch (IOException e) {
			LOGGER.error(e);
		}
		ServiceDiscoveryConfiguration serviceDiscoveryConfiguration = new ServiceDiscoveryConfiguration(
				serviceRepositoryConfiguration);
		ServiceRepository serviceRepository = ServiceRepositoryFactory
				.newInstance(serviceDiscoveryConfiguration.getRepositoryConfiguration());
		return new DiscoveryServiceImpl(serviceRepository);
	}

	/**
	 * Creates a {@link Discovery} instance based on
	 * {@link ServiceDiscoveryConfiguration}.
	 */
	public static Discovery createDiscoveryService(
			ServiceDiscoveryConfiguration serviceDiscoveryConfiguration) {
		ServiceRepository serviceRepository = ServiceRepositoryFactory
				.newInstance(serviceDiscoveryConfiguration.getRepositoryConfiguration());
		return new DiscoveryServiceImpl(serviceRepository);
	}

	/**
	 * Creates a {@link Discovery} instance based on the given server end point
	 * and the given repository identifier.
	 */
	public static Discovery createDiscoveryService(final String serverEndpoint,
			final String repositoryId) {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		DiscoveryConfig config;
		try {
			config = new DiscoveryConfig();
			config.setResourceLocation(resourceLocation);
			serviceRepositoryConfiguration.setRepositoryID(config.getSesameRepositoryID());
			serviceRepositoryConfiguration.setServerEndpoint(config.getSesameEndpoint());
			if (repositoryId != null) {
				serviceRepositoryConfiguration.setRepositoryID(repositoryId);
			}
			if (serverEndpoint != null) {
				serviceRepositoryConfiguration.setServerEndpoint(serverEndpoint);
			}
		} catch (IOException e) {
			LOGGER.error(e);
		}
		ServiceDiscoveryConfiguration serviceDiscoveryConfiguration = new ServiceDiscoveryConfiguration(
				serviceRepositoryConfiguration);
		ServiceRepository serviceRepository = ServiceRepositoryFactory
				.newInstance(serviceDiscoveryConfiguration.getRepositoryConfiguration());
		return new DiscoveryServiceImpl(serviceRepository);
	}

	public static Discovery createDiscoveryService(ServiceRepository serviceRepository) {
		return new DiscoveryServiceImpl(serviceRepository);
	}
}