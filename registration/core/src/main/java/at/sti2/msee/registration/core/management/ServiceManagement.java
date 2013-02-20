package at.sti2.msee.registration.core.management;

import java.io.IOException;

import org.openrdf.repository.RepositoryException;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.common.RegistrationConfig;
import at.sti2.util.triplestore.RepositoryHandler;

public abstract class ServiceManagement {
	private static RepositoryHandler reposHandler;

	public static String delete(String _serviceURI)
			throws ServiceRegistrationException {
		// Initialising the repository
		ServiceManagement.initRepo();
		try {
			// Delete the graph from repository and shut it down
			reposHandler.deleteContext(_serviceURI);
			reposHandler.shutdown();

			return _serviceURI;
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String update(String _oldServiceURI, String _newServiceURI)
			throws ServiceRegistrationException {
		// Initialising the repository
		ServiceManagement.initRepo();

		// Update through delete and add functionality
		String registredServiceURI = TransformationWSDL
				.transformWSDL(_newServiceURI);
		if (registredServiceURI != null) {
			ServiceManagement.delete(_oldServiceURI);
			return registredServiceURI;
		}
		return null;
	}

	/**
	 * Initialising the repository needed for all the above methods.
	 * 
	 * @throws ManagementException
	 */
	private static void initRepo() throws ServiceRegistrationException {
		try {
			RegistrationConfig cfg = new RegistrationConfig();
			reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(),
					cfg.getSesameReposID(), false);
		} catch (IOException e) {
			throw new ServiceRegistrationException(
					"The repository endpoint ID or the WSDL file could NOT be found.",
					e.getCause());
		}
	}
}
