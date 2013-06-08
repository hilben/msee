package at.sti2.msee.invocation.core;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
	private static String registeredServiceID = null;
	private static ServiceRepository serviceRepository;

	@BeforeClass
	public static void setup() throws RepositoryException, ServiceRegistrationException,
			MalformedURLException {

		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		serviceRepositoryConfiguration.setRepositoryID("msee");
		serviceRepositoryConfiguration.setServerEndpoint("http://msee.sti2.at:8080/openrdf-sesame");
		serviceRepository = ServiceRepositoryFactory.newInstance(serviceRepositoryConfiguration);
		serviceRepository.init();
		serviceRepository.clear();

		invocation = new ServiceInvocationImpl(serviceRepository);

		// register some stuff
		String serviceDescriptionURL = ServiceInvocationImplTest.class.getResource(
				"/services/hrests1.html").toString();
		ServiceRegistrationImpl registrationService = new ServiceRegistrationImpl(serviceRepository);
		// registeredServiceID =
		// registrationService.register(serviceDescriptionURL);

		serviceDescriptionURL = ServiceInvocationImplTest.class.getResource(
				"/services/discovery.wsdl").toString();
		registeredServiceID = registrationService.register(serviceDescriptionURL);

	}

	@Test
	@Ignore
	public final void testDiscover() throws DiscoveryException {
		Discovery discovery = ServiceDiscoveryFactory.createDiscoveryService(serviceRepository);
		String result = discovery
				.discover(new String[] { "http://msee.sti2.at/categories#business" });
		System.out.println(result);
	}

	@Test
	public final void testInvoke() throws MalformedURLException, ServiceInvokerException {
		Map<String, String> inputVariablesMap = new HashMap<String, String>();
		inputVariablesMap.put("categoryList", Arrays.toString(new String[]{"http://msee.sti2.at/categories#business"}));
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(output);
		encoder.writeObject(inputVariablesMap);
		encoder.flush();
		encoder.close();
		String inputVariables = new String(output.toByteArray());
		String result = invocation.invoke(new URL(registeredServiceID), "discover", inputVariables);
		System.out.println(result);

		// PostMethod postHandler = new
		// PostMethod("http://192.168.65.147:8080/at.sti2.msee.delivery.discovery.webservice-m17.1-SNAPSHOT/services/discovery");
		// InputStream postResponse = new ByteArrayInputStream("".getBytes());
		// try {
		// postHandler.setRequestBody("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:msee=\"http://sesa.sti2.at/services/\" ><soapenv:Header/>"
		// + "<soapenv:Body><msee:discover><categoryList>"
		// + "http://msee.sti2.at/categories#business"
		// + "</categoryList></msee:discover></soapenv:Body>"
		// + "</soapenv:Envelope>");
		// HttpClient client = new HttpClient();
		// client.executeMethod(postHandler);
		// postResponse = postHandler.getResponseBodyAsStream();
		// String output = convertStreamToString(postResponse);
		// System.out.println(output);
		// } catch (IOException e) {
		// throw new ServiceInvokerException(e);
		// } finally {
		// postHandler.releaseConnection();
		// }
	}

	// private static String convertStreamToString(java.io.InputStream is) {
	// java.util.Scanner s = new java.util.Scanner(is);
	// s.useDelimiter("\\A");
	// String retval = s.hasNext() ? s.next() : "";
	// s.close();
	// return retval;
	// }

}
