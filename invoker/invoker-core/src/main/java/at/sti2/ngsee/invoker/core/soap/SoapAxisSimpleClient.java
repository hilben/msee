package at.sti2.ngsee.invoker.core.soap;

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.client.ServiceClient;

public class SoapAxisSimpleClient {
	
	private ServiceClient serviceClient;
	
	public SoapAxisSimpleClient() {
	}

	// @Override
	public String invoke(URL wsdlURL, QName serviceName, QName operationQName, String inputData)
			throws Exception {

		serviceClient = new ServiceClient(null, wsdlURL, serviceName, null);
		// Options opts = new Options(); //Not needed for dynamic client
		OMElement omInput = AXIOMUtil.stringToOM(inputData);
		String result = serviceClient.sendReceive(operationQName, omInput)
				.toString();
		serviceClient.cleanup();

		return result;
	}
}