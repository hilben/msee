package at.sti2.msee.registration.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;

import at.sti2.msee.config.Config;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

public class ServiceRegistrationImplTest {

	ServiceRepository serviceRepository;

	@Before
	public void setUp() throws Exception {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();

		// Comment these 2 lines to force a in-memory repository
		// serviceRepositoryConfiguration.setRepositoryID(Config.INSTANCE.getRepositoryID());
		// serviceRepositoryConfiguration.setServerEndpoint(Config.INSTANCE.getRepositoryEndpoint());

		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();
		serviceRepository.clear();
	}

	@After
	public void tearDown() throws Exception {
		serviceRepository.shutdown();
	}

	@Test
	public void test() throws ServiceRegistrationException {
		String serviceDescriptionURL = "http://chrismayrdp11.herokuapp.com/wsdl/Indesit_companysite.wsdl.xml";
		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);
		registration.register(serviceDescriptionURL);
		
		serviceDescriptionURL = "http://chrismayrdp11.herokuapp.com/wsdl/Indesit_rest.wsdl.xml";
		registration = new ServiceRegistrationImpl(serviceRepository);
		registration.register(serviceDescriptionURL);
		
		serviceDescriptionURL = "http://chrismayrdp11.herokuapp.com/wsdl/Indesit_webapp.wsdl.xml";
		registration = new ServiceRegistrationImpl(serviceRepository);
		registration.register(serviceDescriptionURL);
	}

	@Test
	public void testRegister_SAWSDL_WithOneCategory() throws ServiceRegistrationException,
			RepositoryException {

		// Repo is empty
		assertEquals(0, this.getNumberOfServices());

		String serviceDescriptionURL = this.getClass()
				.getResource("/webservices/sawsdl/HelloService.sawsdl").toString();
		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);

		String serviceURI = registration.register(serviceDescriptionURL);
		assertNotNull(serviceURI);

		// Service is in repository
		assertEquals(1, this.getNumberOfServices());
	}

	@Test
	public void testRegister_SAWSDL_WithOneCategory_EmptyContext()
			throws ServiceRegistrationException, RepositoryException {
		// Repo is empty
		assertEquals(0, getNumberOfServices());

		String serviceDescriptionURL = this.getClass()
				.getResource("/webservices/sawsdl/HelloService.sawsdl").toString();
		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);

		String serviceURI = registration.register(serviceDescriptionURL);
		assertNotNull(serviceURI);

		// Service is in repository
		assertEquals(1, getNumberOfServices());
	}

	@Test
	public void testRegister_SAWSDL_SeveralServices() throws ServiceRegistrationException,
			RepositoryException {
		// Repo is empty
		assertEquals(0, this.getNumberOfServices());

		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);

		String serviceDescriptionURL = this.getClass()
				.getResource("/webservices/sawsdl/HelloService.sawsdl").toString();
		String serviceURI = registration.register(serviceDescriptionURL);
		assertNotNull(serviceURI);

		serviceDescriptionURL = this.getClass()
				.getResource("/webservices/sawsdl/HelloService2.sawsdl").toString();
		serviceURI = registration.register(serviceDescriptionURL);
		assertNotNull(serviceURI);

		// Service is in repository
		assertEquals(2, this.getNumberOfServices());
	}

	@Test
	public void testRegisterHrests() throws ServiceRegistrationException, RepositoryException {
		assertEquals(0, this.getNumberOfServices());

		ServiceRegistrationImpl registration = new ServiceRegistrationImpl(serviceRepository);

		String serviceDescriptionURL = this.getClass().getResource("/formats/hrests1.html")
				.toString();
		String serviceURI = registration.register(serviceDescriptionURL);
		assertNotNull(serviceURI);

		serviceDescriptionURL = this.getClass().getResource("/formats/hrests1.html").toString();
		serviceURI = registration.register(serviceDescriptionURL);

		assertNotNull(serviceURI);

		// Service is in repository
		assertEquals(2, this.getNumberOfServices());
	}

	// @Test
	// public void testRegisterIndesitM18Services() throws
	// ServiceRegistrationException
	// {
	// String contextURI = null;
	// //Repo is empty
	// assertEquals(0, this.getNumberOfServices(contextURI));
	//
	// ServiceRegistrationImpl registration = new
	// ServiceRegistrationImpl(serviceRepository);
	//
	// String serviceDescriptionURL =
	// this.getClass().getResource("/webservices/M18Indesit/Indesit_companysite.sawsdl").toString();
	// String serviceURI = registration.registerInContext(serviceDescriptionURL,
	// contextURI);
	// assertNotNull(serviceURI);
	//
	// //Service is in repository
	// assertEquals(2,this.getNumberOfServices(contextURI));
	// }

	private int getNumberOfServices() {
		Model repositoryModel = serviceRepository.getModel(null);
		int size = 0;

		repositoryModel.open();
		String query = "select * WHERE {?s " + RDF.type.toSPARQL() + " " + MSM.Service.toSPARQL()
				+ " }";
		QueryResultTable statementsTable = repositoryModel.sparqlSelect(query);
		ClosableIterator<QueryRow> statements = statementsTable.iterator();

		while (statements.hasNext()) {
			statements.next();
			size++;
		}
		statements.close();
		repositoryModel.close();

		return size;
	}

}
