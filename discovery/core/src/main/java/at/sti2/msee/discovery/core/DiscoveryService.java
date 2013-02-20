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

import org.antlr.stringtemplate.StringTemplate;
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

		StringTemplate transQuery = new StringTemplate(

				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
						+ "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n"
						+ "PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n"
						+ "PREFIX msm:<http://cms-wg.sti2.org/ns/minimal-service-model#> \n"
						+ "PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n"
						+ "PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n"
						+

						"CONSTRUCT { \n"
						+ "<_serviceID> rdf:type msm:Service . \n"
						+ "<_serviceID> rdfs:label ?serviceLabel . \n"
						+ "<_serviceID> rdfs:isDefinedBy ?wsdlLink . \n"
						+ "<_serviceID> dc:creator ?creator . \n"
						+ "<_serviceID> sawsdl:modelReference ?serviceModel . \n"
						+ "<_serviceID> msm:hasOperation ?operation . \n"
						+

						"?operation rdf:type msm:Operation . \n"
						+ "?operation rdfs:label ?operationLabel . \n"
						+ "?operation sawsdl:modelReference ?operationModel . \n"
						+

						"?operation msm:hasInput ?inputMessage . \n"
						+ "?inputMessage rdf:type msm:MessageContent . \n"
						+ "?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n"
						+ "?inputMessage msm:hasPart ?inputMessagePart . \n"
						+ "?inputMessagePart rdf:type msm:MessagePart . \n"
						+ "?inputMessagePart msm:hasName ?inputMessagePartName . \n"
						+

						"?operation msm:hasOutput ?outputMessage . \n"
						+ "?outputMessage rdf:type msm:MessageContent . \n"
						+ "?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n"
						+ "?outputMessage msm:hasPart ?outputMessagePart . \n"
						+ "?outputMessagePart rdf:type msm:MessagePart . \n"
						+ "?outputMessagePart msm:hasName ?outputMessagePartName . \n"
						+

						"?operation msm:hasInputFault ?inputFaultMessage . \n"
						+ "?inputFaultMessage rdf:type msm:MessageContent . \n"
						+ "?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n"
						+ "?inputFaultMessage msm:hasPart ?inputFaultMessagePart . \n"
						+ "?inputFaultMessagePart rdf:type msm:MessagePart . \n"
						+ "?inputFaultMessagePart msm:hasName ?inputFaultMessagePartName . \n"
						+

						"?operation msm:hasOutputFault ?outputFaultMessage . \n"
						+ "?outputFaultMessage rdf:type msm:MessageContent . \n"
						+ "?outputFaultMessage msm:hasPart ?outputFaultMessagePart . \n"
						+ "?outputFaultMessagePart rdf:type msm:MessagePart . \n"
						+ "?outputFaultMessagePart msm:hasName ?outputFaultMessagePartName . \n"
						+

						"} WHERE { \n"
						+

						"<_serviceID> rdf:type msm_ext:Service . \n"
						+ "<_serviceID> rdfs:label ?serviceLabel . \n"
						+ "<_serviceID> rdfs:isDefinedBy ?wsdlLink . \n"
						+ "OPTIONAL { \n"
						+ "<_serviceID> dc:creator ?creator . \n"
						+ "} \n"
						+ "OPTIONAL { \n"
						+ "<_serviceID> sawsdl:modelReference ?serviceModel . \n"
						+ "} \n"
						+ "<_serviceID> msm_ext:wsdlDescription ?descriptionBlock . \n"
						+

						"?descriptionBlock wsdl:interface ?interfaceBlock . \n"
						+

						"?interfaceBlock wsdl:interfaceOperation ?operation . \n"
						+ "?operation rdfs:label ?operationLabel . \n"
						+

						"OPTIONAL { \n"
						+ "?operation wsdl:interfaceMessageReference ?inputMessage . \n"
						+ "OPTIONAL { \n"
						+ "?operation sawsdl:modelReference ?operationModel . \n"
						+ "} \n"
						+ "?inputMessage rdf:type wsdl:InputMessage . \n"
						+ "?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n"
						+ "?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n"
						+ "?inputMessagePart wsdl:localName ?inputMessagePartName . \n"
						+ "OPTIONAL { \n"
						+ "?inputMessagePart sawsdl:modelReference ?inputMessagePartModel . \n"
						+ "} \n"
						+ "} \n"
						+

						"OPTIONAL { \n"
						+ "?operation wsdl:interfaceMessageReference ?outputMessage . \n"
						+ "?outputMessage rdf:type wsdl:OutputMessage . \n"
						+ "?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n"
						+ "?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n"
						+ "?outputMessagePart wsdl:localName ?outputMessagePartName . \n"
						+ "OPTIONAL { \n"
						+ "?outputMessagePart sawsdl:modelReference ?outputMessagePartModel . \n"
						+ "} \n"
						+ "} \n"
						+

						"OPTIONAL { \n"
						+ "?operation wsdl:interfaceFaultReference ?inputFaultMessage . \n"
						+ "?inputFaultMessage rdf:type wsdl:InputMessage . \n"
						+ "?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n"
						+ "?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n"
						+ "?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n"
						+ "OPTIONAL { \n"
						+ "?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n"
						+ "} \n"
						+ "} \n"
						+

						"OPTIONAL { \n"
						+ "?operation wsdl:interfaceFaultReference ?outputFaultMessage . \n"
						+ "?outputFaultMessage rdf:type wsdl:OutputMessage . \n"
						+ "?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n"
						+ "?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n"
						+ "?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n"
						+ "OPTIONAL { \n"
						+ "?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n"
						+ "} \n" + "} \n" +

						"}");
		
		transQuery.setAttribute("_serviceID", _serviceID);

		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(transQuery
				.toString());

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}

}
