package at.sti2.ngsee.invoker;

import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.framework.InvokerFactory;
import at.sti2.ngsee.invoker_api.framework.ISOAPInvoker;

public class InvokerFramework {
	protected static Logger logger = Logger.getLogger(InvokerFramework.class);
	
	public static String invoke(String _serviceID, String _operationName, String _inputData) throws Exception {
		
		//TODO retrieve wsmoLite from TripleStore using webServiceID
		
		String serviceURL = TriplestoreHandler.getServiceURL(_serviceID);
		
		// Lookup the namespace of the operation. If the operationName is a URL, then the operationName is returned.
		QName operationQName = TriplestoreHandler.getOperationQName(_serviceID, _operationName);
		// TODO: Lower the RDF Data to Data that is understandable for the Service.
		String serviceData = _inputData;
		
		logger.info("Invoking Web Service '" + _serviceID + "' with data '" + serviceData + "'");

		// TODO: Check the Service Type, e.g. REST, SOAP
		ISOAPInvoker soapInvoker = InvokerFactory.createSOAPInvoker();
		Vector<String> xmlData;
		if ( serviceData == null )
			xmlData = soapInvoker.invoke(serviceURL, operationQName);
		else 
			xmlData = soapInvoker.invoke(serviceURL, operationQName, serviceData);
		// TODO: Lifting the xml Data to RDF.
		String rdfData = xmlData.toString();
		return rdfData;
	}
}
