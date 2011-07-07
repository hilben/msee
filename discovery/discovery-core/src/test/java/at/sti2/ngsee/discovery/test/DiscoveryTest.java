/**
 * DiscoveryTest.java - at.sti2.ngsee.discovery.test
 */
package at.sti2.ngsee.discovery.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.openrdf.rio.RDFFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import at.sti2.ngsee.discovery.core.ServiceDiscovery;

/**
 * @author Alex Oberhauser
 *
 */
public class DiscoveryTest {
	
	private static boolean foundSomething(String _rdfXML) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(new ByteArrayInputStream(_rdfXML.getBytes()));
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		
		boolean hasService = false;
		for ( int count=0; count < nodeList.getLength(); count++ ) {
			Node node = nodeList.item(count);
			if ( node.getNodeName().equals("rdf:Description") ) {
				hasService = true;
			}
		}
		return hasService;
	}
	
	/**
	 * TODO: Check also if the right service was returned and not only if any service was found.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDiscovery() throws Exception {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS"));
		String successRDF = ServiceDiscovery.discover(categoryList, RDFFormat.RDFXML);

		Assert.assertTrue(foundSomething(successRDF));

		categoryList.add(new URI("http://www.example.org/wrongConcept"));
		String failedRDF = ServiceDiscovery.discover(categoryList, RDFFormat.RDFXML);
		Assert.assertFalse(foundSomething(failedRDF));
	}

	@Test
	public void testLookup() throws Exception {
		String lookupResult = ServiceDiscovery.lookup(new URI("http://www.webserviceX.NET"), "GetWeather", RDFFormat.RDFXML);
		Assert.assertTrue(foundSomething(lookupResult));
		
	}

}
