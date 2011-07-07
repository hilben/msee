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
	
	@Override
	@WebMethod
	public String discover(@WebParam(name="categoryList")List<URI> _categoryList) throws Exception {
		return ServiceDiscovery.discover(_categoryList, RDFFormat.RDFXML);
	}
	
	@Override
	@WebMethod
	public String lookup(@WebParam(name="namespace")URI _namespace,
			@WebParam(name="operationName")String _operationName) throws Exception {
		return ServiceDiscovery.lookup(_namespace, _operationName, RDFFormat.RDFXML);
	}
}
