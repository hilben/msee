package at.sti2.msee.registration.webservice;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//import at.sti2.msee.delivery.RegistrationServicePortType;
import at.sti2.msee.registration.api.ServiceRegistration;

public class WebServiceRegistrationImplTest extends EasyMockSupport {

	private Endpoint ep;
	private String address;
	private URL wsdlURL;
	private QName serviceName;
	private QName portName;
	private ServiceRegistration mock;
	
	@Before
	public void setUp() throws Exception {
		
		address = "http://localhost:9000/services/WebServiceRegistration";
		wsdlURL = new URL(address + "?wsdl");
		serviceName = new QName("http://msee.sti2.at/delivery/", "RegistrationService");
		portName = new QName("http://msee.sti2.at/delivery/", "RegistrationServicePort");	

		mock = this.createMock(ServiceRegistration.class);				
		WebServiceRegistrationImpl registrationImpl = new WebServiceRegistrationImpl(mock);

		ep = Endpoint.publish(address, registrationImpl);
	}

	@After
	public void tearDown()
	{
		try {
	         ep.stop();
	      } catch (Throwable t) {
	         System.out.println("Error thrown: " + t.getMessage());
	      }
	}	

	@Test
	public void testRegister_HelloService() throws Exception {
				
		URL serviceDescriptionURL = this.getClass().getResource("/webservices/sawsdl/HelloService.sawsdl");
		assertNotNull("Service description file not found",serviceDescriptionURL);
		
		expect(mock.register(serviceDescriptionURL.toString())).andReturn("http://msee.st2.at/SERVICE");
		
		Service jaxwsService = Service.create(wsdlURL, serviceName);
//		RegistrationServicePortType registration = jaxwsService.getPort(RegistrationServicePortType.class);

		this.replayAll();
		
//		String serviceURI = registration.register(serviceDescriptionURL.toString());
//		assertEquals("http://msee.st2.at/SERVICE", serviceURI);		
		
		this.verifyAll();
	}
}