package at.sti2.msee.registration.core;

import at.sti2.msee.registration.api.ServiceRegistration;
import at.sti2.msee.registration.core.configuration.ServiceRegistrationConfiguration;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

public class ServiceRegistrationFactory {
	
	public static ServiceRegistration createServiceRegistration(ServiceRegistrationConfiguration serviceRegistrationConfiguration)
	{
		ServiceRepository serviceRepository = ServiceRepositoryFactory.newInstance(serviceRegistrationConfiguration.getRepositoryConfiguration());
		return new ServiceRegistrationImpl(serviceRepository);		
	}	
}
