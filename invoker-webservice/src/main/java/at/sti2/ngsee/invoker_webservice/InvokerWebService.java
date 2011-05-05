package at.sti2.ngsee.invoker_webservice;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.namespace.QName;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.InFaultInterceptors;
import org.apache.cxf.interceptor.InInterceptors;
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
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA Invoker Component.")
	)
@InInterceptors(interceptors = {"at.sti2.ngsee.invoker_webservice.SOAPInterceptor"})
@InFaultInterceptors(interceptors = {"at.sti2.ngsee.invoker_webservice.SOAPInterceptor"})
public class InvokerWebService implements IInvokerEndpoint {
	protected static Logger logger = Logger.getLogger(InvokerWebService.class);

	/* (non-Javadoc)
	 * @see at.sti2.ngsee.invoker_api.Invoker#invoke(java.lang.String, java.lang.String)
	 */
	@Deprecated
	@WebMethod
	public String invokeOld(@WebParam(name="serviceID")String _serviceID,
			@WebParam(name="operation")String _operationName,
			@WebParam(name="inputData")String... _inputData) throws Exception {
		logger.info("Invoking invoke('" + _serviceID + "', '" + _operationName + "', '" + _inputData +  "')");
		return InvokerFramework.invoke(_serviceID, _operationName, _inputData);
	}
	
	@WebMethod(exclude=true)
	private List<QName> extractHeader(List<Header> _headers) {
		List<QName> header = new ArrayList<QName>();
		for ( Header entry : _headers) {
			header.add(entry.getName());
		}
		return header;
	}
	
	/* (non-Javadoc)
	 * @see at.sti2.ngsee.invoker_api.Invoker#invoke(java.lang.String, java.lang.String)
	 */
	@WebMethod
	public String invoke(@WebParam(name="serviceID")String _serviceID,
			@WebParam(name="operation")String _operationName,
			@WebParam(name="inputData")String _inputData) throws Exception {
		logger.info("Invoking invoke('" + _serviceID + "', '" + _operationName + "', '" + _inputData +  "')");
		
		List<QName> headers = this.extractHeader(SOAPHeaderThreadLocal.get());
		return InvokerFramework.invoke(_serviceID, headers, _operationName, _inputData);
	}

	@WebMethod
	public String getVersion() {
		logger.info("Invoking getVersion()");
		return "v9999";
	}

}
