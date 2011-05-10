package at.sti2.ngsee.invoker.core.soap.test;

import java.net.URL;

import javax.xml.namespace.QName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.ngsee.invoker.core.soap.SoapCXFInvoker;

public class SoapCXFInvokerTest {

	private SoapCXFInvoker soapInvoker;

	@Before
	public void setUp() throws Exception {
		soapInvoker = new SoapCXFInvoker();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test invoker-dummy-webservice
	 */
	@Test
	public void testDynamicPingService() throws Exception {
		// slow!

		long start = System.currentTimeMillis();
		String input = "<see:ping xmlns:see=\"http://see.sti2.at/\"><serviceID>Michael</serviceID></see:ping>";
		URL wsdlURL = new URL("http://localhost:9090/invoker-dummy-webservice/services/ping?wsdl");
		soapInvoker.invokeDynamicClient(wsdlURL, "ping", "Michael");
		long end = System.currentTimeMillis();
		System.out.println("took ms: " + (end - start));
	}
	
//	/**
//	 * Test invoker-dummy-webservice
//	 */
//	@Test
//	public void testProxyPingService() throws Exception {
//		// slow!
//
//		long start = System.currentTimeMillis();
//		String input = "<see:ping xmlns:see=\"http://see.sti2.at/\"><serviceID>Michael</serviceID></see:ping>";
//		URL wsdlURL = new URL("http://localhost:9090/invoker-dummy-webservice/services/ping?wsdl");
//		String result = soapInvoker.invokeJaxWSProxy(wsdlURL, new QName("http://see.sti2.at/","PingWebServiceService"),input);
//		System.out.println(result);
//		long end = System.currentTimeMillis();
//		System.out.println("took ms: " + (end - start));
//	}

	// @Test
//	public void invokeGlobalWeather() throws Exception {
//		URL wsdlURL = new URL(
//				"http://www.webservicex.com/globalweather.asmx?WSDL");
//
//		soapInvoker.invoke(wsdlURL, "Innsbruck", "Austria");
//	}

}
