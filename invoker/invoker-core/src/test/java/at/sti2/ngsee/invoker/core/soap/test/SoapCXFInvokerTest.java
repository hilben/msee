package at.sti2.ngsee.invoker.core.soap.test;

import java.net.URL;

import javax.xml.namespace.QName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.ngsee.invoker.core.soap.SoapCXFInvoker;

public class SoapCXFInvokerTest extends AbstractSoapTest{

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
//	@Test
	public void testDynamicPingService() throws Exception {

		//slow but at least more than one call works
		int loops = 5;

		for (int i = 0; i < loops; i++) {
			startTimer();
//			String input = "<see:ping xmlns:see=\"http://see.sti2.at/\"><serviceID>Michael</serviceID></see:ping>";
			QName serviceName = new QName("http://see.sti2.at/",
			"PingWebServiceService");
			URL wsdlURL = new URL("http://localhost:9090/invoker-dummy-webservice/services/ping?wsdl");
			String result = soapInvoker.invokeDynamicClient(wsdlURL, "ping", "Michael");
			System.out.println(result);
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
			
		}
	}
	
//	@Test
	public void testBLZService() throws Exception {

		//slow but at least more than one call works
		int loops = 5;

		for (int i = 0; i < loops; i++) {
			startTimer();
//			String input = "<blz:getBank xmlns:blz=\"http://thomas-bayer.com/blz/\">"
//				+ "<blz:blz>60050101</blz:blz>" 
//				+ "</blz:getBank>";
			QName serviceName = new QName("http://thomas-bayer.com/blz/",
			"BLZService");
			URL wsdlURL = new URL("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
			String result = soapInvoker.invokeDynamicClient(wsdlURL, "http://thomas-bayer.com/blz/getBank", "60050101");
			System.out.println(result);
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
			
		}
	}
	
}
