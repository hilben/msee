package at.sti2.ngsee.invoker.core.soap;

import java.io.StringReader;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.client.ServiceClient;

import at.sti2.ngsee.invoker.api.core.ISOAPInvoker;

public class SoapAxisSimpleClient {

	// @Override
	public String invoke(URL wsdlURL, QName operationQName, String inputData)
			throws Exception {

		ServiceClient serviceClient = new ServiceClient(null, wsdlURL, null,
				null);
		// Options opts = new Options(); //Not needed for dynamic client
		OMElement omInput = AXIOMUtil.stringToOM(inputData);
		String result = serviceClient.sendReceive(operationQName, omInput)
				.toString();
		serviceClient.cleanup();

		return result;
	}
}