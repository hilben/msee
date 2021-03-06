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
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.openrdf.query.Binding;
//import org.openrdf.query.BindingSet;
//import org.openrdf.query.GraphQueryResult;
//import org.openrdf.query.MalformedQueryException;
//import org.openrdf.query.QueryEvaluationException;
//import org.openrdf.query.TupleQueryResult;
//import org.openrdf.query.TupleQueryResultHandlerException;
//import org.openrdf.query.resultio.QueryResultIO;
//import org.openrdf.query.resultio.TupleQueryResultFormat;
//import org.openrdf.query.resultio.UnsupportedQueryResultFormatException;
//import org.openrdf.repository.RepositoryException;
//import org.openrdf.rio.RDFFormat;
//import org.openrdf.rio.RDFHandlerException;
//import org.openrdf.rio.UnsupportedRDFormatException;
//
//import at.sti2.msee.discovery.core.common.DiscoveryConfig;
//import at.sti2.msee.discovery.core.common.DiscoveryQueryBuilder;
//import at.sti2.util.triplestore.RepositoryHandler;
//
///**
// * TODO: We support SPARQL 1.1: Optimize the SPARQL queries with the help of
// * paths.
// * 
// * TODO: Find further solutions to prettify the code
// * 
// * @author Alex Oberhauser, Benjamin Hiltpolt
// * 
// */
//
//public class DiscoveryService {
//
//	private final static Logger LOGGER = LogManager
//			.getLogger(DiscoveryService.class.getName());
//
//	private RepositoryHandler repositoryHandler;
//	private String resourceLocation = "/default.properties";
//	private DiscoveryQueryBuilder discoveryQueryBuilder = new DiscoveryQueryBuilder();
//
//	public DiscoveryService() throws FileNotFoundException, IOException {
//		repositoryHandler = getReposHandler();
//	}
//
//	private RepositoryHandler getReposHandler() throws FileNotFoundException,
//			IOException {
//		LOGGER.debug("Building repository handler");
//		DiscoveryConfig config = new DiscoveryConfig();
//		config.setResourceLocation(resourceLocation);
//
//		return new RepositoryHandler(config.getSesameEndpoint(),
//				config.getSesameRepositoryID(), true);
//	}
//
//	public void setDiscoveryConfigLocation(String discoveryConfigLocation) {
//		LOGGER.debug("Configuration set to: " + discoveryConfigLocation);
//		resourceLocation = discoveryConfigLocation;
//		try {
//			repositoryHandler = getReposHandler();
//		} catch (FileNotFoundException e) {
//			LOGGER.catching(e);
//		} catch (IOException e) {
//			LOGGER.catching(e);
//		}
//	}
//
//	/**
//	 * TODO: Unchecked outputParamList, inputParamList
//	 * <p/>
//	 * 
//	 * @param categoryList
//	 * @param inputParamList
//	 * @param outputParamList
//	 * @param outputFormat
//	 * @return
//	 * @throws FileNotFoundException
//	 * @throws IOException
//	 * @throws QueryEvaluationException
//	 * @throws RepositoryException
//	 * @throws MalformedQueryException
//	 * @throws RDFHandlerException
//	 * @throws UnsupportedRDFormatException
//	 */
//	public String discover(List<URI> categoryList, List<URI> inputParamList,
//			List<URI> outputParamList, RDFFormat outputFormat)
//			throws FileNotFoundException, IOException,
//			QueryEvaluationException, RepositoryException,
//			MalformedQueryException, RDFHandlerException,
//			UnsupportedRDFormatException {
//		LOGGER.debug("Starting discover()");
//
//		if (categoryList == null) {
//			LOGGER.error("Category list is null");
//			throw new NullPointerException("Category list is null");
//		}
//		if (categoryList.size() < 1) {
//			LOGGER.error("Category list is empty");
//			throw new IllegalArgumentException("Category list is empty");
//		}
//
//		String query = discoveryQueryBuilder.getDiscoverQuery4Args(
//				categoryList, inputParamList, outputParamList);
//
//		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(query);
//
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		QueryResultIO.write(queryResult, outputFormat, out);
//
//		return out.toString();
//	}
//
//	public String discover(List<URI> categoryList, RDFFormat outputFormat)
//			throws FileNotFoundException, IOException,
//			QueryEvaluationException, RepositoryException,
//			MalformedQueryException, RDFHandlerException,
//			UnsupportedRDFormatException {
//
//		LOGGER.debug("Starting discover()");
//
//		if (categoryList == null) {
//			LOGGER.error("Category list is null");
//			throw new NullPointerException("Category list is null");
//		}
//		if (categoryList.size() < 1) {
//			LOGGER.error("Category list is empty");
//			throw new IllegalArgumentException("Category list is empty");
//		}
//
//		String query = discoveryQueryBuilder
//				.getDiscoverQuery2Args(categoryList);
//
//		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(query);
//
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		QueryResultIO.write(queryResult, outputFormat, out);
//
//		return out.toString();
//	}
//
//	public String lookup(URI namespace, String operationName,
//			RDFFormat _outputFormat) throws FileNotFoundException, IOException,
//			QueryEvaluationException, RepositoryException,
//			MalformedQueryException, RDFHandlerException,
//			UnsupportedRDFormatException {
//		LOGGER.debug("Starting lookup()");
//
//		String query = discoveryQueryBuilder.getLookupQuery(namespace,
//				operationName);
//
//		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(query);
//
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		QueryResultIO.write(queryResult, _outputFormat, out);
//
//		return out.toString();
//	}
//
//	public String getIServeModel(String _serviceID, RDFFormat _outputFormat)
//			throws FileNotFoundException, IOException,
//			QueryEvaluationException, RepositoryException,
//			MalformedQueryException, RDFHandlerException,
//			UnsupportedRDFormatException {
//		LOGGER.debug("Starting getIServeModel()");
//
//		String query = discoveryQueryBuilder.getIServeModelQuery(_serviceID);
//
//		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(query);
//
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		QueryResultIO.write(queryResult, _outputFormat, out);
//
//		return out.toString();
//	}
//
//	public boolean alreadyInTripleStore(String _serviceID)
//			throws QueryEvaluationException, RepositoryException,
//			MalformedQueryException, TupleQueryResultHandlerException,
//			UnsupportedQueryResultFormatException, IOException {
//		LOGGER.debug("Starting alreadyInTripleStore()");
//		String query = discoveryQueryBuilder.getServiceCount(_serviceID);
//		TupleQueryResult queryResult = repositoryHandler.selectSPARQL(query);
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		QueryResultIO.write(queryResult, TupleQueryResultFormat.JSON, out);
//		String beginNum = out.toString().substring(
//				out.toString().indexOf("\"num\": {"));
//		String beginValue = beginNum.substring(beginNum.indexOf("value")
//				+ "value\": \"".length());
//		int num = Integer.valueOf(beginValue.substring(0,
//				beginValue.indexOf("\"")));
//		LOGGER.debug("Number of occurances (" + _serviceID + "): " + num);
//		return (num > 0) ? true : false;
//	}
//
//	/**
//	 * Returns a list containing all categories of a given service TODO: Dummy
//	 * function only returning testvalues
//	 * 
//	 * @param serviceID
//	 * @return
//	 * @throws MalformedQueryException 
//	 * @throws RepositoryException 
//	 * @throws QueryEvaluationException 
//	 * @throws IOException 
//	 * @throws UnsupportedQueryResultFormatException 
//	 * @throws TupleQueryResultHandlerException 
//	 */
//	public String[] getServiceCategories() throws QueryEvaluationException, RepositoryException, MalformedQueryException, TupleQueryResultHandlerException, UnsupportedQueryResultFormatException, IOException {
//		String query = discoveryQueryBuilder.getAllCategoriesQuery();
//
//		TupleQueryResult queryResult = repositoryHandler.selectSPARQL(query);
//		
//		LOGGER.debug("Querying all categories via");
//		LOGGER.debug(query);
//		
//		ArrayList<String> categories = new ArrayList<String>();
//		
//    	while (queryResult.hasNext()) {
//    		BindingSet nextElement = queryResult.next();
//    		Binding binding = nextElement.getBinding("category");
//			String category = binding.getValue().stringValue();
//			categories.add(category);
//			LOGGER.debug("Category : " + category);
//		}
//    	
//    	//Cast to array
//    	String returnArray[] = new String[categories.size()];
//    	int i = 0;
//    	for (String c : categories) {
//    		returnArray[i] = c;
//    		i++;
//    	}
//    	
//		return returnArray;
//	}
//
//}
