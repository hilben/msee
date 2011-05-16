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
	@Test
	public void testDynamicPingService() throws Exception {

		//slow but at least more than one call works
		int loops = 5;

		for (int i = 0; i < loops; i++) {
			startTimer();
			String input = "<see:ping xmlns:see=\"http://see.sti2.at/\"><serviceID>Michael</serviceID></see:ping>";
			QName serviceName = new QName("http://see.sti2.at/",
			"PingWebServiceService");
			URL wsdlURL = new URL("http://localhost:9090/invoker-dummy-webservice/services/ping?wsdl");
			String result = soapInvoker.invokeDynamicClient(wsdlURL, "ping", "Michael");
			System.out.println(result);
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
			
		}
	}
	
}
