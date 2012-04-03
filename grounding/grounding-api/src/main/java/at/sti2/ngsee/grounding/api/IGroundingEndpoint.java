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
package at.sti2.ngsee.grounding.api;

import at.sti2.ngsee.grounding.api.data.DebugResponse;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Alex Oberhauser<br>
 * @version     $Id$<br>
 * Date of creation:  17.03.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public interface IGroundingEndpoint {
	
	/**
	 * Invokes a service and handles the lifting and lowering process and all 
	 * low level processes needed for the invocation process.
	 * 
	 * @param serviceID The abstract Identifier of the service.
	 * @param operationName The operation that should be called.
	 * @param rdfData The data described in RDF.
	 * @return
	 */
	public String transform(String inputMessage, String xsltToOntology, String xsltToOutput) throws Exception;
	
	public DebugResponse transform_debug(String inputMessage, String xsltToOntology, String xsltToOutput) throws Exception;

}
