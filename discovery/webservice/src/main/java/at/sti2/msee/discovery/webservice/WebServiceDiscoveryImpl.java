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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openrdf.rio.UnsupportedRDFormatException;

import at.sti2.msee.config.Config;
import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.ServiceDiscoveryFactory;

/**
 * @author Alex Oberhauser
 */
@WebService(targetNamespace = "http://msee.sti2.at/delivery/discovery/", endpointInterface = "at.sti2.msee.discovery.api.webservice.Discovery", portName = "DiscoveryServicePort", serviceName = "service")
public class WebServiceDiscoveryImpl implements Discovery {
	private final static Logger LOGGER = LogManager.getLogger(WebServiceDiscoveryImpl.class
			.getName());
	private Config config = Config.INSTANCE;
	private Discovery discoveryDelegate;

	public WebServiceDiscoveryImpl() throws IOException {
		init();
	}

	public WebServiceDiscoveryImpl(Discovery discovery) {
		this.discoveryDelegate = discovery;
	}

	private void init() {
		String repositoryId = config.getRepositoryID();
		String serverEndpoint = config.getRepositoryEndpoint();

		discoveryDelegate = ServiceDiscoveryFactory.createDiscoveryService(serverEndpoint,
				repositoryId);
	}

	@SuppressWarnings("unused")
	@Deprecated
	private static List<URI> stringArray2UriList(String[] stringArray) throws URISyntaxException {
		List<URI> uriList = new ArrayList<URI>();
		for (String s : stringArray) {
			uriList.add(new URI(s));
		}
		return uriList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.msee.discovery.api.webservice.Discovery#discover(java.lang.String
	 * [])
	 */
	@WebMethod(operationName = "discover")
	public String discover(@WebParam(name = "categoryList") String[] categoryList)
			throws DiscoveryException {
		LOGGER.debug("Method discover invoked with category list of size " + categoryList.length);
		try {
			return discoveryDelegate.discover(categoryList);
		} catch (UnsupportedRDFormatException e) {
			throw new DiscoveryException(e);
		}
	}

//	@Override
//	public String discoverAdvanced(String[] categoryList, String[] inputParamList,
//			String[] outputParamList) throws DiscoveryException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String lookup(String namespace, String operationName) throws DiscoveryException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getIServeModel(String serviceID) throws DiscoveryException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.msee.discovery.api.webservice.Discovery#discoverAdvanced(java
	 * .lang.String[], java.lang.String[], java.lang.String[])
	 */
	// public String discoverAdvanced(String[] categoryList,
	// String[] inputParamList, String[] outputParamList)
	// throws DiscoveryException {
	// LOGGER.debug("Method discover invoked");
	// try {
	// return serviceDiscovery.discover(stringArray2UriList(categoryList),
	// stringArray2UriList(inputParamList),
	// stringArray2UriList(outputParamList), RDFFormat.RDFXML);
	// } catch (QueryEvaluationException
	// | RepositoryException | MalformedQueryException
	// | RDFHandlerException | UnsupportedRDFormatException
	// | IOException | URISyntaxException e) {
	// throw new DiscoveryException(e);
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.msee.discovery.api.webservice.Discovery#lookup(java.lang.String,
	 * java.lang.String)
	 */
	// public String lookup(String namespace, String operationName)
	// throws DiscoveryException {
	// LOGGER.debug("Method lookup invoked with namespace " + namespace +
	// " and " +
	// "operation " + operationName);
	// try {
	// return serviceDiscovery.lookup(new URI(namespace), operationName,
	// RDFFormat.RDFXML);
	// } catch (QueryEvaluationException
	// | RepositoryException | MalformedQueryException
	// | RDFHandlerException | UnsupportedRDFormatException
	// | IOException | URISyntaxException e) {
	// throw new DiscoveryException(e);
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.sti2.msee.discovery.api.webservice.Discovery#getIServeModel(java.lang
	 * .String)
	 */
	// public String getIServeModel(String serviceID)
	// throws DiscoveryException {
	// LOGGER.debug("Method getIServeModel invoked with serviceID: " +
	// serviceID);
	// try {
	// return serviceDiscovery.getIServeModel("\""+serviceID+"\"",
	// RDFFormat.RDFXML);
	// } catch (QueryEvaluationException
	// | RepositoryException | MalformedQueryException
	// | RDFHandlerException | UnsupportedRDFormatException
	// | IOException e) {
	// throw new DiscoveryException(e);
	// }
	// }

}
