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
package at.sti2.ngsee.management.api;

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
 * Date of creation:  9.07.2012<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public interface IManagementEndpoint {

	/** 
	 * 
	 * @return A test string.  
	 * @throws Exception 
	 */
	public String managementTesting() throws Exception;
	
	/**
	 * Deleting a service by giving the URI.
	 * 
	 * @param _serviceURI The URI of service which will be deleted. 
	 * @return true if the service was successfully deleted, otherwise an exception with more detailed information. 
	 */
	public boolean deleteService(String _serviceURI);		
	
	/**
	 * Updating a service by deleting the saved one and registering the updated service.
	 * 
	 * @param _oldServiceURI The URI of the service which need to be updated.
	 * @param _newWsdlURL The URL of the updated service.
	 * @return The service URI of the new updated service.
	 */
	public String updateService(String _oldServiceURI, String _newWsdlURL);
}
