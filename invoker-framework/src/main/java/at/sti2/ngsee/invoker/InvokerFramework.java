package at.sti2.ngsee.invoker;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.framework.InvokerFactory;
import at.sti2.ngsee.invoker_api.framework.ISOAPInvoker;
import at.sti2.ngsee.invoker_api.grounding.IGroundingEngine;
import at.sti2.ngsee.invoker_grounding.GroundingFactory;

public class InvokerFramework {
	protected static Logger logger = Logger.getLogger(InvokerFramework.class);
	
	@Deprecated
	public static String invoke(String _serviceID, String _operationName, String... _inputData) throws Exception {	
		// TODO: Delete this line. msmObject includes the WSDL URL 
		String serviceURL = _serviceID;
		
		// TODO: Delete this line. msmObject includes the WSDL URL
		QName operationQName = TriplestoreHandler.getStringToQName(_operationName);
		// TODO: Lower the RDF Data to Data that is understandable for the Service.
		String[] serviceData = _inputData;
		
		logger.info("Invoking Web Service '" + _serviceID + "' with data '" + serviceData + "'");

		// TODO: Check the Service Type, e.g. REST, SOAP
		ISOAPInvoker soapInvoker = InvokerFactory.createSOAPInvoker();
		String xmlData;
		if ( serviceData == null )
			xmlData = soapInvoker.invoke(serviceURL, operationQName);
		else 
			xmlData = soapInvoker.invoke(serviceURL, operationQName, serviceData);
		// TODO: Lifting the xml Data to RDF.
		String rdfData = xmlData.toString();
		return rdfData;
	}
	
	/**
	 * 
	 * @param _serviceID
	 * @param _operationName
	 * @param _inputData
	 * @return
	 * @throws Exception
	 */
	public static String invoke(String _serviceID, String _operationName, String _inputData) throws Exception {
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
		String outputData = soapInvoker.invoke(msmObject.getWSDL(), msmObject.getSOAPAction(), loweredInputData);
		
		/*
		 * Return the lifted data
		 */
		return groundingEngine.lift(outputData);
	}
}
