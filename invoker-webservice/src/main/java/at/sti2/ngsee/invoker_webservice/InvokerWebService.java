package at.sti2.ngsee.invoker_webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.InvokerFramework;
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
			@WebParam(name="inputData")String _inputData) throws Exception {
		return InvokerFramework.invoke(_serviceID, _operationName, _inputData);
	}
	
	public String getVersion() {
		logger.info("Invoking getVersion()");
		return "v9999";
	}

}
