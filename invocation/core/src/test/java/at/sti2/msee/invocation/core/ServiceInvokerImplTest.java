package at.sti2.msee.invocation.core;

import java.io.FileInputStream;
import java.util.Scanner;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

public class ServiceInvokerImplTest extends TestCase{
	protected static Logger logger = Logger.getLogger(ServiceInvokerImplTest.class);
	
	private ServiceInvocationImpl invoker;

	@Before
	public final void setUp() {
		invoker = new ServiceInvocationImpl();
	}

	@Test
	public final void testInvokeTestWebService1() throws Exception {
		String endpoint = "http://sesa.sti2.at:8080/monitoring-testwebservices/services/big";
		String soapFile = "/test_monitoring.soap";

		FileInputStream fis = new FileInputStream(ServiceInvokerImplTest.class
				.getResource(soapFile).getFile());
		String soapMessage = "";

		Scanner s = new Scanner(fis);
		while (s.hasNext()) {
			soapMessage += s.nextLine() + "\n";
		}
		
		s.close();
		fis.close();
		
		invoker.invoke(endpoint, "", soapMessage);
	}
	
	@Test
	public final void testInvokeTestWebService2() throws Exception {
		String endpoint = "http://msee.sti2.at/discovery-webservice/service";
		String soapFile = "/test_discover.soap";

		FileInputStream fis = new FileInputStream(ServiceInvokerImplTest.class
				.getResource(soapFile).getFile());
		String soapMessage = "";

		Scanner s = new Scanner(fis);
		while (s.hasNext()) {
			soapMessage += s.nextLine() + "\n";
		}
		
		s.close();
		fis.close();
		
		invoker.invoke(endpoint, "", soapMessage);
	}
	
	@Test
	public final void testInvokeFailure() throws Exception {
		String endpoint = "http://www.test.com/noservice";
		String soapFile = "/test_monitoring.soap";

		FileInputStream fis = new FileInputStream(ServiceInvokerImplTest.class
				.getResource(soapFile).getFile());
		String soapMessage = "";

		Scanner s = new Scanner(fis);
		while (s.hasNext()) {
			soapMessage += s.nextLine() + "\n";
		}
		
		s.close();
		fis.close();
		
		boolean fails = false;
		try {
		invoker.invoke(endpoint, "", soapMessage);
		} catch (ServiceInvokerException e){
			fails = true;
		}
		
		assertTrue(fails);
	}

}
