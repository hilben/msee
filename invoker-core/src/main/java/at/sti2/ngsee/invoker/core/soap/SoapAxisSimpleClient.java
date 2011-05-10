package at.sti2.ngsee.invoker.core.soap;

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
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.client.ServiceClient;

import at.sti2.ngsee.invoker.api.core.ISOAPInvoker;

public class SoapAxisSimpleClient{

	public void doTest(String[] args) throws SOAPException {
		String endpointUrl = "http://www.webservicex.com/globalweather.asmx";

		QName serviceName = new QName("http://www.webserviceX.NET/",
				"GlobalWeather");
		QName portName = new QName("http://www.webserviceX.NET/",
				"GlobalWeatherSoap12");

		/** Service erstellen und mindestens einen Port hinzufügen. **/
		Service service = Service.create(serviceName);
		service.addPort(portName, SOAPBinding.SOAP12HTTP_BINDING, endpointUrl);

		/** Dispatch-Instanz aus einem Service erstellen **/
		Dispatch<SOAPMessage> dispatch = service.createDispatch(portName,
				SOAPMessage.class, Service.Mode.MESSAGE);

		/** SOAPMessage-Anforderung erstellen **/
		// Anforderungsnachricht erstellen
		MessageFactory mf = MessageFactory
				.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);

		// Nachricht erstellen. In diesem Beispiel wird mit SOAPPART gearbeitet.
		SOAPMessage request = mf.createMessage();
		SOAPPart part = request.getSOAPPart();

		// SOAPEnvelope sowie Header- und Hauptteilelemente abrufen
		SOAPEnvelope env = part.getEnvelope();
		SOAPHeader header = env.getHeader();
		SOAPBody body = env.getBody();

		// Nachrichtennutzdaten erstellen
		SOAPElement operation = body.addChildElement("GetWeather", "ns1",
				"http://www.webserviceX.NET");
		SOAPElement value = operation.addChildElement("CityName");
		value.addTextNode("Innsbruck");
		SOAPElement value2 = operation.addChildElement("CountryName");
		value2.addTextNode("Austria");
		request.saveChanges();

		/** Service-Endpoint aufrufen **/
		SOAPMessage response = dispatch.invoke(request);

		/** Antwort verarbeiten **/
	}

//	@Override
	public String invoke(URL wsdlURL, QName operationQName,
			String inputData) throws Exception {
		
		ServiceClient serviceClient = new ServiceClient(null, wsdlURL, null,null);
		// Options opts = new Options(); //Not needed for dynamic client
		OMElement omInput = AXIOMUtil.stringToOM(inputData);
		String result = serviceClient.sendReceive(operationQName,omInput).toString();
		serviceClient.cleanup();
		
		return result;
	}
}