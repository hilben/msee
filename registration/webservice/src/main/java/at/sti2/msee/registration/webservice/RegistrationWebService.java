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
package at.sti2.msee.registration.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;

import at.sti2.msee.registration.api.IRegistrationEndpoint;
import at.sti2.msee.registration.core.management.ServiceManagement;
import at.sti2.msee.registration.core.management.TransformationWSDL;

@WebService(targetNamespace="http://msee.sti2.at/delivery/")
@WSDLDocumentationCollection( @WSDLDocumentation("MSEE Registration Service"))
public class RegistrationWebService implements IRegistrationEndpoint
{		
	@Override
	@WebMethod
	public String register(@WebParam(name="wsdlURL")String _wsdlURL) throws Exception {
			return TransformationWSDL.transformWSDL(_wsdlURL);
	}

	@Override
	@WebMethod
	public String delete(@WebParam(name="serviceURI")String _serviceURI) throws Exception {
		return ServiceManagement.delete(_serviceURI);
	}

	@Override
	@WebMethod
	public String update(@WebParam(name="oldServiceURI")String _oldServiceURI, 
			@WebParam(name="newServiceURI")String _newServiceURI)
			throws Exception {
		return ServiceManagement.update(_oldServiceURI, _newServiceURI);
	}
}
