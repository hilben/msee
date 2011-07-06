/**
 * DiscoveryWebService.java - at.sti2.ngsee.discovery.webservice
 */
package at.sti2.ngsee.discovery.webservice;

import java.net.URI;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

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
		return ServiceDiscovery.discover(_categoryList);
	}
	
	@Override
	@WebMethod
	public String lookup(@WebParam(name="operation")URI _operation) throws Exception {
		return null;
	}
}
