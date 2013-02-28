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
package at.sti2.msee.discovery.webservice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.openrdf.rio.RDFFormat;

import at.sti2.msee.discovery.api.webservice.ServiceDiscovery;
import at.sti2.msee.discovery.core.DiscoveryService;

/**
 * @author Alex Oberhauser
 */
@WebService(targetNamespace = "http://sesa.sti2.at/services/")
@WSDLDocumentationCollection(@WSDLDocumentation("SESA Discovery Component"))
public class DiscoveryWebServiceImpl implements ServiceDiscovery {
	
	private DiscoveryService serviceDiscovery;

	public DiscoveryWebServiceImpl () throws FileNotFoundException, IOException {
		serviceDiscovery = new DiscoveryService();
	}

	/**
	 * @see at.sti2.msee.discovery.api.webservice.DiscoveryWebService#discover(java.util.List)
	 */
	@WebMethod
	@Override
	public String discover(
			@WebParam(name = "categoryList") List<URI> _categoryList)
			throws Exception {
		return serviceDiscovery.discover(_categoryList, RDFFormat.RDFXML);
	}

	/**
	 * @see at.sti2.msee.discovery.api.webservice.DiscoveryWebService#discover(java.util.List,
	 *      java.util.List, java.util.List)
	 */
	@WebMethod(operationName = "discoverAdvanced")
	@Override
	public String discover(List<URI> _categoryList, List<URI> _inputParamList,
			List<URI> _outputParamList) throws Exception {
		return serviceDiscovery.discover(_categoryList, _inputParamList,
				_outputParamList, RDFFormat.RDFXML);
	}

	/**
	 * @see at.sti2.msee.discovery.api.webservice.DiscoveryWebService#lookup(java.net.URI,
	 *      java.lang.String)
	 */
	@WebMethod
	@Override
	public String lookup(@WebParam(name = "namespace") URI _namespace,
			@WebParam(name = "operationName") String _operationName)
			throws Exception {
		return serviceDiscovery.lookup(_namespace, _operationName,
				RDFFormat.RDFXML);
	}

	/**
	 * @see at.sti2.msee.discovery.api.webservice.DiscoveryWebService#getIServeModel(java.lang.String)
	 */
	@WebMethod
	@Override
	public String getIServeModel(@WebParam(name = "serviceID") String _serviceID)
			throws Exception {
		return serviceDiscovery.getIServeModel("\""+_serviceID+"\"", RDFFormat.RDFXML);
	}

}
