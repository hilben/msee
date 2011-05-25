/**
 * 
 */
package at.sti2.ngsee.invoker.api.core;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;

//import javax.xml.namespace.QName;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>    Copyright (c) 2011 STI<br>
 * <b>Company:</b>      STI Innsbruck<br>
 *
 * @author      		michaelrogger<br>
 * @author 				Alex Oberhauser<br>
 * @version     		$Id$<br>
 * Date of creation:  	17.03.2011<br>
 * File:         		$Source$<br>
 * Modifier:     		$Author$<br>
 * Revision:     		$Revision$<br>
 * State:        		$State$<br>
 */
public interface ISOAPInvoker {
	
	/**
	 * 
	 * @param wsdlURL The Endpoint of the 
	 * @param header The SOAP Header forwareded from the client.
	 * @param soapAction SOAPAction field specified in the HTTP Header.
	 * @param inputData The input data, understandable by the services.
	 * @return
	 */
//	public String invoke(URL wsdlURL, List<QName> header, String _soapAction, String inputData) throws Exception;
	

	public SOAPMessage invoke(QName serviceName,
			QName portName,
			String endpointUrl,
			String soapActionUri,
			SOAPMessage soapMessage) throws Exception;
	
}
