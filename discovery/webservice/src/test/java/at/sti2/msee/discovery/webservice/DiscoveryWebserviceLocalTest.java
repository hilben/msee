package at.sti2.msee.discovery.webservice;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import at.sti2.msee.discovery.core.common.DiscoveryConfig;
import at.sti2.msee.registration.core.ServiceRegistrationImpl;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

public class DiscoveryWebserviceLocalTest {
	private static String resourceLocation = "/default.properties";
	private static String webPort = "48080";
	static String webappDirLocation = "src/main/webapp/";
	private static Server server;

	@BeforeClass
	public static void setUp() throws Exception {
		// init Repository
		/*
		 * ServiceRepositoryConfiguration serviceRepositoryConfiguration = new
		 * ServiceRepositoryConfiguration(); ServiceRepository
		 * serviceRepository; DiscoveryConfig config = new DiscoveryConfig();
		 * config.setResourceLocation(resourceLocation);
		 * serviceRepositoryConfiguration
		 * .setRepositoryID(config.getSesameRepositoryID());
		 * serviceRepositoryConfiguration
		 * .setServerEndpoint(config.getSesameEndpoint());
		 * 
		 * serviceRepository =
		 * ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		 * serviceRepository.init(); serviceRepository.clear();
		 * 
		 * String serviceDescriptionURL =
		 * WebServiceDiscoveryImpl.class.getResource(
		 * "/services/HelloService.sawsdl").toString(); ServiceRegistrationImpl
		 * registrationService = new ServiceRegistrationImpl(serviceRepository);
		 * registrationService.register(serviceDescriptionURL);
		 */

		// SERVER
		server = new Server(Integer.parseInt(webPort));

		WebAppContext context = new WebAppContext();
		context.setDescriptor(webappDirLocation + "WEB-INF/web.xml");
		context.setResourceBase(webappDirLocation);
		context.setContextPath("/");
		// context.setClassLoader(DiscoveryWebserviceLocalTest.class.getClassLoader());
		context.setParentLoaderPriority(true);

		server.setHandler(context);

		server.start();
		// server.join();
	}

	@Test
	public void test() {
		String endpoint = "http://localhost:" + webPort + "/services/service";
		Service service = new Service();
		Call call = null;

		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://msee.sti2.at/delivery/discovery/", "discover"));
			call.addParameter("categoryList", org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);

			String returnValue = (String) call
					.invoke(new Object[] { "http://msee.sti2.at/categories#business" });

			String minimalReturnString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"> </rdf:RDF>";

			// System.out.println(returnValue);
			Assert.assertTrue(returnValue.length() >= minimalReturnString.length());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		server.stop();
	}
}
