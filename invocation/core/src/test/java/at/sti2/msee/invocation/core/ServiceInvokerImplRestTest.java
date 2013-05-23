package at.sti2.msee.invocation.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.LogManager;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

public class ServiceInvokerImplRestTest {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private ServiceInvocationImpl invoker = new ServiceInvocationImpl();
	private static Server server;
	private static String webPort = "48080";

	@BeforeClass
	public static void setupServer() throws Exception {
		LogManager.getLogManager().reset(); // to have silent JERSEY logging

		String webappDirLocation = "src/test/resources/";

		server = new Server(Integer.valueOf(webPort));
		WebAppContext root = new WebAppContext();

		root.setContextPath("/");
		root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);

		root.setParentLoaderPriority(true);

		server.setHandler(root);

		server.start();
		//server.join();
	}

	@AfterClass
	public static void shutdownServer() throws Exception {
		//server.stop();
	}

	@Before
	public void setup() {
		Assert.assertNotNull(invoker.getMonitoring());
	}

	@Test
	public void testGET() throws ServiceInvokerException, MalformedURLException {
		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/{id}";
		String method = "GET";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", "5");
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		Assert.assertThat(result, is("{\"id\":\"5\",\"name\":\"The standard name of Hotel5\"}"));
	}
}
