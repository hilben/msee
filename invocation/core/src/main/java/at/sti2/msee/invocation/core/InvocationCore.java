/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.msee.invocation.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import at.sti2.msee.invocation.api.grounding.IGroundingEngine;
import at.sti2.msee.invocation.grounding.GroundingFactory;
import at.sti2.wsmf.core.MonitoringInvocationHandler;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;

/**
 * @author Benjamin Hiltpolt
 * 
 *         TODO: Documentation
 */
public class InvocationCore {
	protected static Logger logger = Logger.getLogger(InvocationCore.class);

	/**
	 * @param _message
	 * @return
	 * @throws TransformerException
	 * @throws SOAPException
	 */
	private static String getBodyContent(SOAPMessage _message)
			throws TransformerException, SOAPException {
		Document doc = _message.getSOAPBody().extractContentAsDocument();

		Source source = new DOMSource(doc);
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.transform(source, result);

		return stringWriter.getBuffer().toString();
	}

	/**
	 * @param serviceID
	 * @param operationName
	 * @param inputData
	 * @return
	 * @throws Exception
	 */
	public static String invoke(String serviceID, String operationName,
			String inputData) throws Exception {
		Service service = new Service();
		Call call = null;
		String endpoint = "http://msee.sti2.at/discovery-webservice/service/discovery?wsdl";

		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName(serviceID,
					operationName));
			call.addParameter("categoryList",
					org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			String returnValue = (String) call
					.invoke(new Object[] { inputData });
			return returnValue;
		} catch (Exception e) {
			Assert.assertEquals(
					"No parameters specified to the Call object!  You must call addParameter() for all parameters if you have called setReturnType().",
					e.toString());
		}

		return null;
	}

	/**
	 * 
	 * Generates a SOAPMessage object out of a string
	 * 
	 * @param _soapMessageString
	 * @return
	 * @throws SOAPException
	 */
	public static SOAPMessage generateSOAPMessage(String _soapMessageString)
			throws SOAPException {
		MessageFactory msgFactory = MessageFactory.newInstance();
		SOAPMessage msg = msgFactory.createMessage();
		msg.getSOAPHeader();
		SOAPPart soapPart = msg.getSOAPPart();

		StreamSource msgSrc = new StreamSource(new StringReader(
				_soapMessageString));
		soapPart.setContent(msgSrc);
		msg.saveChanges();
		return msg;
	}
	
	/**
	 * Creates a SOAP message with the input data as message body.
	 * 
	 * @param _loweredInputData
	 * @return
	 * @throws SOAPException
	 * @throws DocumentException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private static SOAPMessage createSOAPMessage(String _loweredInputData)
			throws SOAPException, DocumentException, SAXException, IOException,
			ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource inStream = new InputSource();

		inStream.setCharacterStream(new StringReader(_loweredInputData));
		Document doc = builder.parse(inStream);

		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage message = messageFactory.createMessage();
		SOAPBody soapBody = message.getSOAPBody();
		soapBody.addDocument(doc);
		message.saveChanges();
		return message;
	}

}
