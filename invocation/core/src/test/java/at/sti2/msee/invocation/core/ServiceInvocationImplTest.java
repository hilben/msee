package at.sti2.msee.invocation.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.ServiceDiscoveryFactory;
import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.ServiceRegistrationImpl;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

public class ServiceInvocationImplTest {
	private static ServiceInvocationImpl invocation = null;
	private static String registeredServiceID1 = null;
	private static String registeredServiceID2 = null;
	private static String registeredServiceID3 = null;
	private static ServiceRepository serviceRepository;
	private static String registeredServiceID4 = null;

	@BeforeClass
	public static void setup() throws RepositoryException, ServiceRegistrationException,
			MalformedURLException {

		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		serviceRepositoryConfiguration.setRepositoryID("msee-test");
		serviceRepositoryConfiguration.setServerEndpoint("http://msee.sti2.at:8080/openrdf-sesame");
		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();
		serviceRepository.clear();

		invocation = new ServiceInvocationImpl(serviceRepository);

		// register some stuff
		String serviceDescriptionURL = ServiceInvocationImplTest.class.getResource(
				"/services/hotelapp.html").toString();
		ServiceRegistrationImpl registrationService = new ServiceRegistrationImpl(serviceRepository);
		registeredServiceID1 = registrationService.register(serviceDescriptionURL);

		serviceDescriptionURL = ServiceInvocationImplTest.class.getResource(
				"/services/hotelapp.wsdl").toString();
		registeredServiceID3 = registrationService.register(serviceDescriptionURL);

		serviceDescriptionURL = ServiceInvocationImplTest.class.getResource(
				"/services/hotelwsdl2.0-2013-06-11.wsdl").toString();
		registeredServiceID2 = registrationService.register(serviceDescriptionURL);

	}

	@Test
	public final void testDiscover() throws DiscoveryException {
		Discovery discovery = ServiceDiscoveryFactory.createDiscoveryService(serviceRepository);
		String result = discovery
				.discover(new String[] { "http://msee.sti2.at/categories#business" });
		Assert.assertTrue("Is everything registrated?", result.length() > 1000);
	}

	@Test
	public final void testInvokeREST() throws MalformedURLException, ServiceInvokerException {
		String id = "2";
		String expected = "Hotel Name" + id;
		String operation = "getHotelName";
		String inputVariables = "<parameters>" + "<id>" + id + "</id>" + "</parameters>";
		String result = invocation.invoke(new URL(registeredServiceID1), operation, inputVariables);
		Assert.assertEquals("Result is: " + result, expected, result);
	}

	@Test
	public final void testInvokeWSDL() throws MalformedURLException, ServiceInvokerException {
		String hotelID = "1";
		String expected = "Street Road 312" + hotelID;
		String operation = "getHotelAddress";
		String inputVariables = "<parameters>" + "<hotelID>" + hotelID + "</hotelID>" + "</parameters>";
		String result = invocation.invoke(new URL(registeredServiceID2), operation, inputVariables);
		Assert.assertEquals("Result is: " + result, expected, result);
	}

	@Test
	public final void testInvokeRESTfulInWSDLFormat() throws MalformedURLException,
			ServiceInvokerException {
		String id = "4";
		String expected = "Hotel Name" + id;
		String operation = "getHotelName";
		String inputVariables = "<parameters>" + "<id>" + id + "</id>" + "</parameters>";
		String result = invocation.invoke(new URL(registeredServiceID3), operation, inputVariables);
		Assert.assertEquals("Result is: " + result, expected, result);
	}

}
