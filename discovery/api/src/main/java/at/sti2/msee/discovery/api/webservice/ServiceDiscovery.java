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
package at.sti2.msee.discovery.api.webservice;

import java.net.URI;
import java.util.List;

import javax.jws.WebService;

/**
 * @author Alex Oberhauser
 *
 */

public interface ServiceDiscovery {
	
	/**
	 * Search Behavior: Conjunction of Categories with subClassOf inferencing (Subclasses match also...)
	 * 
	 * @param categoryList A list of categories. A category is a link to a concept in a taxonomy.
	 * @return Services with related operation (that match the goal) in RDF Representation
	 * @throws Exception
	 */
	public String discover(List<URI> categoryList) throws Exception;
	
	/**
	 * inputs:  disjunction (i1 OR i2 OR i3 OR ... OR oN)
	 * outputs: conjunction (o2 AND o2 AND ... AND oM)<p/>
	 * 
	 * <b>Argumentation for this approach:<b><br/>
	 * I do not care if I need less input as available to reach my goal, 
	 * but I want as output all the information that I specify/need.
	 * 
	 * @param categoryList A list of categories. A category is a link to a concept in a taxonomy.
	 * @param inputParamList A list of input parameters (match mechanism!?!)
	 * @param outputParamList A list of output parameters (match mechanism!?!)
	 * @return Services with related operation (that match the goal) in RDF Representation
	 * @throws Exception
	 */
	public String discover(List<URI> categoryList, List<URI> inputParamList, List<URI> outputParamList) throws Exception;
	
	/**
	 * Get more information about the specified operation. 
	 * 
	 * @param namespace The namespace related to the operation name.
	 * @param operationName The operation name.
	 * @return A RDF Representation of the Operation with Inputs, Outputs, Faults and related modelReferences.
	 * @throws Exception
	 */
	public String lookup(URI namespace, String operationName) throws Exception;
	
	/**
	 * Exports the internal RDF service model as iServe compatible MSM model. (see {@link http://iserve.kmi.open.ac.uk/})
	 * 
	 * @param serviceID The service ID
	 * @return A iServe compatible MSM model (see {@link http://iserve.kmi.open.ac.uk/wiki/index.php/IServe_vocabulary})
	 * @throws Exception
	 */
	public String getIServeModel(String serviceID) throws Exception;
}
