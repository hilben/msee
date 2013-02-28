package org.ow2.easywsdl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ow2.easywsdl.extensions.sawsdl.SAWSDLFactory;
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLReader;
import org.ow2.easywsdl.wsdl.WSDLFactory;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.Service;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.WSDLReader;
import org.xml.sax.SAXException;

public class EasyWsdlTest {

	@Before
	public void setUp() throws Exception {
//		XMLUnit.getTestDocumentBuilderFactory().setValidating(true);
//		XMLUnit.setIgnoreAttributeOrder(true);
//		XMLUnit.setIgnoreComments(true);
//		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
//		XMLUnit.setIgnoreWhitespace(true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEasyWSDL_WSDLReader() throws WSDLException, IOException, URISyntaxException, SAXException
	{
		WSDLFactory wsdlFactory = WSDLFactory.newInstance();
		WSDLReader wsdlReader = wsdlFactory.newWSDLReader();

		URL serviceDescriptionURL = EasyWsdlTest.class.getResource("/webservices/ReservationService.wsdl");
		assertNotNull(serviceDescriptionURL);
		
		Description description = wsdlReader.read(serviceDescriptionURL);
		assertNotNull(description);
		
		assertEquals("Target namespace incorrect", "http://greath.example.com/2004/wsdl/resSvc" ,description.getTargetNamespace());
		
		List<Service> services = description.getServices();		
		assertEquals("Number of services incorrect", 1, services.size());		
		assertEquals("Name of service incorrect", "{http://greath.example.com/2004/wsdl/resSvc}reservationService", services.get(0).getQName().toString());
		
	}
	
	@Test
	public void testEasyWSDL_SAWSDLReader() throws WSDLException, IOException, URISyntaxException, SAXException
	{
		SAWSDLReader sawsdlReader = SAWSDLFactory.newInstance().newSAWSDLReader();
		
		URL serviceDescriptionURL = EasyWsdlTest.class.getResource("/webservices/ReservationService.wsdl");
		assertNotNull(serviceDescriptionURL);
		
		org.ow2.easywsdl.extensions.sawsdl.api.Description description = sawsdlReader.read(serviceDescriptionURL);
		assertNotNull(description);
		
		List<org.ow2.easywsdl.extensions.sawsdl.api.Service> services = description.getServices();
		assertEquals("Number of services incorrect", 
				1, 
				services.size());		
		assertEquals("Annotated categories are incorrect",
				"http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS", 
				services.get(0).getModelReference().get(0).toString());
		
	}	

}
