package at.sti2.ngsee.invoker.core.soap.test;


import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.ngsee.invoker.core.soap.SoapJaxWSInvoker;

public class SoapJaxWSInvokerTest extends AbstractSoapTest{
	
	private SoapJaxWSInvoker invoker;

	@Before
	public void setUp() throws Exception {
		invoker=new SoapJaxWSInvoker();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testInvoke() throws Exception {
		
		int loops = 10;

		for (int i = 0; i < loops; i++) {
			startTimer();
			QName serviceName = new QName("http://see.sti2.at/",
			"PingWebServiceService");
			QName portName = new QName("http://see.sti2.at/",
			"PingWebServiceService");
			String endpointUrl = "http://localhost:9090/invoker-dummy-webservice/services/ping";
			String soapActionUri = "";
			String inputData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:see=\"http://see.sti2.at/\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ "<see:ping>"
				+ "<serviceID>Michael</serviceID>"
				+ "</see:ping>"
				+ "</soapenv:Body>" + "</soapenv:Envelope>";
			
			SOAPMessage result = invoker.invoke(serviceName, portName, endpointUrl, soapActionUri, inputData);
			System.out.println(result.getSOAPBody().getFirstChild()
					.getTextContent());
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
		}
		
		
	}

}
