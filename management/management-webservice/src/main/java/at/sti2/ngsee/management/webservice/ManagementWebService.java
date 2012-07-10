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
package at.sti2.ngsee.management.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;

import at.sti2.ngsee.management.api.IManagementEndpoint;
import at.sti2.ngsee.management.service.ServiceManagement;

/**
 * 
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>    Copyright (c) 2012 STI<br>
 * <b>Company:</b>      STI Innsbruck<br>
 *
 * @author      		Corneliu Stanciu<br>
 * @version     		$Id$<br>
 * Date of creation:  	9.07.2012<br>
 * File:         		$Source$<br>
 * Modifier:     		$Author$<br>
 * Last modified:  		9.07.2012<br>
 * Revision:     		$Revision$<br>
 * State:        		$State$<br>
 */

@WebService(targetNamespace="http://sesa.sti2.at/services/")
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA Management Component")
	)
public class ManagementWebService implements IManagementEndpoint
{	
	/**
	 * @throws Exception 
	 * @see at.sti2.ngsee.management.api.IManagementEndpoint#addOntology
	 */
	@Override
	@WebMethod
	public String addOntology(@WebParam(name="ontologyURL")String _ontologyURL) throws Exception {
		return  ServiceManagement.add(_ontologyURL);
	}

	/**
	 * @throws Exception 
	 * @see at.sti2.ngsee.management.api.IManagementEndpoint#deleteOntology
	 */
	@Override
	@WebMethod
	public boolean deleteOntology(@WebParam(name="ontologyURL")String _ontologyURI) throws Exception {
		return ServiceManagement.delete(_ontologyURI);
	}

	/**
	 * @throws Exception 
	 * @see at.sti2.ngsee.management.api.IManagementEndpoint#updateOntology
	 */
	@Override
	@WebMethod
	public String updateOntology(
				@WebParam(name="oldOntologyURL")String _oldOntologyURI, 
				@WebParam(name="newOntologyURL")String _newOntologyURL) throws Exception {		
		return ServiceManagement.update(_oldOntologyURI, _newOntologyURL);
	}
	
	/**
	 * @throws Exception 
	 * @see at.sti2.ngsee.management.api.IManagementEndpoint#managementTesting
	 */
	@Override
	@WebMethod
	public String managementTesting() throws Exception {
			return "Testing";
	}
}
