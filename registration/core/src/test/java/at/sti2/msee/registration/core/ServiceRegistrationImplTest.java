package at.sti2.msee.registration.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.URI;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;
import at.sti2.msee.triplestore.query.ModelQueryHelper;

public class ServiceRegistrationImplTest{

	ServiceRepository serviceRepository;
	
	@Before
	public void setUp() throws Exception {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		
		//Comment these 2 lines to force a in-memory repository
		serviceRepositoryConfiguration.setRepositoryID("msee-test");
		serviceRepositoryConfiguration.setServerEndpoint("http://sesa.sti2.at:8080/openrdf-sesame");
	
		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();
		serviceRepository.clear();
	}

	@After
	public void tearDown() throws Exception {
		serviceRepository.shutdown();
	}
		
	@Test

	public void testRegister_SAWSDL_WithOneCategory() throws ServiceRegistrationException, RepositoryException {
		String contextURI = "http://msee.sti2.at/msee";

		//Repo is empty
		assertEquals(0, this.getNumberOfServices(contextURI));
		
		String serviceDescriptionURL = this.getClass().getResource("/webservices/sawsdl/HelloService.sawsdl").toString();	
		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);
		
		String serviceURI = registration.registerInContext(serviceDescriptionURL, contextURI);
		assertNotNull(serviceURI);
		
		//Service is in repository
		assertEquals(1,this.getNumberOfServices(contextURI));
	}

	@Test
	public void testRegister_SAWSDL_WithOneCategory_EmptyContext() throws ServiceRegistrationException, RepositoryException {
		String contextURI = null;
		//Repo is empty
		assertEquals(0, this.getNumberOfServices(contextURI));
		
		String serviceDescriptionURL = this.getClass().getResource("/webservices/sawsdl/HelloService.sawsdl").toString();	
		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);
		
		String serviceURI = registration.registerInContext(serviceDescriptionURL, contextURI);
		assertNotNull(serviceURI);
		
		//Service is in repository
		assertEquals(1,this.getNumberOfServices(contextURI));
	}
	
	@Test
	public void testRegister_SAWSDL_SeveralServices() throws ServiceRegistrationException, RepositoryException {
		String contextURI = null;
		//Repo is empty
		assertEquals(0, this.getNumberOfServices(contextURI));

		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);

		String serviceDescriptionURL = this.getClass().getResource("/webservices/sawsdl/HelloService.sawsdl").toString();		
		String serviceURI = registration.registerInContext(serviceDescriptionURL, contextURI);
		assertNotNull(serviceURI);

		serviceDescriptionURL = this.getClass().getResource("/webservices/sawsdl/HelloService2.sawsdl").toString();		
		serviceURI = registration.registerInContext(serviceDescriptionURL, contextURI);
		assertNotNull(serviceURI);
		
		//Service is in repository
		assertEquals(2,this.getNumberOfServices(contextURI));
	}
	
//	@Test
//	public void testRegisterIndesitM18Services() throws ServiceRegistrationException
//	{
//		String contextURI = null;
//		//Repo is empty
//		assertEquals(0, this.getNumberOfServices(contextURI));
//
//		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);
//
//		String serviceDescriptionURL = this.getClass().getResource("/webservices/M18Indesit/Indesit_companysite.sawsdl").toString();		
//		String serviceURI = registration.registerInContext(serviceDescriptionURL, contextURI);
//		assertNotNull(serviceURI);
//		
//		//Service is in repository
//		assertEquals(2,this.getNumberOfServices(contextURI));		
//	}

	private int getNumberOfServices(String contextURI){
		Model repositoryModel = serviceRepository.getModel(contextURI);
		repositoryModel.open();
		List<URI> services = ModelQueryHelper.findServices(repositoryModel);
		repositoryModel.close();		
		return services.size();
	}
		
//	@Testv
//	public void testDeregister() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdate() {
//		fail("Not yet implemented");
//	}

}
