package at.sti2.ngsee.invoker.webservice;

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

import at.sti2.ngsee.invoker.core.InvokerCore;
import at.sti2.ngsee.invoker.api.webservice.IAvailabilityCheck;
import at.sti2.ngsee.invoker.api.webservice.IInvokerEndpoint;

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
		@WSDLDocumentation("SESA Invoker Component")
	)
@InInterceptors(interceptors = {"at.sti2.ngsee.invoker.webservice.SOAPInterceptor"})
@InFaultInterceptors(interceptors = {"at.sti2.ngsee.invoker.webservice.SOAPInterceptor"})
public class InvokerWebService implements IInvokerEndpoint, IAvailabilityCheck {
	protected static Logger logger = Logger.getLogger(InvokerWebService.class);
	
	@WebMethod(exclude=true)
	private List<QName> extractHeader(List<Header> _headers) {
		List<QName> header = new ArrayList<QName>();
		for ( Header entry : _headers) {
			header.add(entry.getName());
		}
		return header;
	}
	
	/**
	 * @see at.sti2.ngsee.invoker_api.Invoker#invoke(java.lang.String, java.lang.String)
	 */
	@Override
	@WebMethod
	public String invoke(@WebParam(name="serviceID")String _serviceID,
			@WebParam(name="operation")String _operationName,
			@WebParam(name="inputData")String _inputData) throws Exception {
		logger.info("Invoking invoke('" + _serviceID + "', '" + _operationName + "', '" + _inputData +  "')");

		List<QName> headers = this.extractHeader(SOAPHeaderThreadLocal.get());
		return InvokerCore.invoke(_serviceID, headers, _operationName, _inputData);
	}

	/**
	 * @see at.sti2.ngsee.invoker.api.webservice.IAvailabilityCheck#checkAvailability()
	 */
	@WebMethod
	@Override
	public boolean checkAvailability() {
		return true;
	}

}