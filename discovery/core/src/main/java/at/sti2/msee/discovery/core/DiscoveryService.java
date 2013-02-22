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
package at.sti2.msee.discovery.core;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.UnsupportedRDFormatException;

import at.sti2.msee.discovery.core.common.DiscoveryConfig;
import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilder;
import at.sti2.util.triplestore.RepositoryHandler;

/**
 * TODO: We support SPARQL 1.1: Optimize the SPARQL queries with the help of
 * paths.
 * 
 * TODO: Find further solutions to prettify the code
 * 
 * @author Alex Oberhauser, Benjamin Hiltpolt
 * 
 */
public class DiscoveryService {
	
	private RepositoryHandler repositoryHandler;
	private String resourceLocation = "/default.properties";
	private DiscoveryQueryBuilder discoveryQueryBuilder = new DiscoveryQueryBuilder();
	
	public DiscoveryService() throws FileNotFoundException, IOException{
		repositoryHandler = getReposHandler();
	}

	private RepositoryHandler getReposHandler()
			throws FileNotFoundException, IOException {
		DiscoveryConfig config = new DiscoveryConfig();
		config.setResourceLocation(resourceLocation);
		
		return new RepositoryHandler(config.getSesameEndpoint(),
				config.getSesameRepositoryID(), true);
	}
	
	public void setDiscoveryConfigLocation(String discoveryConfigLocation){
		resourceLocation = discoveryConfigLocation;
		try {
			repositoryHandler = getReposHandler();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * TODO: Unchecked outputParamList, inputParamList
	 * <p/>
	 * 
	 * @param _categoryList
	 * @param inputParamList
	 * @param outputParamList
	 * @param _outputFormat
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws QueryEvaluationException
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws RDFHandlerException
	 * @throws UnsupportedRDFormatException
	 */
	public String discover(List<URI> _categoryList,
			List<URI> _inputParamList, List<URI> _outputParamList,
			RDFFormat _outputFormat) throws FileNotFoundException, IOException,
			QueryEvaluationException, RepositoryException,
			MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {
		
		String query = discoveryQueryBuilder
				.getDiscoverQuery4Args(_categoryList, _inputParamList, _outputParamList);

		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(query);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}

	public String discover(List<URI> _categoryList,
			RDFFormat _outputFormat) throws FileNotFoundException, IOException,
			QueryEvaluationException, RepositoryException,
			MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {

		String query = discoveryQueryBuilder.getDiscoverQuery2Args(_categoryList);

		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(query);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}



	public String lookup(URI _namespace, String _operationName,
			RDFFormat _outputFormat) throws FileNotFoundException, IOException,
			QueryEvaluationException, RepositoryException,
			MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {

		String query = discoveryQueryBuilder.getLookupQuery(_namespace, _operationName);
		
		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(query);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}

	public String getIServeModel(String _serviceID,
			RDFFormat _outputFormat) throws FileNotFoundException, IOException,
			QueryEvaluationException, RepositoryException,
			MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {

		String query = discoveryQueryBuilder.getIServeModelQuery(_serviceID);		

		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(query);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}

}