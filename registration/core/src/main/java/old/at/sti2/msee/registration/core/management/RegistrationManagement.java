//package old.at.sti2.msee.registration.core.management;
//
//import java.io.IOException;
//
//import old.at.sti2.msee.registration.core.common.RegistrationConfig;
//
//import org.openrdf.repository.RepositoryException;
//
//import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
//import at.sti2.util.triplestore.RepositoryHandler;
//
//public abstract class RegistrationManagement {
//	private static RepositoryHandler repositoryHandler;
//
//	/**
//	 * Delete all context associated with a service URI
//	 * 
//	 * @param serviceURI
//	 * @return
//	 * @throws ServiceRegistrationException
//	 */
//	public static String delete(String serviceURI) throws ServiceRegistrationException {
//
//		RegistrationManagement.initRepo();
//		try {
//			repositoryHandler.deleteContext(serviceURI);
//			repositoryHandler.commit();
//			repositoryHandler.shutdown();
//
//			return serviceURI;
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * Update a web service by deleting it and then reuploading it
//	 * 
//	 * @param oldServiceURI
//	 * @param newServiceURI
//	 * @return
//	 * @throws ServiceRegistrationException
//	 */
//	public static String update(String oldServiceURI, String newServiceURI)
//			throws ServiceRegistrationException {
//		// Initialising the repository
//		RegistrationManagement.initRepo();
//		RegistrationManagement.delete(oldServiceURI);
//		
//		RegistrationWSDLToTriplestoreWriter registration = new RegistrationWSDLToTriplestoreWriter();
//		String registredServiceURI = registration
//				.transformWSDLtoTriplesAndStoreInTripleStore(newServiceURI);
//
//		return registredServiceURI;
//	}
//
//	/**
//	 * Initialising the repository needed for all the above methods.
//	 * 
//	 * @throws ServiceRegistrationException
//	 */
//	private static void initRepo() throws ServiceRegistrationException {
//		try {
//			RegistrationConfig cfg = new RegistrationConfig();
//			repositoryHandler = new RepositoryHandler(cfg.getSesameEndpoint(),
//					cfg.getSesameReposID(), false);
//		} catch (IOException e) {
//			throw new ServiceRegistrationException(
//					"There was a problem initializing the repository. Maybe the endpoint or the repository do not exist.",
//					e.getCause());
//		}
//	}
//}
