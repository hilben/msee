package at.sti2.ngsee.invoker_client;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.NodeList;

public class Invoker {
	
	public String getVersion() throws IOException, SOAPException {
		SOAPMessage response = InvokerCall.getVersion();
		SOAPBody body = response.getSOAPBody();
		NodeList nodes = body.getChildNodes();
		if ( nodes.getLength() > 0 )
			return nodes.item(0).getTextContent();
		return null;
	}
	
	public String invokeService(String _serviceID, String _operation, String _inputData) throws UnsupportedOperationException, MalformedURLException, SOAPException {
		SOAPMessage response = InvokerCall.invokeService(_serviceID, _operation, _inputData);
		SOAPBody body = response.getSOAPBody();
		NodeList nodes = body.getChildNodes();
		if ( nodes.getLength() > 0 )
			return nodes.item(0).getTextContent();
		return null;
	}
	
}
