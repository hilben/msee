package at.sti2.msee.triplestore;

import at.sti2.msee.triplestore.impl.SesameServiceRepositoryImpl;

public class ServiceRepositoryFactory {
	public static ServiceRepository newInstance(ServiceRepositoryConfiguration serviceRepositoryConfiguration) {
		return new SesameServiceRepositoryImpl(serviceRepositoryConfiguration);
	}
}