package at.sti2.msee.invocation.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.LogManager;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.services.Hotel;
import com.google.gson.Gson;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.invocation.core.common.InvokerREST;
import at.sti2.msee.invocation.core.common.Parameter;

public class ServiceInvokerImplRestTest {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private InvokerREST invoker = new InvokerREST(null);
	private static Server server;
	private static String webPort = "48080";
	Gson gson = new Gson();

	@Rule
	public ExpectedException exception = ExpectedException.none();

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
		// server.join();
	}

	@AfterClass
	public static void shutdownServer() throws Exception {
		server.stop();
	}

	@Before
	public void setup() {
		// Assert.assertNotNull(invoker.getMonitoring());
	}

	@Test
	public void testGET() throws ServiceInvokerException, MalformedURLException {
		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/{id}";
		String method = "GET";
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("id", "5"));
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		Assert.assertThat(result, is("{\"id\":\"5\",\"name\":\"The standard name of Hotel5\"}"));
	}

	@Test
	public void testGETfail() throws ServiceInvokerException, MalformedURLException {
		exception.expect(ServiceInvokerException.class);
		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort
				+ "/hotel/hotel/{id}";
		String method = "GET";
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("id", "5"));
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		Assert.assertThat(result, is("{\"id\":\"5\",\"name\":\"The standard name of Hotel5\"}"));
	}

	@Test
	public void testGETdetails() throws MalformedURLException, IOException, ServiceInvokerException {
		int id = 4;
		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort
				+ "/hotel/{id}/details";
		String method = "GET";
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("id", Integer.toString(id)));
		String result = invoker.invokeREST(serviceID, address, method, parameters);

		String expected = "\"Further hotel details for Hotel" + id + ".\"";
		String notExpected = "\"Further hotel details for Hotel" + 5 + ".\"";
		assertThat(expected, is(result));
		assertThat(notExpected, is(not(result)));
	}

	@Test
	public void testGETHotelCount() throws MalformedURLException, ServiceInvokerException {
		assertTrue(getHotelCount() > 0);
	}

	private int getHotelCount() throws ServiceInvokerException, MalformedURLException {
		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/count";
		String method = "GET";
		List<Parameter> parameters = new ArrayList<>();
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		return new Integer(result);
	}

	@Test
	public void testPOST() throws ServiceInvokerException, MalformedURLException {
		int count = getHotelCount();
		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/";
		String method = "POST";
		List<Parameter> parameters = new ArrayList<>();
		Hotel newHotel = new Hotel();
		parameters.add(new Parameter("hotel", gson.toJson(newHotel)));
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		assertThat(result, is("{\"id\":\"11\"}"));
		assertThat(count, is(equalTo(getHotelCount() - 1)));
	}

	@Test
	public void testPOSTfail() throws ServiceInvokerException, MalformedURLException {
		exception.expect(ServiceInvokerException.class);
		int count = getHotelCount();
		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/hotel";
		String method = "POST";
		List<Parameter> parameters = new ArrayList<>();
		Hotel newHotel = new Hotel();
		parameters.add(new Parameter("hotel", gson.toJson(newHotel)));
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		assertThat(result, is("{\"id\":\"11\"}"));
		assertThat(count, is(equalTo(getHotelCount() - 1)));
	}

	@Test
	public void testPUTupdate() throws MalformedURLException, IOException, ServiceInvokerException {
		int initialCount = getHotelCount();
		int id = 2;
		Hotel updatedHotel = new Hotel();
		updatedHotel.setId(Integer.toString(id));
		String newName = "New 5 Star";
		updatedHotel.setName(newName);

		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/{id}";
		String method = "PUT";
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("id", Integer.toString(id)));
		parameters.add(new Parameter("data", gson.toJson(updatedHotel)));
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		assertThat(result, is("{\"id\":\"" + id + "\",\"name\":\"" + newName + "\"}"));
		assertThat(getHotelCount(), is(initialCount)); // must not change

		Hotel retrievedHotel = gson.fromJson(result, Hotel.class);
		assertNotNull(retrievedHotel);
	}

	@Test
	public void testPUTcreate() throws MalformedURLException, IOException, ServiceInvokerException {
		int initialCount = getHotelCount();
		int id = 12312321;
		Hotel updatedHotel = new Hotel();
		updatedHotel.setId(Integer.toString(id));
		String newName = "New 5 Star";
		updatedHotel.setName(newName);

		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/{id}";
		String method = "PUT";
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("id", Integer.toString(id)));
		parameters.add(new Parameter("data", gson.toJson(updatedHotel)));
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		assertThat(result, is("{\"id\":\"" + id + "\",\"name\":\"" + newName + "\"}"));
		assertThat(getHotelCount(), is(initialCount + 1));

		Hotel retrievedHotel = gson.fromJson(result, Hotel.class);
		assertNotNull(retrievedHotel);
	}

	@Test
	public void testDELETE() throws MalformedURLException, IOException, ServiceInvokerException {
		int initialCount = getHotelCount();
		int id = 1;

		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/{id}";
		String method = "DELETE";
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("id", Integer.toString(id)));
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		assertThat(result, is("{\"id\":\"1\",\"name\":\"The standard name of Hotel1\"}"));
		assertThat(getHotelCount(), is(initialCount - 1));

		Hotel retrievedHotel = gson.fromJson(result, Hotel.class);
		assertNotNull(retrievedHotel);
	}

	@Test
	public void testDELETEnotFound() throws MalformedURLException, IOException,
			ServiceInvokerException {
		int initialCount = getHotelCount();
		int id = 1111234;

		URL serviceID = new URL("http://" + UUID.randomUUID());
		String address = "http://localhost:" + ServiceInvokerImplRestTest.webPort + "/hotel/{id}";
		String method = "DELETE";
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("id", Integer.toString(id)));
		String result = invoker.invokeREST(serviceID, address, method, parameters);
		assertThat(result, either(is(nullValue(String.class))).or(is("")));
		assertThat(getHotelCount(), is(initialCount));

		Hotel retrievedHotel = gson.fromJson(result, Hotel.class);
		assertThat(retrievedHotel, is(nullValue()));
	}

}
