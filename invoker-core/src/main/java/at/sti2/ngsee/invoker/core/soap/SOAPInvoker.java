package at.sti2.ngsee.invoker.core.soap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.api.core.ISOAPInvoker;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Alex Oberhauser<br>
 * @version     $Id$<br>
 * Date of creation:  13.04.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public class SOAPInvoker implements ISOAPInvoker {
	protected static Logger logger = Logger.getLogger(SOAPInvoker.class);
	
	@Deprecated
    private String _getSOAPAction(QName _operationQName) {
        String namespace = _operationQName.getNamespaceURI();
        String operation = _operationQName.getLocalPart();
        if ( namespace.endsWith("/") )
        	return namespace + operation;
        else
            return namespace + "/" + operation;
    }
	
    @Deprecated
	@Override
	public String invoke(String _wsdlURL, QName _operationQName, String... _inputData) throws AxisFault, MalformedURLException, XMLStreamException { 
		ServiceClient serviceClient = new ServiceClient();
		Options opts = new Options();
		opts.setTo(new EndpointReference(_wsdlURL));
		/**
		 * TODO: Support SOAPAction values that are different than the operation qualified name. 
		 */
		opts.setAction(this._getSOAPAction(_operationQName));
		serviceClient.setOptions(opts);
		
		OMFactory factory = OMAbstractFactory.getOMFactory();
		OMElement method = factory.createOMElement(_operationQName);
		
		if ( _inputData != null ) {
			for ( int count=0; count < _inputData.length; count++ ) {
				OMElement inputData = AXIOMUtil.stringToOM(_inputData[count]);
				method.addChild(inputData);
			}
		}
		logger.debug("SOAPAction  : " + opts.getAction());
		logger.debug("SOAPMessage : " + method);
		
		String result = serviceClient.sendReceive(method).toString();
		
		serviceClient.cleanup();
		serviceClient.cleanupTransport();
		serviceClient.removeHeaders();
		return result;
	}
	
	@Override
	public String invoke(URL _wsdlURL, List<QName> _header, String _soapAction, String _inputData) throws AxisFault, XMLStreamException {
		ServiceClient serviceClient = new ServiceClient();
		Options opts = new Options();
		opts.setTo(new EndpointReference(_wsdlURL.toString()));
		opts.setAction(_soapAction);

		OMFactory factory = OMAbstractFactory.getOMFactory();
		for ( QName entry : _header ) {
			OMElement omHeaderEle = factory.createOMElement(entry);
			serviceClient.addHeader(omHeaderEle);
		}
		
		OMElement omInput = AXIOMUtil.stringToOM(_inputData.toString());
		String result = serviceClient.sendReceive(omInput).toString();
		
		serviceClient.cleanup();
		serviceClient.cleanupTransport();
		serviceClient.removeHeaders();
		
		return result;
	}
	
}
