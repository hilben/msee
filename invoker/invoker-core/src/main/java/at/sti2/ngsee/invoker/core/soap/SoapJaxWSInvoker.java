package at.sti2.ngsee.invoker.core.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import at.sti2.ngsee.invoker.api.core.ISOAPInvoker;

public class SoapJaxWSInvoker implements ISOAPInvoker {
	
	public SOAPMessage invoke(QName serviceName, QName portName,
			String endpointUrl, String soapActionUri, SOAPMessage _soapMessage)
			throws Exception {

		/** Create a service and add at least one port to it. **/
		Service service = Service.create(serviceName);
		service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointUrl);

		/** Create a Dispatch instance from a service. **/
		Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class,
				Service.Mode.MESSAGE);

		// The soapActionUri is set here. otherwise we get a error on .net based
		// services.
		dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY,
				new Boolean(true));
		dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY,
				soapActionUri);

		/** Create SOAPMessage request. **/
		// compose a request message
//		MessageFactory messageFactory = MessageFactory.newInstance();
//		SOAPMessage message = messageFactory.createMessage();
		// Create objects for the message parts
//		SOAPPart soapPart = message.getSOAPPart();
		// Populate the Message. In here, I populate the message from a xml file
//		StreamSource preppedMsgSrc = new StreamSource(new StringReader(
//				inputData));
//		soapPart.setContent(preppedMsgSrc);
		// Save the message
//		message.saveChanges();

//		System.out.println(message.getSOAPBody().getFirstChild()
//				.getTextContent());

		SOAPMessage response = (SOAPMessage) dispatch.invoke(_soapMessage);

		return response;
	}

}
