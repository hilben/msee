package at.sti2.ngsee.invoker_client;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }
    
    private static Node getDescriptionNode(String _rdfXML) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(new ByteArrayInputStream(_rdfXML.getBytes()));
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		
		for ( int count=0; count < nodeList.getLength(); count++ ) {
			Node node = nodeList.item(count);
			if ( node.getNodeName().equals("rdf:Description") ) {
				return node;
			}
		}
		return null;
    }

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
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testApp() throws Exception {
//		String serviceID = "http://www.webserviceX.NET#GlobalWeather";
//		String operationName = "GetWeather";
//    	String classificationURI = "http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS";
//    	
//    	List<String> classificationList = new ArrayList<String>();
//    	classificationList.add(classificationURI);
//    	
//    	JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//    	Client discoverClient = dcf.createClient("http://sesa.sti2.at:8080/discovery-webservice/services/discovery?wsdl");
//    	Object[] discoverResponse = discoverClient.invoke(new QName("http://webservice.discovery.ngsee.sti2.at/", "discover"), classificationList);
//    	if ( discoverResponse.length > 0 ) {
//    		Assert.assertTrue(foundSomething(discoverResponse[0].toString()));
//    		Node descriptionNode = getDescriptionNode(discoverResponse[0].toString());
//    		if ( descriptionNode != null ) {
//    			NodeList nodes = descriptionNode.getChildNodes();
//    			Assert.assertTrue(nodes.getLength() > 0);
//    		}
//    	} else
//    		Assert.assertTrue(false);
//    	
//    	Client invocationClient = dcf.createClient("http://sesa.sti2.at:8080/invoker-webservice/services/invoker?wsdl");
//    	
//    	StringBuilder inputData = new StringBuilder();
//		String NL = System.getProperty("line.separator");
//		Scanner scanner = new Scanner(new FileInputStream(AppTest.class.getResource("/weather-input.rdf.xml").getFile()));
//		try {
//		      while (scanner.hasNextLine()){
//			        inputData.append(scanner.nextLine() + NL);
//		      }
//		} finally {
//			scanner.close();
//		}
//		
//    	Object[] invocationResponse = invocationClient.invoke(new QName("http://see.sti2.at/", "invoke"), serviceID, operationName, inputData.toString());
//    	System.out.println(invocationResponse[0]);
    }
}
