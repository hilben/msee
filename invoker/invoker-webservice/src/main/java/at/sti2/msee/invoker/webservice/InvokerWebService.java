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
package at.sti2.msee.invoker.webservice;

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

import at.sti2.msee.invoker.api.webservice.IAvailabilityCheck;
import at.sti2.msee.invoker.api.webservice.IInvokerEndpoint;
import at.sti2.msee.invoker.core.InvokerCore;

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

@WebService(targetNamespace="http://sesa.sti2.at/services/")
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA Invoker Component")
	)
@InInterceptors(interceptors = {"at.sti2.msee.invoker.webservice.SOAPInterceptor"})
@InFaultInterceptors(interceptors = {"at.sti2.msee.invoker.webservice.SOAPInterceptor"})
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
	 * @see at.sti2.msee.invoker_api.Invoker#invoke(java.lang.String, java.lang.String)
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
	 * @see at.sti2.msee.invoker.api.webservice.IAvailabilityCheck#checkAvailability()
	 */
	@WebMethod
	@Override
	public boolean checkAvailability() {
		return true;
	}

}
