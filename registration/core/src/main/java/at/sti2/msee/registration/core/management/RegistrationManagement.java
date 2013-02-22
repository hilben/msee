package at.sti2.msee.registration.core.management;

import java.io.IOException;

import org.openrdf.repository.RepositoryException;

import at.sti2.msee.registration.api.exception.RegistrationException;
import at.sti2.msee.registration.core.common.RegistrationConfig;
import at.sti2.util.triplestore.RepositoryHandler;

public abstract class RegistrationManagement {
	private static RepositoryHandler repositoryHandler;

	/**
	 * Delete all context associated with a service URI
	 * 
	 * @param serviceURI
	 * @return
	 * @throws RegistrationException
	 */
	public static String delete(String serviceURI) throws RegistrationException {

		RegistrationManagement.initRepo();
		try {
			repositoryHandler.deleteContext(serviceURI);
			repositoryHandler.shutdown();

			return serviceURI;
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Update a web service by deleting it and then reuploading it
	 * 
	 * @param oldServiceURI
	 * @param newServiceURI
	 * @return
	 * @throws RegistrationException
	 */
	public static String update(String oldServiceURI, String newServiceURI)
			throws RegistrationException {
		// Initialising the repository
		RegistrationManagement.initRepo();

		// Update through delete and add functionality
		RegistrationWSDLToTriplestoreWriter registration = new RegistrationWSDLToTriplestoreWriter();

		String registredServiceURI = registration
				.transformWSDLtoTriplesAndStoreInTripleStore(newServiceURI);
		if (registredServiceURI != null) {
			RegistrationManagement.delete(oldServiceURI);
			return registredServiceURI;
		}
		return null;
	}

	/**
	 * Initialising the repository needed for all the above methods.
	 * 
	 * @throws ManagementException
	 */
	private static void initRepo() throws RegistrationException {
		try {
			RegistrationConfig cfg = new RegistrationConfig();
			repositoryHandler = new RepositoryHandler(cfg.getSesameEndpoint(),
					cfg.getSesameReposID(), false);
		} catch (IOException e) {
			throw new RegistrationException(
					"There was a problem initializing the repository. Maybe the endpoint or the repository do not exist.",
					e.getCause());
		}
	}
}
