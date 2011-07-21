package at.sti2.ngsee.invoker.core.soap.test;

import java.io.StringReader;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.ngsee.invoker.core.soap.SoapJaxWSInvoker;

public class SoapJaxWSInvokerTest extends AbstractSoapTest {
	
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
	
	private SOAPMessage _createDummyService1PingMessage(String _serviceID) throws Exception {
		SOAPMessage inputMessage = MessageFactory.newInstance().createMessage();
		SOAPBody body = inputMessage.getSOAPBody();
		
		SOAPBodyElement operationElement = body.addBodyElement(new QName("http://see.sti2.at/", "ping", "ex"));
		operationElement.addChildElement("serviceID").addTextNode(_serviceID);
		
		inputMessage.saveChanges();
		return inputMessage;
	}
	
//	@Test
	public void testDummyService1() throws Exception {
		String namespace = "http://see.sti2.at/";
		QName serviceName = new QName(namespace, "PingWebServiceService");
		QName portName = new QName(namespace, "PingWebService");
		String endpointURL = "http://sesa.sti2.at:8080/invoker-dummy-webservice/services/ping";
		
		for ( int count=0; count < 2; count++ ) {
			/*
			 * Generating a random, unique identifier to prevent success through caching.
			 */
			String serviceID = UUID.randomUUID().toString();
			SOAPMessage responseMessage = this.invoker.invoke(serviceName, portName, endpointURL, "", this._createDummyService1PingMessage(serviceID));
			SOAPBodyElement operationNode = (SOAPBodyElement) responseMessage.getSOAPBody().getChildElements().next();
			SOAPBodyElement returnNode = (SOAPBodyElement) operationNode.getChildElements().next();
			Assert.assertEquals("Hello " + serviceID, returnNode.getValue());
			
			logger.info("Invoked Service " + serviceName + " using endpoint "+endpointURL);
		}
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
			logger.info(result.getSOAPBody().getFirstChild().getTextContent());
			logger.info("ServiceName " + serviceName + "took ms: " +stopTimer());
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
			logger.info(result.getSOAPBody().getFirstChild().getTextContent());
			logger.info("ServiceName " + serviceName + "took ms: " +stopTimer());
		}
	}

}
