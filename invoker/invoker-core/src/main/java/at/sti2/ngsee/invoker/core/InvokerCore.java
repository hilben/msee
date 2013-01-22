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
package at.sti2.ngsee.invoker.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import at.sti2.ngsee.invoker.api.grounding.IGroundingEngine;
import at.sti2.ngsee.invoker.grounding.GroundingFactory;
import at.sti2.wsmf.core.MonitoringInvocationHandler;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;

/**
 * @author Benjamin Hiltpolt
 *
 * TODO: Documentation
 */
public class InvokerCore {
	protected static Logger logger = Logger.getLogger(InvokerCore.class);

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
	 * @param _serviceID
	 * @param _operationName
	 * @param _inputData
	 * @return
	 * @throws Exception
	 */
	public static String invoke(String _serviceID, List<QName> _header,
			String _operationName, String _inputData) throws Exception {

		logger.setLevel(Level.INFO);

		/*
		 * Reading out all relevant information from the Triplestore
		 */
		InvokerMSM msmObject = TriplestoreHandler.getInvokerMSM(_serviceID,
				_operationName);
		

		IGroundingEngine groundingEngine = GroundingFactory
				.createGroundingEngine(msmObject.getLoweringSchema(),
						msmObject.getLifingSchema());

		/*
		 * Starting the lowering process
		 */
		String loweredInputData = groundingEngine.lower(_inputData);

		
		/*
		 * Starting the invocation process
		 */
		
		logger.info("Invoking Web Service '" + msmObject.getWSDL()
				+ "' with input data '" + loweredInputData + "'");

		/**
		 * TODO: Here comes the monitoring code from InvocationHandler (in wsmf)
		 * that was written before invoke
		 */

		SOAPMessage loweredSOAPMessage = createSOAPMessage(loweredInputData);

		SOAPMessage returnMsg = null;
		
		try {
			logger.info("service id: " + _serviceID);
			logger.info("soap content: " + loweredSOAPMessage);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			loweredSOAPMessage.writeTo(out);
			String strMsg = new String(out.toByteArray());
			
			/*
			 * Monitoring!
			 */
			String endpointURL = msmObject.getEndpointURL().toExternalForm();
			
			WebServiceEndpointConfig cfg = WebServiceEndpointConfig.getConfig(endpointURL);
			cfg.setWebServiceName(_operationName);
			
			
			returnMsg = generateSOAPMessage(MonitoringInvocationHandler.invokeWithMonitoring(generateSOAPMessage(strMsg),
					msmObject.getSOAPAction(), new ActivityInstantiatedEvent(endpointURL),
					strMsg.length()));
			
			out = new ByteArrayOutputStream();
			returnMsg.writeTo(out);
			String strMsg2 = new String(out.toByteArray());
			
			
			logger.info("return msg_new" + strMsg2);

		} catch (Exception e) {
			logger.info(e.getCause());
			logger.info(e.getMessage());
			e.printStackTrace();
		}

		/*
		 * Return the lifted data
		 */
		return groundingEngine.lift(getBodyContent(returnMsg));
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
	

}
