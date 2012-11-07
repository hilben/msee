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
package at.sti2.ngsee.discovery.core;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.UnsupportedRDFormatException;

import at.sti2.util.triplestore.RepositoryHandler;

/**
 * TODO: We support SPARQL 1.1: Optimize the SPARQL queries with the help of paths.
 * 
 * @author Alex Oberhauser
 * 
 * @author: Benjamin Hiltpolt
 */
public class ServiceDiscovery {
	
	public static RepositoryHandler getReposHandler() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(ServiceDiscovery.class.getResourceAsStream("/default.properties"));
		//TODO: MAYBE DISABLE AUTOCOMMIT
		return new RepositoryHandler(prop.getProperty("sesame.endpoint"), prop.getProperty("sesame.reposid"),true);
	}
	
	/**
	 * TODO: Unchecked outputParamList, inputParamList<p/>
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
	public static String discover(List<URI> _categoryList, List<URI> _inputParamList, List<URI> _outputParamList, RDFFormat _outputFormat) throws FileNotFoundException, IOException, QueryEvaluationException, RepositoryException, MalformedQueryException, RDFHandlerException, UnsupportedRDFormatException {
		RepositoryHandler reposHandler = getReposHandler();
		
		StringBuffer discoveryQuery = new StringBuffer();

		discoveryQuery.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		discoveryQuery.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		discoveryQuery.append("PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n");
		discoveryQuery.append("PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n");
		discoveryQuery.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");

		discoveryQuery.append("CONSTRUCT { \n");
		discoveryQuery.append("?serviceID msm_ext:hasOperation ?inputMessage . \n");
		
		discoveryQuery.append("?inputMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery.append("?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n");
		discoveryQuery.append("?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n");
		discoveryQuery.append("?inputMessagePart wsdl:localName ?inputMessagePartName . \n");
		discoveryQuery.append("?inputMessagePart sawsdl:modelReference ?inputMessagePartModel . \n");

		discoveryQuery.append("?serviceID msm_ext:hasOperation ?outputMessage . \n");
		discoveryQuery.append("?outputMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery.append("?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n");
		discoveryQuery.append("?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n");
		discoveryQuery.append("?outputMessagePart wsdl:localName ?outputMessagePartName . \n");

		discoveryQuery.append("?serviceID msm_ext:hasOperation ?inputFaultMessage . \n");
		discoveryQuery.append("?inputFaultMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery.append("?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n");
		discoveryQuery.append("?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n");
		discoveryQuery.append("?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n");
		discoveryQuery.append("?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n");
				
		discoveryQuery.append("?serviceID msm_ext:hasOperation ?outputFaultMessage . \n");
		discoveryQuery.append("?outputFaultMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery.append("?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n");
		discoveryQuery.append("?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n");
		discoveryQuery.append("?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n");
		discoveryQuery.append("?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n");
		
		discoveryQuery.append("} WHERE { \n");
		discoveryQuery.append("?serviceID rdf:type msm_ext:Service . \n");
		for ( URI category : _categoryList ) {
			discoveryQuery.append("{ \n");
			discoveryQuery.append("<" + category + "> rdfs:subClassOf* ?superClass . \n");
			discoveryQuery.append("?serviceID sawsdl:modelReference ?superClass . \n");
			discoveryQuery.append("} UNION { \n");
			discoveryQuery.append("?serviceID sawsdl:modelReference <" + category + "> . \n");
			discoveryQuery.append("} \n");
		}
		discoveryQuery.append("?serviceID msm_ext:wsdlDescription ?descriptionBlock . \n");
		discoveryQuery.append("?descriptionBlock wsdl:namespace ?namespace . \n");
		discoveryQuery.append("?descriptionBlock wsdl:interface ?interfaceBlock . \n");
		discoveryQuery.append("?interfaceBlock wsdl:interfaceOperation ?operation . \n");
		discoveryQuery.append("?operation rdfs:label ?operationName . \n");
		
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation wsdl:interfaceMessageReference ?inputMessage . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation sawsdl:modelReference ?operationModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("?inputMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery.append("?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n");
		discoveryQuery.append("?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n");
		discoveryQuery.append("?inputMessagePart wsdl:localName ?inputMessagePartName . \n");
//		discoveryQuery.append("OPTIONAL { \n");
		
		int count = 0;
		for ( URI inputParam : _inputParamList ) {
			discoveryQuery.append("{ ?inputMessagePart sawsdl:modelReference <" + inputParam + "> . } \n");
			count++;
			if ( count != _inputParamList.size() )
				discoveryQuery.append(" UNION \n");
		}
		
//		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");
		
		if ( _outputParamList.size() == 0 )
			discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation wsdl:interfaceMessageReference ?outputMessage . \n");
		discoveryQuery.append("?outputMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery.append("?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n");
		discoveryQuery.append("?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n");
		discoveryQuery.append("?outputMessagePart wsdl:localName ?outputMessagePartName . \n");
//		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?outputMessagePart sawsdl:modelReference ?outputMessagePartModel . \n");

		for ( URI outputParam : _outputParamList ) {
			discoveryQuery.append("?outputMessagePart sawsdl:modelReference <" + outputParam + "> . \n");
		}
		
//		discoveryQuery.append("} \n");
		
		if ( _outputParamList.size() == 0 )
			discoveryQuery.append("} \n");
		
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation wsdl:interfaceFaultReference ?inputFaultMessage . \n");
		discoveryQuery.append("?inputFaultMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery.append("?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n");
		discoveryQuery.append("?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n");
		discoveryQuery.append("?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");

		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation wsdl:interfaceFaultReference ?outputFaultMessage . \n");
		discoveryQuery.append("?outputFaultMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery.append("?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n");
		discoveryQuery.append("?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n");
		discoveryQuery.append("?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");
		
		discoveryQuery.append("}");

		GraphQueryResult queryResult = reposHandler.constructSPARQL(discoveryQuery.toString());

		ByteArrayOutputStream out =  new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);
		
		return out.toString();
	}
	
	public static String discover(List<URI> _categoryList, RDFFormat _outputFormat) throws FileNotFoundException, IOException, QueryEvaluationException, RepositoryException, MalformedQueryException, RDFHandlerException, UnsupportedRDFormatException {
		RepositoryHandler reposHandler = getReposHandler();
		
		StringBuffer discoveryQuery = new StringBuffer();

		discoveryQuery.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		discoveryQuery.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		discoveryQuery.append("PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n");
		discoveryQuery.append("PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n");
		discoveryQuery.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");

		discoveryQuery.append("CONSTRUCT { \n");
		discoveryQuery.append("?serviceID msm_ext:hasOperation ?inputMessage . \n");
		
		discoveryQuery.append("?inputMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery.append("?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n");
		discoveryQuery.append("?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n");
		discoveryQuery.append("?inputMessagePart wsdl:localName ?inputMessagePartName . \n");
		discoveryQuery.append("?inputMessagePart sawsdl:modelReference ?inputMessagePartModel . \n");

		discoveryQuery.append("?serviceID msm_ext:hasOperation ?outputMessage . \n");
		discoveryQuery.append("?outputMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery.append("?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n");
		discoveryQuery.append("?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n");
		discoveryQuery.append("?outputMessagePart wsdl:localName ?outputMessagePartName . \n");
		discoveryQuery.append("?outputMessagePart sawsdl:modelReference ?outputMessagePartModel . \n");
		
		discoveryQuery.append("?serviceID msm_ext:hasOperation ?inputFaultMessage . \n");
		discoveryQuery.append("?inputFaultMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery.append("?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n");
		discoveryQuery.append("?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n");
		discoveryQuery.append("?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n");
		discoveryQuery.append("?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n");
				
		discoveryQuery.append("?serviceID msm_ext:hasOperation ?outputFaultMessage . \n");
		discoveryQuery.append("?inputFaultMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery.append("?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n");
		discoveryQuery.append("?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n");
		discoveryQuery.append("?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n");
		discoveryQuery.append("?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n");
		
		discoveryQuery.append("} WHERE { \n");
		discoveryQuery.append("?serviceID rdf:type msm_ext:Service . \n");
		for ( URI category : _categoryList ) {
			/**
			 * TODO: Check this part.
			 */
			discoveryQuery.append("{ \n");
			discoveryQuery.append("<" + category + "> rdfs:subClassOf* ?superClass . \n");
			discoveryQuery.append("?serviceID sawsdl:modelReference ?superClass . \n");
			discoveryQuery.append("} UNION { \n");
			discoveryQuery.append("?serviceID sawsdl:modelReference <" + category + "> . \n");
			discoveryQuery.append("} \n");
		}
		discoveryQuery.append("?serviceID msm_ext:wsdlDescription ?descriptionBlock . \n");
		discoveryQuery.append("?descriptionBlock wsdl:namespace ?namespace . \n");
		discoveryQuery.append("?descriptionBlock wsdl:interface ?interfaceBlock . \n");
		discoveryQuery.append("?interfaceBlock wsdl:interfaceOperation ?interfaceOperation . \n");
		discoveryQuery.append("?interfaceOperation rdfs:label ?operationName . \n");
		
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation wsdl:interfaceMessageReference ?inputMessage . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation sawsdl:modelReference ?operationModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("?inputMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery.append("?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n");
		discoveryQuery.append("?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n");
		discoveryQuery.append("?inputMessagePart wsdl:localName ?inputMessagePartName . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?inputMessagePart sawsdl:modelReference ?inputMessagePartModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");
		
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation wsdl:interfaceMessageReference ?outputMessage . \n");
		discoveryQuery.append("?outputMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery.append("?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n");
		discoveryQuery.append("?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n");
		discoveryQuery.append("?outputMessagePart wsdl:localName ?outputMessagePartName . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?outputMessagePart sawsdl:modelReference ?outputMessagePartModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");
		
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation wsdl:interfaceFaultReference ?inputFaultMessage . \n");
		discoveryQuery.append("?inputFaultMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery.append("?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n");
		discoveryQuery.append("?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n");
		discoveryQuery.append("?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");

		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?operation wsdl:interfaceFaultReference ?outputFaultMessage . \n");
		discoveryQuery.append("?outputFaultMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery.append("?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n");
		discoveryQuery.append("?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n");
		discoveryQuery.append("?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery.append("?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");
		
		discoveryQuery.append("}");

		GraphQueryResult queryResult = reposHandler.constructSPARQL(discoveryQuery.toString());

		ByteArrayOutputStream out =  new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}

	public static String lookup(URI _namespace, String _operationName, RDFFormat _outputFormat) throws FileNotFoundException, IOException, QueryEvaluationException, RepositoryException, MalformedQueryException, RDFHandlerException, UnsupportedRDFormatException {
		RepositoryHandler reposHandler = getReposHandler();

		StringBuffer lookupQuery = new StringBuffer();

		lookupQuery.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		lookupQuery.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		lookupQuery.append("PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n");
		lookupQuery.append("PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n");
		lookupQuery.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");

		lookupQuery.append("CONSTRUCT { \n");
		lookupQuery.append("?messageReference ?p ?o . \n");
		lookupQuery.append("?faultMessageReference ?p1 ?o1 . \n");
		lookupQuery.append("} WHERE { \n");

		lookupQuery.append("?serviceID rdf:type msm_ext:Service . \n");
		lookupQuery.append("?serviceID msm_ext:wsdlDescription ?descriptionBlock . \n");
		lookupQuery.append("?descriptionBlock wsdl:namespace <" + _namespace + "> . \n");
		lookupQuery.append("?descriptionBlock wsdl:interface ?interfaceBlock . \n");
		lookupQuery.append("?interfaceBlock wsdl:interfaceOperation ?interfaceOperation . \n");
		lookupQuery.append("?interfaceOperation wsdl:interfaceMessageReference ?messageReference . \n");
		lookupQuery.append("?interfaceOperation rdfs:label \"" + _operationName + "\" . \n");
		lookupQuery.append("?messageReference ?p ?o . \n");
		lookupQuery.append("OPTIONAL { ?interfaceOperation wsdl:interfaceFaultReference ?faultMessageReference . \n");
		lookupQuery.append("?faultMessageReference ?p1 ?o1 . } \n");

		lookupQuery.append("}");
		
		GraphQueryResult queryResult = reposHandler.constructSPARQL(lookupQuery.toString());

		ByteArrayOutputStream out =  new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}

	public static String getIServeModel(String _serviceID, RDFFormat _outputFormat) throws FileNotFoundException, IOException, QueryEvaluationException, RepositoryException, MalformedQueryException, RDFHandlerException, UnsupportedRDFormatException {
		RepositoryHandler reposHandler = getReposHandler();
		
		StringBuffer transQuery = new StringBuffer();

		transQuery.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		transQuery.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		transQuery.append("PREFIX dc: <http://purl.org/dc/elements/1.1/> \n");
		transQuery.append("PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n");
		transQuery.append("PREFIX msm:<http://cms-wg.sti2.org/ns/minimal-service-model#> \n");
		transQuery.append("PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n");
		transQuery.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");
		
		transQuery.append("CONSTRUCT { \n");
		transQuery.append("<" + _serviceID + "> rdf:type msm:Service . \n");
		transQuery.append("<" + _serviceID + "> rdfs:label ?serviceLabel . \n");
		transQuery.append("<" + _serviceID + "> rdfs:isDefinedBy ?wsdlLink . \n");
		transQuery.append("<" + _serviceID + "> dc:creator ?creator . \n");
		transQuery.append("<" + _serviceID + "> sawsdl:modelReference ?serviceModel . \n");
		transQuery.append("<" + _serviceID + "> msm:hasOperation ?operation . \n");
		
		transQuery.append("?operation rdf:type msm:Operation . \n");
		transQuery.append("?operation rdfs:label ?operationLabel . \n");
		transQuery.append("?operation sawsdl:modelReference ?operationModel . \n");

		transQuery.append("?operation msm:hasInput ?inputMessage . \n");
		transQuery.append("?inputMessage rdf:type msm:MessageContent . \n");
		transQuery.append("?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n");
		transQuery.append("?inputMessage msm:hasPart ?inputMessagePart . \n");
		transQuery.append("?inputMessagePart rdf:type msm:MessagePart . \n");
		transQuery.append("?inputMessagePart msm:hasName ?inputMessagePartName . \n");
		
		transQuery.append("?operation msm:hasOutput ?outputMessage . \n");
		transQuery.append("?outputMessage rdf:type msm:MessageContent . \n");
		transQuery.append("?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n");
		transQuery.append("?outputMessage msm:hasPart ?outputMessagePart . \n");
		transQuery.append("?outputMessagePart rdf:type msm:MessagePart . \n");
		transQuery.append("?outputMessagePart msm:hasName ?outputMessagePartName . \n");
		
		transQuery.append("?operation msm:hasInputFault ?inputFaultMessage . \n");
		transQuery.append("?inputFaultMessage rdf:type msm:MessageContent . \n");
		transQuery.append("?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n");
		transQuery.append("?inputFaultMessage msm:hasPart ?inputFaultMessagePart . \n");
		transQuery.append("?inputFaultMessagePart rdf:type msm:MessagePart . \n");
		transQuery.append("?inputFaultMessagePart msm:hasName ?inputFaultMessagePartName . \n");
		
		transQuery.append("?operation msm:hasOutputFault ?outputFaultMessage . \n");
		transQuery.append("?outputFaultMessage rdf:type msm:MessageContent . \n");
		transQuery.append("?outputFaultMessage msm:hasPart ?outputFaultMessagePart . \n");
		transQuery.append("?outputFaultMessagePart rdf:type msm:MessagePart . \n");
		transQuery.append("?outputFaultMessagePart msm:hasName ?outputFaultMessagePartName . \n");
		
		transQuery.append("} WHERE { \n");
		
		transQuery.append("<" + _serviceID + "> rdf:type msm_ext:Service . \n");
		transQuery.append("<" + _serviceID + "> rdfs:label ?serviceLabel . \n");
		transQuery.append("<" + _serviceID + "> rdfs:isDefinedBy ?wsdlLink . \n");
		transQuery.append("OPTIONAL { \n");
		transQuery.append("<" + _serviceID + "> dc:creator ?creator . \n");
		transQuery.append("} \n");
		transQuery.append("OPTIONAL { \n");
		transQuery.append("<" + _serviceID + "> sawsdl:modelReference ?serviceModel . \n");
		transQuery.append("} \n");
		transQuery.append("<" + _serviceID + "> msm_ext:wsdlDescription ?descriptionBlock . \n");
		
		transQuery.append("?descriptionBlock wsdl:interface ?interfaceBlock . \n");;
		
		transQuery.append("?interfaceBlock wsdl:interfaceOperation ?operation . \n");
		transQuery.append("?operation rdfs:label ?operationLabel . \n");
		
		transQuery.append("OPTIONAL { \n");
		transQuery.append("?operation wsdl:interfaceMessageReference ?inputMessage . \n");
		transQuery.append("OPTIONAL { \n");
		transQuery.append("?operation sawsdl:modelReference ?operationModel . \n");
		transQuery.append("} \n");
		transQuery.append("?inputMessage rdf:type wsdl:InputMessage . \n");
		transQuery.append("?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n");
		transQuery.append("?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n");
		transQuery.append("?inputMessagePart wsdl:localName ?inputMessagePartName . \n");
		transQuery.append("OPTIONAL { \n");
		transQuery.append("?inputMessagePart sawsdl:modelReference ?inputMessagePartModel . \n");
		transQuery.append("} \n");
		transQuery.append("} \n");
		
		transQuery.append("OPTIONAL { \n");
		transQuery.append("?operation wsdl:interfaceMessageReference ?outputMessage . \n");
		transQuery.append("?outputMessage rdf:type wsdl:OutputMessage . \n");
		transQuery.append("?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n");
		transQuery.append("?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n");
		transQuery.append("?outputMessagePart wsdl:localName ?outputMessagePartName . \n");
		transQuery.append("OPTIONAL { \n");
		transQuery.append("?outputMessagePart sawsdl:modelReference ?outputMessagePartModel . \n");
		transQuery.append("} \n");
		transQuery.append("} \n");
		
		transQuery.append("OPTIONAL { \n");
		transQuery.append("?operation wsdl:interfaceFaultReference ?inputFaultMessage . \n");
		transQuery.append("?inputFaultMessage rdf:type wsdl:InputMessage . \n");
		transQuery.append("?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n");
		transQuery.append("?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n");
		transQuery.append("?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n");
		transQuery.append("OPTIONAL { \n");
		transQuery.append("?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n");
		transQuery.append("} \n");
		transQuery.append("} \n");

		transQuery.append("OPTIONAL { \n");
		transQuery.append("?operation wsdl:interfaceFaultReference ?outputFaultMessage . \n");
		transQuery.append("?outputFaultMessage rdf:type wsdl:OutputMessage . \n");
		transQuery.append("?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n");
		transQuery.append("?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n");
		transQuery.append("?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n");
		transQuery.append("OPTIONAL { \n");
		transQuery.append("?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n");
		transQuery.append("} \n");
		transQuery.append("} \n");
		
		transQuery.append("}");
		
		GraphQueryResult queryResult = reposHandler.constructSPARQL(transQuery.toString());

		ByteArrayOutputStream out =  new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}
	
	public static void main(String[] args) throws Exception {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS"));
//		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#AUTHORITY"));
//		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#Maritime"));
//		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#HealthDeclaration"));
		System.out.println(ServiceDiscovery.discover(categoryList, RDFFormat.N3));
//		System.out.println("---");
		
		List<URI> inputParamList = new ArrayList<URI>();
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		List<URI> outputParamList = new ArrayList<URI>();
		outputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
//		System.out.println(ServiceDiscovery.discover(categoryList, inputParamList, outputParamList, RDFFormat.N3));
		
//		System.out.println(ServiceDiscovery.lookup(new URI("http://www.webserviceX.NET"), "GetWeather", RDFFormat.N3));	
//		System.out.println("---");
		
//		System.out.println(ServiceDiscovery.getIServeModel("http://www.webserviceX.NET#GlobalWeather", RDFFormat.N3));
//		System.out.println("---");
	}
	
}
