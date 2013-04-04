package at.sti2.msee.invocation.core;

import static org.junit.Assert.*;

import java.io.StringWriter;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.junit.Test;

public class InvocationCoreTest2 {

	@Test
	public final void testInvoke() throws Exception {
		String serviceID = "http://sesa.sti2.at/services/";
		String operationName = "discover";
		String inputData = "http://msee.sti2.at/categories#BUSINESS";
		String retval = InvocationCore.invoke(serviceID, operationName, inputData);
		System.out.println(retval);
	}

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
