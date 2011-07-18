/**
 * DiscoveryWebService.java - at.sti2.ngsee.discovery.webservice
 */
package at.sti2.ngsee.discovery.webservice;

import java.net.URI;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.openrdf.rio.RDFFormat;

import at.sti2.ngsee.discovery.api.webservice.IDiscoveryWebService;
import at.sti2.ngsee.discovery.core.ServiceDiscovery;

/**
 * @author Alex Oberhauser
 */
@WebService
public class DiscoveryWebService implements IDiscoveryWebService {
	
	/**
	 * @see at.sti2.ngsee.discovery.api.webservice.IDiscoveryWebService#discover(java.util.List)
	 */
	@WebMethod
	@Override
	public String discover(@WebParam(name="categoryList")List<URI> _categoryList) throws Exception {
		return ServiceDiscovery.discover(_categoryList, RDFFormat.RDFXML);
	}
	
	/**
	 * @see at.sti2.ngsee.discovery.api.webservice.IDiscoveryWebService#lookup(java.net.URI, java.lang.String)
	 */
	@WebMethod
	@Override
	public String lookup(@WebParam(name="namespace")URI _namespace,
			@WebParam(name="operationName")String _operationName) throws Exception {
		return ServiceDiscovery.lookup(_namespace, _operationName, RDFFormat.RDFXML);
	}

	/**
	 * @see at.sti2.ngsee.discovery.api.webservice.IDiscoveryWebService#getIServeModel(java.lang.String)
	 */
	@WebMethod
	@Override
	public String getIServeModel(@WebParam(name="serviceID")String _serviceID) throws Exception {
		return ServiceDiscovery.getIServeModel(_serviceID, RDFFormat.RDFXML);
	}
}