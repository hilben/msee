/**
 * ProxyWebService.java - at.sti2.ngsee.monitoring.webservice
 */
package at.sti2.ngsee.monitoring.webservice;

import java.io.StringReader;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import at.sti2.wsmf.api.ws.IProxyWebService;
import at.sti2.wsmf.core.InvocationHandler;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;

/**
 * @author Alex Oberhauser
 */
@WebService
public class ProxyWebService implements IProxyWebService {
	
	@WebMethod(exclude=true)
	private SOAPMessage generateSOAPMessage(String _soapMessageString) throws SOAPException {
		MessageFactory msgFactory = MessageFactory.newInstance();
		SOAPMessage msg = msgFactory.createMessage();
		msg.getSOAPHeader();
		SOAPPart soapPart = msg.getSOAPPart();
		
		StreamSource msgSrc = new StreamSource(new StringReader(_soapMessageString));
		soapPart.setContent(msgSrc);
		msg.saveChanges();
		return msg;
	}
	
	/**
	 * @see at.sti2.wsmf.api.ws.IProxyWebService#invoke(java.lang.String)
	 */
	@WebMethod
	@Override
	public String invoke(@WebParam(name="soapRequest")String _soapRequest,
			@WebParam(name="soapAction")String _soapAction) throws Exception {
		return InvocationHandler.invoke(this.generateSOAPMessage(_soapRequest), _soapAction, new ActivityInstantiatedEvent(), _soapRequest.length());
	}

}
