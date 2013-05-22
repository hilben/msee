package at.sti2.msee.discovery.core.common;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.UnsupportedRDFormatException;

/**
 * @author Christian Mayr
 * 
 */
public class DiscoveryQueryBuilderTest {

	DiscoveryQueryBuilder discoveryQueryBuilder = new DiscoveryQueryBuilder();
	private final static Logger LOGGER = LogManager.getLogger(DiscoveryQueryBuilderTest.class
			.getName());

	@BeforeClass
	public static void setup() {
		LOGGER.debug("DiscoveryQueryBuilderTest " + "starting ...");
	}

	@Test
	public void testGetDiscoverQuery2Args() throws URISyntaxException, FileNotFoundException,
			IOException, QueryEvaluationException, RepositoryException, MalformedQueryException,
			RDFHandlerException, UnsupportedRDFormatException {
		String expected = replaceNewline(readFile("/getDiscoverQuery2ArgsTestResult"));
		String[] categoryList = new String[1];
		String query = null;

		// pass
		categoryList[0] = "http://msee.sti2.at/categories#REST_WEB_SERVICE";
		query = discoveryQueryBuilder.getDiscoverQuery2Args(categoryList);
		assertEquals(replaceNewline(query), expected);

		// fail
		categoryList[0] = "http://msee.sti2.at/categories#FAIL";
		query = discoveryQueryBuilder.getDiscoverQuery2Args(categoryList);
		assertNotEquals(replaceNewline(query), expected);
	}

	@Test
	public void testGetDiscoverServicesArrayOneElement() throws IOException {
		String expected = replaceNewline(readFile("/getDiscoverServicesArrayOneElement"));
		String[] categoryList = new String[1];
		String query = null;

		// pass
		categoryList[0] = "http://msee.sti2.at/categories#REST_WEB_SERVICE";
		query = discoveryQueryBuilder.getDiscoverServices(categoryList);
		assertEquals(replaceNewline(query), expected);

		// fail
		categoryList[0] = "http://msee.sti2.at/categories#FAIL";
		query = discoveryQueryBuilder.getDiscoverServices(categoryList);
		assertNotEquals(replaceNewline(query), expected);
	}

	@Test
	public void testGetDiscoverServices() throws IOException {
		String expected = replaceNewline(readFile("/getDiscoverServicesArrayOneElement"));
		String query = null;

		// pass
		String category = "http://msee.sti2.at/categories#REST_WEB_SERVICE";
		query = discoveryQueryBuilder.getDiscoverServices(category);
		assertEquals(replaceNewline(query), expected);

		// fail
		category = "http://msee.sti2.at/categories#FAIL";
		query = discoveryQueryBuilder.getDiscoverServices(category);
		assertNotEquals(replaceNewline(query), expected);
		category = null;
		query = discoveryQueryBuilder.getDiscoverServices(category);
		assertNotEquals(replaceNewline(query), expected);
	}

	@Test
	public void testGetDiscoverServicesArrayMoreElements() throws IOException {
		String expected = replaceNewline(readFile("/getDiscoverServicesArrayMoreElements"));
		String[] categoryList = new String[2];
		String query = null;

		// pass
		categoryList[0] = "http://msee.sti2.at/categories#REST_WEB_SERVICE";
		categoryList[1] = "http://msee.sti2.at/categories#INDESIT_SERVICE";
		query = discoveryQueryBuilder.getDiscoverServices(categoryList);
		assertEquals(replaceNewline(query), expected);

		// fail
		categoryList[0] = "http://msee.sti2.at/categories#FAIL1";
		categoryList[1] = "http://msee.sti2.at/categories#FAIL2";
		query = discoveryQueryBuilder.getDiscoverServices(categoryList);
		assertNotEquals(replaceNewline(query), expected);
	}

	@Test
	public void testGetAllCategoriesQuery() throws IOException {
		String query = discoveryQueryBuilder.getAllCategoriesQuery();
		String expected = replaceNewline(readFile("/getAllCategoriesQuery"));
		Assert.assertEquals(replaceNewline(query), expected);
	}

	@Test
	@Deprecated
	public void testGetDiscoverQuery4Args() throws URISyntaxException, FileNotFoundException,
			IOException, QueryEvaluationException, RepositoryException, MalformedQueryException,
			RDFHandlerException, UnsupportedRDFormatException {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI("http://www.sti2.at/MSEE/ServiceCategories#BUSINESS"));
		List<URI> inputList = new ArrayList<URI>();
		inputList.add(new URI("http://www.sti2.at/MSEE/ServiceCategories#INPUT"));
		List<URI> outputList = new ArrayList<URI>();
		outputList.add(new URI("http://www.sti2.at/MSEE/ServiceCategories#OUTPUT"));
		String query = discoveryQueryBuilder.getDiscoverQuery4Args(categoryList, inputList,
				outputList);
		String expected = readFile("/getDiscoverQuery4ArgsTestResult");
		Assert.assertEquals(query, expected);

		// add a second category
		categoryList.add(new URI("http://www.sti2.at/MSEE/ServiceCategories#MARITIM"));
		query = discoveryQueryBuilder.getDiscoverQuery4Args(categoryList, inputList, outputList);
		String expected2 = replaceNewline(readFile("/getDiscoverQuery4ArgsTestResult-secondCategory"));
		Assert.assertEquals(replaceNewline(query), expected2);
	}

	@Test
	@Deprecated
	public void testGetLookupQuery() throws URISyntaxException, IOException {
		URI namespace = new URI("http://www.sti2.at/MSEE/");
		String operationName = "getData";

		String query = discoveryQueryBuilder.getLookupQuery(namespace, operationName);
		String expected = replaceNewline(readFile("/getLookupQueryTestResult"));
		Assert.assertEquals(replaceNewline(query), expected);
	}

	@Test
	@Deprecated
	public void testGetIServeModelQuery() throws URISyntaxException, IOException,
			QueryEvaluationException, RepositoryException, MalformedQueryException,
			RDFHandlerException, UnsupportedRDFormatException {
		String serviceID = "http://www.theserviceid.com#id";

		String query = discoveryQueryBuilder.getIServeModelQuery(serviceID);
		// System.out.println(query);
		String expected = replaceNewline(readFile("/getIServeModelQueryTestResult"));
		Assert.assertEquals(replaceNewline(query), expected);
	}

	@Test
	public void testGetServiceCount() throws IOException {
		String serviceID = "http://www.theserviceid.com#id";
		String query = discoveryQueryBuilder.getServiceCount(serviceID);
		String expected = replaceNewline(readFile("/getServiceCountQueryTestResult"));
		Assert.assertEquals(replaceNewline(query), expected);
	}

	// @Test
	// public void testGetAllCategoriesForServiceIdQuery() {
	// LOGGER.debug("getAllCategoriesForServiceIdQuery:");
	//
	// //TODO: implement
	// TestCase.fail();
	// }

	private static String readFile(String path) throws IOException {
		Scanner sc = new Scanner(DiscoveryQueryBuilderTest.class.getResourceAsStream(path));
		String text = sc.useDelimiter("\\A").next();
		sc.close();
		return text;
	}

	/**
	 * Replaces end of line symbols for compatibility with windows.
	 * 
	 * @param origin
	 *            - original
	 * @return
	 */
	private static String replaceNewline(String origin) {
		return origin.replaceAll("\\r\\n|\\r|\\n|\\t", "");
	}

}
