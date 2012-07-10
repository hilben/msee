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
	 * 
	 * @param _ontologyURL The URL of the ontology which should be added.
	 * @return The URL of the added ontology
	 * @throws Exception 
	 */
	public String addOntology(String _ontologyURL) throws Exception;
	
	/**
	 * Deleting a service by giving the URI.
	 * 
	 * @param _ontologyURI The URL of ontology which will be deleted. 
	 * @return true if the ontology was successfully deleted, otherwise an exception with more detailed information. 
	 */
	public boolean deleteOntology(String _ontologyURL) throws Exception;		
	
	/**
	 * Updating a ontology by deleting the saved one and adding the updated ontology.
	 * 
	 * @param _oldOntologyURI The URI of the ontology which need to be updated.
	 * @param _newOntologyURL The URL of the updated ontology.
	 * @return The URL of the updated ontology.
	 */
	public String updateOntology(String _oldOntologyURL, String _newOntologyURL) throws Exception;
}
