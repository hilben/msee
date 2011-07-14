/**
 * IDiscoveryWebService.java - at.sti2.ngsee.discovery.api.webservice
 */
package at.sti2.ngsee.discovery.api.webservice;

import java.net.URI;
import java.util.List;

/**
 * @author Alex Oberhauser
 *
 */
public interface IDiscoveryWebService {
	
	/**
	 * Search Behaviour: Conjunction of Categories with subClassOf inferencing (Subclasses match also...)
	 * 
	 * @param _categoryList A list of categories. A category is a link to a concept in a taxonomy.
	 * @return Services with related operation (that match the goal) in RDF Representation
	 * @throws Exception
	 */
	public String discover(List<URI> categoryList) throws Exception;
	
	/**
	 * Get more information about the operation. 
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
