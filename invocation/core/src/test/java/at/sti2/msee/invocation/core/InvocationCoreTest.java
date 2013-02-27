package at.sti2.msee.invocation.core;

import java.io.IOException;

import javax.xml.soap.SOAPException;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

public class InvocationCoreTest {

	@Test
	public void test() throws SOAPException, IOException {
		String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>  <SOAP-ENV:Envelope   SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"   xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"   xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"   xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\"   xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">	<SOAP-ENV:Body>		<ns1:doubleAnInteger		 xmlns:ns1=\"urn:MySoapServices\">			<param1 xsi:type=\"xsd:int\">123</param1>		</ns1:doubleAnInteger>	</SOAP-ENV:Body>  </SOAP-ENV:Envelope> ";
		Assert.assertNotNull(InvocationCore.generateSOAPMessage(xmlText));

	}
	
	@Ignore
	@Test
	public void testInvoke() throws SOAPException, IOException {
		//String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>  <SOAP-ENV:Envelope   SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"   xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"   xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"   xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\"   xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">	<SOAP-ENV:Body>		<ns1:doubleAnInteger		 xmlns:ns1=\"urn:MySoapServices\">			<param1 xsi:type=\"xsd:int\">123</param1>		</ns1:doubleAnInteger>	</SOAP-ENV:Body>  </SOAP-ENV:Envelope> ";
		//Assert.assertNotNull(invocationCore.invoke(_serviceID, _header, _operationName, _inputData));

	}
	


}
