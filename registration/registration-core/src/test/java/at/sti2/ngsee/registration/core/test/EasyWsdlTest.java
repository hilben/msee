package at.sti2.ngsee.registration.core.test;


import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ow2.easywsdl.extensions.sawsdl.SAWSDLFactory;
import org.ow2.easywsdl.extensions.sawsdl.api.Description;
import org.ow2.easywsdl.extensions.sawsdl.api.Endpoint;
import org.ow2.easywsdl.extensions.sawsdl.api.Input;
import org.ow2.easywsdl.extensions.sawsdl.api.InterfaceType;
import org.ow2.easywsdl.extensions.sawsdl.api.Operation;
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLReader;
import org.ow2.easywsdl.extensions.sawsdl.api.Service;

public class EasyWsdlTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testWSDL() throws Exception{
		
		// Read a SAWSDL description
		SAWSDLReader reader = SAWSDLFactory.newInstance().newSAWSDLReader();
		Description desc = reader.read(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"));
		
		for(Service service : desc.getServices()){
			System.out.println("service name "+service.getQName());
			
			for(Endpoint endpoint : service.getEndpoints()){
				System.out.println(" endpoint address "+endpoint.getAddress());
			}
		}
		
		for(InterfaceType wsdlInterface : desc.getInterfaces()){
			System.out.println("interface name "+wsdlInterface.getQName());
			
			for(Operation operation : wsdlInterface.getOperations()){
				System.out.println(" operation name "+operation.getQName());
				
				Input input = operation.getInput();
				System.out.println("  input name "+input.getName());
				
//				for( Part part : input.getParts()){
//					System.out.println("   part name "+part.getPartQName());
//					
//					Element element = part.getElement();
//					System.out.println("    element name "+element.getQName());
//				}
			}
		}
		
	}

}
