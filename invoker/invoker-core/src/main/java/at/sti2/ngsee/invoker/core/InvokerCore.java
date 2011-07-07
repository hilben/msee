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
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import at.sti2.ngsee.invoker.api.core.ISOAPInvoker;
import at.sti2.ngsee.invoker.api.grounding.IGroundingEngine;
import at.sti2.ngsee.invoker.grounding.GroundingFactory;

public class InvokerCore {
	protected static Logger logger = Logger.getLogger(InvokerCore.class);
	
	private static SOAPMessage createSOAPMessage(String _loweredInputData) throws SOAPException, DocumentException, SAXException, IOException, ParserConfigurationException {
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
	
	private static String getBodyContent(SOAPMessage _message) throws TransformerException, SOAPException {
		Document doc = _message.getSOAPBody().extractContentAsDocument();
		
		Source source = new DOMSource(doc);
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.transform(source, result);
		
		return stringWriter.getBuffer().toString();
	}
	
	@SuppressWarnings("unused")
	private static String getSOAPMessageAsString(SOAPMessage _message) throws SOAPException, IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		_message.writeTo(os);
		return os.toString();
	}
	
	/**
	 * @param _serviceID
	 * @param _operationName
	 * @param _inputData
	 * @return
	 * @throws Exception
	 */
	public static String invoke(String _serviceID, List<QName> _header, String _operationName, String _inputData) throws Exception {
		/*
		 * Reading out all relevant information from the Triplestore
		 */
		InvokerMSM msmObject = TriplestoreHandler.getInvokerMSM(_serviceID, _operationName);
		
		IGroundingEngine groundingEngine = GroundingFactory.createGroundingEngine(msmObject.getLoweringSchema(), msmObject.getLifingSchema());
		
		/*
		 * Starting the lowering process
		 */
		String loweredInputData = groundingEngine.lower(_inputData);
		
		/*
		 * Starting the invocation process
		 */
		ISOAPInvoker soapInvoker = InvokerFactory.createSOAPInvoker();
		logger.info("Invoking Web Service '" + msmObject.getWSDL() + "' with input data '" + loweredInputData + "'");
		
		SOAPMessage outputData = soapInvoker.invoke(msmObject.getServiceQName(), msmObject.getPortQName(), msmObject.getEndpointURL().toExternalForm(), msmObject.getSOAPAction(), createSOAPMessage(loweredInputData));
		
		/*
		 * Return the lifted data
		 */
		return groundingEngine.lift(getBodyContent(outputData));
	}

}
