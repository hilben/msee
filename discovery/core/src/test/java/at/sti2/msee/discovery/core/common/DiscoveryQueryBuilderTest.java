package at.sti2.msee.discovery.core.common;

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
	private final static Logger LOGGER = LogManager
			.getLogger(DiscoveryQueryBuilderTest.class.getName());
	
	@BeforeClass
	public static void setup(){
		LOGGER.debug("DiscoveryQueryBuilderTest "+"starting ...");
	}

	@Test
	public void testGetDiscoverQuery2Args() throws URISyntaxException,
			FileNotFoundException, IOException, QueryEvaluationException,
			RepositoryException, MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI(
				"http://msee.sti2.at/categories#REST_WEB_SERVICE"));
		String query = discoveryQueryBuilder
				.getDiscoverQuery2Args(categoryList);
		String expected = replaceNewline(readFile("/getDiscoverQuery2ArgsTestResult"));
		Assert.assertEquals(replaceNewline(query), expected);
	}

	@Test
	public void testGetDiscoverQuery4Args() throws URISyntaxException,
			FileNotFoundException, IOException, QueryEvaluationException,
			RepositoryException, MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#BUSINESS"));
		List<URI> inputList = new ArrayList<URI>();
		inputList
				.add(new URI("http://www.sti2.at/MSEE/ServiceCategories#INPUT"));
		List<URI> outputList = new ArrayList<URI>();
		outputList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#OUTPUT"));
		String query = discoveryQueryBuilder.getDiscoverQuery4Args(
				categoryList, inputList, outputList);
		String expected = readFile("/getDiscoverQuery4ArgsTestResult");
		Assert.assertEquals(query, expected);

		// add a second category
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#MARITIM"));
		query = discoveryQueryBuilder.getDiscoverQuery4Args(categoryList,
				inputList, outputList);
		String expected2 = replaceNewline(readFile("/getDiscoverQuery4ArgsTestResult-secondCategory"));
		Assert.assertEquals(replaceNewline(query), expected2);
	}

	@Test
	public void testGetLookupQuery() throws URISyntaxException, IOException {
		URI namespace = new URI("http://www.sti2.at/MSEE/");
		String operationName = "getData";

		String query = discoveryQueryBuilder.getLookupQuery(namespace,
				operationName);
		String expected = replaceNewline(readFile("/getLookupQueryTestResult"));
		Assert.assertEquals(replaceNewline(query), expected);
	}

	@Test
	public void testGetIServeModelQuery() throws URISyntaxException,
			IOException, QueryEvaluationException, RepositoryException,
			MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {
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

	@Test
	public void testGetAllCategoriesQuery() throws IOException {
		String query = discoveryQueryBuilder.getAllCategoriesQuery();
		String expected = replaceNewline(readFile("/getAllCategoriesQuery"));
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
		Scanner sc = new Scanner(
				DiscoveryQueryBuilderTest.class.getResourceAsStream(path));
		String text = sc.useDelimiter("\\A").next();
		sc.close();
		return text;
	}
	
	/**
	 * Replaces end of line symbols for compatibility with windows.
	 * @param origin - original 
	 * @return
	 */
	private static String replaceNewline(String origin){
		return origin.replaceAll("\\r\\n|\\r|\\n", "");
	}

}
