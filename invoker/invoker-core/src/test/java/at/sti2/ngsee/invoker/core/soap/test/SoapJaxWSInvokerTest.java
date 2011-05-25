package at.sti2.ngsee.invoker.core.soap.test;


import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;

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
	
	private SOAPMessage createSOAPMessage(String _soapMessage) throws SOAPException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage message = messageFactory.createMessage();
		SOAPPart soapPart = message.getSOAPPart();
		StreamSource preppedMsgSrc = new StreamSource(new StringReader(_soapMessage));
		soapPart.setContent(preppedMsgSrc);
		message.saveChanges();
		return message;
	}
	
//	@Test
	public void testInvokeLocalPing() throws Exception {
		
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
			
			SOAPMessage result = invoker.invoke(serviceName, portName, endpointUrl, soapActionUri, createSOAPMessage(inputData));
			System.out.println(result.getSOAPBody().getFirstChild()
					.getTextContent());
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
		}
	}
	
//	@Test
	public void testInvokeBLZService() throws Exception {
		
		int loops = 10;

		for (int i = 0; i < loops; i++) {
			startTimer();
			QName serviceName = new QName("http://thomas-bayer.com/blz/",
			"BLZService");
			QName portName = new QName("http://thomas-bayer.com/blz/", "BLZServiceSOAP12port_http");
			String endpointUrl = "http://www.thomas-bayer.com:80/axis2/services/BLZService";
			String soapActionUri = "";
			String inputData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:see=\"http://see.sti2.at/\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ "<blz:getBank xmlns:blz=\"http://thomas-bayer.com/blz/\">"
				+ "<blz:blz>60050101</blz:blz>" 
				+ "</blz:getBank>"
				+ "</soapenv:Body>" + "</soapenv:Envelope>";
			
			SOAPMessage result = invoker.invoke(serviceName, portName, endpointUrl, soapActionUri, createSOAPMessage(inputData));
			System.out.println(result.getSOAPBody().getFirstChild()
					.getTextContent());
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
		}
	}

}
