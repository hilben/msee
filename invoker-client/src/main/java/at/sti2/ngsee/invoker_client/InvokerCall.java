package at.sti2.ngsee.invoker_client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class InvokerCall {
	
	public static SOAPMessage invokeService(String _serviceID, String _operation, String _inputData) throws UnsupportedOperationException, SOAPException, MalformedURLException {
		SOAPConnectionFactory soapFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapFactory.createConnection();
		
		URL url = new URL("http://localhost:9090/invoker-webservice/services/invoker");
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", "");
		
		SOAPBody soapBody = soapMessage.getSOAPBody();
		soapBody.addNamespaceDeclaration("xsi", "http://www.w3.org/1999/XMLSchema-instance");
		
		SOAPElement discoverMethodElement = soapBody.addChildElement(new QName("http://see.sti2.at/", "invoke", "ns1"));
		
		SOAPElement serviceIDElement = discoverMethodElement.addChildElement("serviceID");
		serviceIDElement.addAttribute(new QName(soapBody.getNamespaceURI("xsi"), "type", "xsi"), "xsd:string");
		serviceIDElement.addTextNode(_serviceID);

		SOAPElement operationElement = discoverMethodElement.addChildElement("operation");
		operationElement.addAttribute(new QName(soapBody.getNamespaceURI("xsi"), "type", "xsi"), "xsd:string");
		operationElement.addTextNode(_serviceID);
		
		SOAPElement inputDataElement = discoverMethodElement.addChildElement("inputData");
		inputDataElement.addAttribute(new QName(soapBody.getNamespaceURI("xsi"), "type", "xsi"), "xsd:string");
		inputDataElement.addTextNode(_inputData);
		
		soapMessage.saveChanges();
		return soapConnection.call(soapMessage, url);
	}
	
	public static SOAPMessage getVersion() throws UnsupportedOperationException, SOAPException, MalformedURLException {
		SOAPConnectionFactory soapFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapFactory.createConnection();
		
		URL url = new URL("http://localhost:9090/invoker-webservice/services/invoker");
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
		
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", "");
		
		SOAPBody soapBody = soapMessage.getSOAPBody();
		soapBody.addNamespaceDeclaration("xsi", "http://www.w3.org/1999/XMLSchema-instance");
		
		soapBody.addChildElement(new QName("http://see.sti2.at/", "getVersion", "ns1"));
		
		soapMessage.saveChanges();
		return soapConnection.call(soapMessage, url);
	}
}
