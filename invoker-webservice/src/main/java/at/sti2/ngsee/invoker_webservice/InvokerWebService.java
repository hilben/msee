package at.sti2.ngsee.invoker_webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.ServiceHandler;
import at.sti2.ngsee.invoker.framework.InvokerFactory;
import at.sti2.ngsee.invoker_api.framework.IServiceInvoker;
import at.sti2.ngsee.invoker_api.framework.IServiceInvoker.BINDING_TYPE;
import at.sti2.ngsee.invoker_api.ws.IInvokerEndpoint;

/**
 * 
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>    Copyright (c) 2011 STI<br>
 * <b>Company:</b>      STI Innsbruck<br>
 *
 * @author      		michaelrogger<br>
 * @author				Alex Oberhauser<br>
 * @version     		$Id$<br>
 * Date of creation:  	17.03.2011<br>
 * File:         		$Source$<br>
 * Modifier:     		$Author$<br>
 * Revision:     		$Revision$<br>
 * State:        		$State$<br>
 */

@WebService(targetNamespace="http://see.sti2.at/")
public class InvokerWebService implements IInvokerEndpoint
{
	
	protected static Logger logger = Logger.getLogger(InvokerWebService.class);

	/* (non-Javadoc)
	 * @see at.sti2.ngsee.invoker_api.Invoker#invoke(java.lang.String, java.lang.String)
	 */
	@WebMethod
	public String invoke(@WebParam(name="serviceID")String _serviceID,
			@WebParam(name="operation")String _operationName,
			@WebParam(name="inputData")String _inputData) {
		
		//TODO retrieve wsmoLite from TripleStore using webServiceID
		
		String serviceURL = ServiceHandler.getServiceURL(_serviceID);
		
		// Lookup the namespace of the operation. If the operationName is a URL, then the operationName is returned.
		String operationQName = ServiceHandler.getOperationQName(_serviceID, _operationName);
		// TODO: Lower the RDF Data to Data that is understandable for the Service.
		String serviceData = _inputData;
		
		logger.info("Invoking Web Service '" + _serviceID + "' with data '" + _inputData + "'");

		// TODO: Check the Service Type, e.g. REST, SOAP
		IServiceInvoker wsdlInvoker = InvokerFactory.createWSDLInvoker();
		return wsdlInvoker.invoke(serviceURL, BINDING_TYPE.Automatic, operationQName, serviceData);
	}
	
	public String getVersion() {
		logger.info("Invoking getVersion()");
		return "v9999";
	}

}
