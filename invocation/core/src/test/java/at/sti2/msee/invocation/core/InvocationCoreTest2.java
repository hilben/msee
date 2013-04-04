package at.sti2.msee.invocation.core;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Scanner;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.junit.Ignore;
import org.junit.Test;

import org.apache.axis.Message;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;

import at.sti2.wsmf.core.test.TestInvocationHandler;

import javax.xml.namespace.QName;

public class InvocationCoreTest2 {

	@Ignore
	@Test
	public final void testInvoke() throws Exception {
		String endpoint = "http://sesa.sti2.at:8080/monitoring-testwebservices/services/big";
		String soapFile ="/test_monitoring.soap";
		
		Service service = new Service();
		Call call = (Call) service.createCall();

		FileInputStream fis = new FileInputStream(InvocationCoreTest2.class
				.getResource(soapFile).getFile());

		Scanner s = new Scanner(fis);
		while (s.hasNext())
			System.out.println(s.next());
		
		fis = new FileInputStream(InvocationCoreTest2.class
				.getResource(soapFile).getFile());

		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		SOAPEnvelope env = new SOAPEnvelope(fis);

		System.out.println("RETURN: "+ call.invoke(env));
	}
	
	@Test
	public final void testInvoke2() throws Exception {
		String endpoint = "http://msee.sti2.at/discovery-webservice/service";
		String soapFile = "/test_discover.soap";
		
		Service service = new Service();
		Call call = (Call) service.createCall();

		FileInputStream fis = new FileInputStream(InvocationCoreTest2.class
				.getResource(soapFile).getFile());

		Scanner s = new Scanner(fis);
		while (s.hasNext())
			System.out.println(s.next());
		
		fis = new FileInputStream(InvocationCoreTest2.class
				.getResource(soapFile).getFile());

		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		SOAPEnvelope env = new SOAPEnvelope(fis);

		System.out.println("RETURN: "+ call.invoke(env));
	}

	@Ignore
	@Test
	public final void testGenerateSOAPMessage() throws SOAPException {
		String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">	<SOAP-ENV:Body>		<ns1:doubleAnInteger xmlns:ns1=\"urn:MySoapServices\">			<param1 xsi:type=\"xsd:int\">123</param1>		</ns1:doubleAnInteger>	</SOAP-ENV:Body>  </SOAP-ENV:Envelope>";

		SOAPMessage message = InvocationCore.generateSOAPMessage(xmlText);
		assertNotNull(message);

		SOAPPart part = message.getSOAPPart();
		Source source = part.getContent();
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			assertEquals(xmlText, result.getWriter().toString());
		} catch (TransformerException ex) {
			ex.printStackTrace();
		}

	}

}
