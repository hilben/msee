package at.sti2.msee.discovery.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.UnsupportedRDFormatException;

import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilder;

/**
 * @author Christian Mayr
 * 
 */
public class DiscoveryQueryBuilderTest {

	DiscoveryQueryBuilder discoveryQueryBuilder = new DiscoveryQueryBuilder();

	@Test
	public void testGetDiscoverQuery2Args() throws URISyntaxException,
			FileNotFoundException, IOException, QueryEvaluationException,
			RepositoryException, MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#BUSINESS"));
		String query = discoveryQueryBuilder
				.getDiscoverQuery2Args(categoryList);
		String expected = readFile("/getDiscoverQuery2ArgsTestResult");
		Assert.assertEquals(query, expected);
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
	}

	@Test
	public void testGetLookupQuery() throws URISyntaxException, IOException {
		URI namespace = new URI("http://www.sti2.at/MSEE/");
		String operationName = "getData";

		String query = discoveryQueryBuilder.getLookupQuery(namespace,
				operationName);
		String expected = readFile("/getLookupQueryTestResult");
		Assert.assertEquals(query, expected);
	}

	@Test
	public void testGetIServeModelQuery() throws URISyntaxException,
			IOException, QueryEvaluationException, RepositoryException,
			MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {
		String serviceID = "TheServiceID";
		
		String query = discoveryQueryBuilder.getIServeModelQuery(serviceID);
		String expected = readFile("/getIServeModelQueryTestResult");
		Assert.assertEquals(query, expected);
	}

	private static String readFile(String path) throws IOException {
		Scanner sc = new Scanner(
				DiscoveryQueryBuilderTest.class.getResourceAsStream(path));
		String text = sc.useDelimiter("\\A").next();
		sc.close();
		return text;
	}

}
