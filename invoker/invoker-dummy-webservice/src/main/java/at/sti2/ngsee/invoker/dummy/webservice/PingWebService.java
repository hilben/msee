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
package at.sti2.ngsee.invoker.dummy.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.apache.log4j.Logger;

@WebService(targetNamespace="http://sesa.sti2.at/services/dummy/")
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA Example Service")
	)
public class PingWebService {
	protected static Logger logger = Logger.getLogger(PingWebService.class);
	
	@WebMethod
	public String ping(@WebParam(name="serviceID")String msg){
		return "Ping Pong "+msg;
	}

}
