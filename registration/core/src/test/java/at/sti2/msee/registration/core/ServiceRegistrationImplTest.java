package at.sti2.msee.registration.core;

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;

public class ServiceRegistrationImplTest extends XMLTestCase {

	@Before
	public void setUp() throws Exception {
		XMLUnit.getTestDocumentBuilderFactory().setValidating(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
	}

	@After
	public void tearDown() throws Exception {
	}
		
	@Test
	public void testRegisterAnnotatedWSDLWithOneCategory() throws ServiceRegistrationException {
		
		ServiceRegistrationImpl registration = new ServiceRegistrationImpl();
		
		String serviceDescriptionURL = ServiceRegistrationImplTest.class.getResource("/webservices/ReservationService_OneCategory.wsdl").toString();
		
		//Validate wsdl file		
		String serviceURI = registration.register(serviceDescriptionURL);		
		assertNotNull(serviceURI);
		
		String expectedServiceURI = "http://greath.example.com/2004/wsdl/resSvc#reservationService";		
		assertEquals("Service URI incorrect", expectedServiceURI, serviceURI);
		
	}

	@Test
	public void testDeregister() {
		//fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		//fail("Not yet implemented");
	}

}
