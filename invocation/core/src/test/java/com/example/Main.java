package com.example;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

/**
 * 
 * This class launches the web application in an embedded Jetty container.
 * 
 */
public class Main {
	static Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static Server server;
	private static String webPort;
	private static Gson gson = new Gson();

	@BeforeClass
	public static void testServer() throws Exception {
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
		//server.join();
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
		String url = "http://localhost:" + webPort + "/services/hotel/count";

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
		String url = "http://localhost:" + webPort + "/services/hotel";
		String charset = "UTF-8";
		String id = "1";

		String path = String.format("%s", URLEncoder.encode(id, charset));

		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url + "/" + path);

		client.executeMethod(method);
		InputStream response = method.getResponseBodyAsStream();

		String output = convertStreamToString(response);
		Hotel retrievedHotel = gson.fromJson(output, Hotel.class);

		assertEquals(id, retrievedHotel.getId());

		method.releaseConnection();

	}

	@Test
	public void testPOST() throws MalformedURLException, IOException {
		int initialCount = getHotelCount();
		String url = "http://localhost:" + webPort + "/services/hotel";

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

	private static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is);
		s.useDelimiter("\\A");
		String retval = s.hasNext() ? s.next() : "";
		s.close();
		return retval;
	}

}
