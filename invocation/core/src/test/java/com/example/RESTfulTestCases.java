package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.services.Hotel;
import com.google.gson.Gson;

/**
 * 
 * This class launches the web application in an embedded Jetty container.
 * 
 */
public class RESTfulTestCases {
	static Logger LOGGER = Logger.getLogger(RESTfulTestCases.class.getName());
	private final String charset = "UTF-8";
	private static Server server;
	private static String webPort;
	private String baseUrl = "http://localhost:" + webPort + "/hotel";
	private static Gson gson = new Gson();

	@BeforeClass
	public static void testServer() throws Exception {
		LogManager.getLogManager().reset(); // to have silent JERSEY logging

		String webappDirLocation = "src/test/resources/";

		webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "48080";
		}

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

	@Test
	public void testGETHotelCount() {
		assertTrue(getHotelCount() > 0);
	}

	private int getHotelCount() {
		String url = baseUrl + "/count";

		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);

		InputStream response = null;
		try {
			client.executeMethod(method);
			response = method.getResponseBodyAsStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String output = convertStreamToString(response);
		int count = new Integer(gson.fromJson(output, String.class));

		method.releaseConnection();
		return count;
	}

	@Test
	public void testGET() throws MalformedURLException, IOException {
		String url = baseUrl;
		String id = "1";

		String path = String.format("/%s", URLEncoder.encode(id, charset));

		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url + path);

		client.executeMethod(method);
		InputStream response = method.getResponseBodyAsStream();

		String output = convertStreamToString(response);
		Hotel retrievedHotel = gson.fromJson(output, Hotel.class);

		assertEquals(id, retrievedHotel.getId());

		method.releaseConnection();

	}

	@Test
	public void testGETdetails() throws MalformedURLException, IOException {
		String url = baseUrl;
		String id = "1";

		String path = String.format("/%s/details", URLEncoder.encode(id, charset));

		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url + path);

		client.executeMethod(method);
		InputStream response = method.getResponseBodyAsStream();

		String output = convertStreamToString(response);
		String details = gson.fromJson(output, String.class);

		String expected = "Further hotel details for Hotel" + id + ".";
		assertEquals(expected, details);

		method.releaseConnection();
	}

	@Test
	public void testPOST() throws MalformedURLException, IOException {
		int initialCount = getHotelCount();
		String url = baseUrl;

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		Hotel newHotel = new Hotel();

		NameValuePair[] data = { new NameValuePair("hotel", gson.toJson(newHotel)) };
		method.setRequestBody(data);
		client.executeMethod(method);

		InputStream response = method.getResponseBodyAsStream();
		String outputString = convertStreamToString(response);

		Hotel retrievedHotel = gson.fromJson(outputString, Hotel.class);
		assertNotNull(retrievedHotel);
		assertTrue(new Integer(retrievedHotel.getId()) > 10);

		method.releaseConnection();

		assertTrue(++initialCount == getHotelCount());
	}

	@Test
	public void testPUTupdate() throws MalformedURLException, IOException {
		String url = baseUrl;
		String id = "1";
		String path = String.format("/%s", URLEncoder.encode(id, charset));

		HttpClient client = new HttpClient();
		PutMethod method = new PutMethod(url + path);
		Hotel updatedHotel = new Hotel();
		updatedHotel.setId(id);
		String newName = "New 5 Star";
		updatedHotel.setName(newName);

		RequestEntity data = new StringRequestEntity(gson.toJson(updatedHotel), "application/json",
				charset);
		method.setRequestEntity(data);
		client.executeMethod(method);

		InputStream response = method.getResponseBodyAsStream();
		String outputString = convertStreamToString(response);

		Hotel retrievedHotel = gson.fromJson(outputString, Hotel.class);
		assertNotNull(retrievedHotel);
		assertTrue(retrievedHotel.getName().equals(newName));

		assertEquals(Status.OK.getStatusCode(), method.getStatusCode());

		method.releaseConnection();

	}

	@Test
	public void testPUTcreate() throws MalformedURLException, IOException {
		String url = baseUrl;
		String id = "11111111111";
		String path = String.format("/%s", URLEncoder.encode(id, charset));

		HttpClient client = new HttpClient();
		PutMethod method = new PutMethod(url + path);
		Hotel updatedHotel = new Hotel();
		updatedHotel.setId(id);
		String newName = "New 5 Star";
		updatedHotel.setName(newName);

		RequestEntity data = new StringRequestEntity(gson.toJson(updatedHotel), "application/json",
				charset);
		method.setRequestEntity(data);
		client.executeMethod(method);

		InputStream response = method.getResponseBodyAsStream();
		String outputString = convertStreamToString(response);

		Hotel retrievedHotel = gson.fromJson(outputString, Hotel.class);

		assertNotNull(retrievedHotel);
		assertTrue(retrievedHotel.getName().equals(newName));
		assertEquals(Status.CREATED.getStatusCode(), method.getStatusCode());

		method.releaseConnection();

	}

	@Test
	public void testDELETE() throws MalformedURLException, IOException {
		int initialCount = getHotelCount();
		String url = baseUrl;
		String id = "1";
		String path = String.format("/%s", URLEncoder.encode(id, charset));

		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(url + path);

		client.executeMethod(method);

		InputStream response = method.getResponseBodyAsStream();
		String outputString = convertStreamToString(response);

		Hotel retrievedHotel = gson.fromJson(outputString, Hotel.class);

		assertNotNull(retrievedHotel);
		assertEquals(Status.OK.getStatusCode(), method.getStatusCode());
		assertTrue(--initialCount == getHotelCount());

		method.releaseConnection();
	}

	@Test
	public void testDELETEnotFound() throws MalformedURLException, IOException {
		int initialCount = getHotelCount();
		String url = baseUrl;
		String id = "11111111111111112";
		String path = String.format("/%s", URLEncoder.encode(id, charset));

		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(url + path);

		client.executeMethod(method);

		InputStream response = method.getResponseBodyAsStream();
		String outputString = convertStreamToString(response);

		Hotel retrievedHotelNULL = gson.fromJson(outputString, Hotel.class);

		assertNull(retrievedHotelNULL);
		assertEquals(Status.NOT_FOUND.getStatusCode(), method.getStatusCode());
		assertTrue(initialCount == getHotelCount());

		method.releaseConnection();
	}

	private static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is);
		s.useDelimiter("\\A");
		String retval = s.hasNext() ? s.next() : "";
		s.close();
		return retval;
	}

}
