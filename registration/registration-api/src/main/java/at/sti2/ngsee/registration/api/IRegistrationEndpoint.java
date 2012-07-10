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
package at.sti2.ngsee.registration.api;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Corneliu Stanciu<br>
 * @version     $Id$<br>
 * Date of creation:  17.03.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public interface IRegistrationEndpoint {

	/**
	 * Register a WSDL service. The service MUST contain annotations in order to be stored into the triple store.
	 * For more info see  http://www.sesa.sti2.at/doc/service_annotation
	 * 
	 * @param _serviceURI The URI of the Web Service description (WSDL).
	 * @return The URI through which the service can be deleted or updated.
	 * @throws Exception
	 */
	public String register(String _serviceURI) throws Exception;
	
	/**
	 * Delete a service from triple store.
	 * 
	 * @param _serviceURI The URI of the service which needs to be deleted.
	 * @return The URI of the deleted service, if the process was successful, otherwise will throw an exception. 
	 * @throws Exception
	 */
	public String delete(String _serviceURI) throws Exception;
	
	/**
	 * Update a service by deleting the old one and registering the new service.
	 * 
	 * @param _oldServiceURI The URI of the service which needs to be updated.
	 * @param _newServiceURI The URI of the service which includes the updates.
	 * @return The URI through which the service can be deleted or updated.
	 * @throws Exception
	 */
	public String update(String _oldServiceURI, String _newServiceURI) throws Exception;
	
}
