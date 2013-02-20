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

import javax.jws.WebService;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.management.ServiceManagement;
import at.sti2.msee.registration.core.management.TransformationWSDL;

@WebService(targetNamespace="http://msee.sti2.at/delivery/",
endpointInterface="at.sti2.msee.registration.webservice.Registration")
public class RegistrationImpl implements Registration
{
	public String register(String serviceDescriptionURL) throws ServiceRegistrationException {
		return TransformationWSDL.transformWSDL(serviceDescriptionURL);		
	}
	
	public String deregister(String serviceURI) throws ServiceRegistrationException {
		return ServiceManagement.delete(serviceURI);
	}

	public String update(String serviceURI, String serviceURL) throws ServiceRegistrationException {
		return ServiceManagement.update(serviceURI, serviceURL);
	}
}
