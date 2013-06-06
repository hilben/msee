package at.sti2.msee.discovery.webservice;

import java.io.FileInputStream;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.deployment.DeploymentEngine;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.http.SimpleHTTPServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import at.sti2.msee.discovery.core.common.DiscoveryConfig;
import at.sti2.msee.registration.core.ServiceRegistrationImpl;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

public class DiscoveryWebserviceLocalTest {

	static Logger LOGGER = Logger.getLogger(DiscoveryWebserviceLocalTest.class.getName());
	private static SimpleHTTPServer server;
	private static String webPort = "48080";
	static String webappDirLocation = "src/main/webapp/";
	private static String resourceLocation = "/default.properties";

	@BeforeClass
	public static void testServer() throws Exception {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		ServiceRepository serviceRepository;
		DiscoveryConfig config = new DiscoveryConfig();
		config.setResourceLocation(resourceLocation);
		serviceRepositoryConfiguration.setRepositoryID(config.getSesameRepositoryID());
		serviceRepositoryConfiguration.setServerEndpoint(config.getSesameEndpoint());

		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();
		serviceRepository.clear();

		String serviceDescriptionURL = WebServiceDiscoveryImpl.class.getResource(
				"/services/HelloService.sawsdl").toString();
		ServiceRegistrationImpl registrationService = new ServiceRegistrationImpl(serviceRepository);
		registrationService.register(serviceDescriptionURL);

		ConfigurationContext context;
		context = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null,
				webappDirLocation + "WEB-INF/conf/axis2.xml");
		int serverPort = Integer.valueOf(webPort);
		server = new SimpleHTTPServer(context, serverPort);

		AxisService service = DeploymentEngine.buildService(new FileInputStream(webappDirLocation
				+ "WEB-INF/services/discovery/META-INF/services.xml"), context);
		AxisConfiguration xConfig = context.getAxisConfiguration();
		xConfig.addService(service);
		server.start();
	}

	@AfterClass
	public static void shutdownServer() throws Exception {
		server.stop();
	}

	@Test
	public void test() {
		String endpoint = "http://localhost:" + webPort + "/axis2/services/discovery";
		Service service = new Service();
		Call call = null;

		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://sesa.sti2.at/services/", "discover"));
			call.addParameter("categoryList", org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);

			String returnValue = (String) call
					.invoke(new Object[] { "http://msee.sti2.at/categories#business" });

			// writeToFile("returnDiscoverTest.txt", returnValue);

			String minimalReturnString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:sawsdl=\"http://www.w3.org/ns/sawsdl#\" xmlns:msm_ext=\"http://sesa.sti2.at/ns/minimal-service-model-ext#\" xmlns:wsdl=\"http://www.w3.org/ns/wsdl-rdf#\"> </rdf:RDF>";
			// System.out.println(returnValue);
			Assert.assertTrue(returnValue.length() >= minimalReturnString.length());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
