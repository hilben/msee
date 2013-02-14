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
import java.io.StringReader;
import java.net.URI;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.UnsupportedRDFormatException;

import at.sti2.msee.discovery.core.common.DiscoveryConfig;
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
public class ServiceDiscovery {
	
	private RepositoryHandler repositoryHandler;
	
	public ServiceDiscovery() throws FileNotFoundException, IOException{
		repositoryHandler = getReposHandler();
	}

	private RepositoryHandler getReposHandler()
			throws FileNotFoundException, IOException {
		DiscoveryConfig config = new DiscoveryConfig();
		
		
		return new RepositoryHandler(config.getSesameEndpoint(),
				config.getSesameReposID(), true);
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

		StringBuffer discoveryQuery = new StringBuffer();

		discoveryQuery
				.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		discoveryQuery
				.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		discoveryQuery
				.append("PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n");
		discoveryQuery
				.append("PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n");
		discoveryQuery
				.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");

		discoveryQuery.append("CONSTRUCT { \n");
		discoveryQuery
				.append("?serviceID msm_ext:hasOperation ?inputMessage . \n");

		discoveryQuery.append("?inputMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery
				.append("?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n");
		discoveryQuery
				.append("?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n");
		discoveryQuery
				.append("?inputMessagePart wsdl:localName ?inputMessagePartName . \n");
		discoveryQuery
				.append("?inputMessagePart sawsdl:modelReference ?inputMessagePartModel . \n");

		discoveryQuery
				.append("?serviceID msm_ext:hasOperation ?outputMessage . \n");
		discoveryQuery
				.append("?outputMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery
				.append("?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n");
		discoveryQuery
				.append("?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n");
		discoveryQuery
				.append("?outputMessagePart wsdl:localName ?outputMessagePartName . \n");

		discoveryQuery
				.append("?serviceID msm_ext:hasOperation ?inputFaultMessage . \n");
		discoveryQuery
				.append("?inputFaultMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery
				.append("?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n");
		discoveryQuery
				.append("?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n");
		discoveryQuery
				.append("?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n");
		discoveryQuery
				.append("?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n");

		discoveryQuery
				.append("?serviceID msm_ext:hasOperation ?outputFaultMessage . \n");
		discoveryQuery
				.append("?outputFaultMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery
				.append("?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n");
		discoveryQuery
				.append("?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n");
		discoveryQuery
				.append("?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n");
		discoveryQuery
				.append("?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n");

		discoveryQuery.append("} WHERE { \n");
		discoveryQuery.append("?serviceID rdf:type msm_ext:Service . \n");
		for (URI category : _categoryList) {
			discoveryQuery.append("{ \n");
			discoveryQuery.append("<" + category
					+ "> rdfs:subClassOf* ?superClass . \n");
			discoveryQuery
					.append("?serviceID sawsdl:modelReference ?superClass . \n");
			discoveryQuery.append("} UNION { \n");
			discoveryQuery.append("?serviceID sawsdl:modelReference <"
					+ category + "> . \n");
			discoveryQuery.append("} \n");
		}
		discoveryQuery
				.append("?serviceID msm_ext:wsdlDescription ?descriptionBlock . \n");
		discoveryQuery
				.append("?descriptionBlock wsdl:namespace ?namespace . \n");
		discoveryQuery
				.append("?descriptionBlock wsdl:interface ?interfaceBlock . \n");
		discoveryQuery
				.append("?interfaceBlock wsdl:interfaceOperation ?operation . \n");
		discoveryQuery.append("?operation rdfs:label ?operationName . \n");

		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery
				.append("?operation wsdl:interfaceMessageReference ?inputMessage . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery
				.append("?operation sawsdl:modelReference ?operationModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("?inputMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery
				.append("?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n");
		discoveryQuery
				.append("?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n");
		discoveryQuery
				.append("?inputMessagePart wsdl:localName ?inputMessagePartName . \n");
		// discoveryQuery.append("OPTIONAL { \n");

		int count = 0;
		for (URI inputParam : _inputParamList) {
			discoveryQuery.append("{ ?inputMessagePart sawsdl:modelReference <"
					+ inputParam + "> . } \n");
			count++;
			if (count != _inputParamList.size())
				discoveryQuery.append(" UNION \n");
		}

		// discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");

		if (_outputParamList != null && _outputParamList.size() == 0)
			discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery
				.append("?operation wsdl:interfaceMessageReference ?outputMessage . \n");
		discoveryQuery
				.append("?outputMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery
				.append("?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n");
		discoveryQuery
				.append("?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n");
		discoveryQuery
				.append("?outputMessagePart wsdl:localName ?outputMessagePartName . \n");
		// discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery
				.append("?outputMessagePart sawsdl:modelReference ?outputMessagePartModel . \n");

		for (URI outputParam : _outputParamList) {
			discoveryQuery.append("?outputMessagePart sawsdl:modelReference <"
					+ outputParam + "> . \n");
		}

		// discoveryQuery.append("} \n");

		if (_outputParamList != null && _outputParamList.size() == 0)
			discoveryQuery.append("} \n");

		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery
				.append("?operation wsdl:interfaceFaultReference ?inputFaultMessage . \n");
		discoveryQuery
				.append("?inputFaultMessage rdf:type wsdl:InputMessage . \n");
		discoveryQuery
				.append("?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n");
		discoveryQuery
				.append("?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n");
		discoveryQuery
				.append("?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery
				.append("?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");

		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery
				.append("?operation wsdl:interfaceFaultReference ?outputFaultMessage . \n");
		discoveryQuery
				.append("?outputFaultMessage rdf:type wsdl:OutputMessage . \n");
		discoveryQuery
				.append("?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n");
		discoveryQuery
				.append("?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n");
		discoveryQuery
				.append("?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n");
		discoveryQuery.append("OPTIONAL { \n");
		discoveryQuery
				.append("?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n");
		discoveryQuery.append("} \n");
		discoveryQuery.append("} \n");

		discoveryQuery.append("}");

		GraphQueryResult queryResult = repositoryHandler
				.constructSPARQL(discoveryQuery.toString());

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}

	public String discover(List<URI> _categoryList,
			RDFFormat _outputFormat) throws FileNotFoundException, IOException,
			QueryEvaluationException, RepositoryException,
			MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {

		String templateString = new String(
				"group group-demo;"
						+ "outerTemplate(_categoryList) ::= <<"
						+ " "
						+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
						+ "PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n"
						+ "PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n"
						+ "PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n"
						+ "CONSTRUCT { \n"
						+ "?serviceID msm_ext:hasOperation ?inputMessage . \n"
						+ "?inputMessage rdf:type wsdl:InputMessage . \n"
						+ "?inputMessage sawsdl:loweringSchemaMapping ?inputMessageLowering . \n"
						+ "?inputMessage wsdl:elementDeclaration ?inputMessagePart . \n"
						+ "?inputMessagePart wsdl:localName ?inputMessagePartName . \n"
						+ "?inputMessagePart sawsdl:modelReference ?inputMessagePartModel . \n"
						+ "?serviceID msm_ext:hasOperation ?outputMessage . \n"
						+ "?outputMessage rdf:type wsdl:OutputMessage . \n"
						+ "?outputMessage sawsdl:liftingSchemaMapping ?outputMessageLifting . \n"
						+ "?outputMessage wsdl:elementDeclaration ?outputMessagePart . \n"
						+ "?outputMessagePart wsdl:localName ?outputMessagePartName . \n"
						+ "?outputMessagePart sawsdl:modelReference ?outputMessagePartModel . \n"
						+

						"?serviceID msm_ext:hasOperation ?inputFaultMessage . \n"
						+ "?inputFaultMessage rdf:type wsdl:InputMessage . \n"
						+ "?inputFaultMessage sawsdl:loweringSchemaMapping ?inputFaultMessageLowering . \n"
						+ "?inputFaultMessage wsdl:elementDeclaration ?inputFaultMessagePart . \n"
						+ "?inputFaultMessagePart wsdl:localName ?inputFaultMessagePartName . \n"
						+ "?inputFaultMessagePart sawsdl:modelReference ?inputFaultMessagePartModel . \n"
						+

						"?serviceID msm_ext:hasOperation ?outputFaultMessage . \n"
						+ "?inputFaultMessage rdf:type wsdl:OutputMessage . \n"
						+ "?outputFaultMessage sawsdl:liftingSchemaMapping ?outputFaultMessageLifting . \n"
						+ "?outputFaultMessage wsdl:elementDeclaration ?outputFaultMessagePart . \n"
						+ "?outputFaultMessagePart wsdl:localName ?outputFaultMessagePartName . \n"
						+ "?outputFaultMessagePart sawsdl:modelReference ?outputFaultMessagePartModel . \n"
						+

						"} WHERE { \n"
						+ "?serviceID rdf:type msm_ext:Service . \n"
						+ ""
						+ "$_categoryList:innerTemplate()$"
						+ ""
						+ "?serviceID msm_ext:wsdlDescription ?descriptionBlock . \n"
						+ "?descriptionBlock wsdl:namespace ?namespace . \n"
						+ "?descriptionBlock wsdl:interface ?interfaceBlock . \n"
						+ "?interfaceBlock wsdl:interfaceOperation ?interfaceOperation . \n"
						+ "?interfaceOperation rdfs:label ?operationName . \n"
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

						"}>>" + "" +

						"innerTemplate(category) ::= <<" +
						/**
						 * TODO: Check this part.
						 */
						"{ \n"
						+ "<$category$> rdfs:subClassOf* ?superClass . \n"
						+ "?serviceID sawsdl:modelReference ?superClass . \n"
						+ "} UNION { \n"
						+ "?serviceID sawsdl:modelReference <$category$> . \n"
						+ "} \n" + ">>");

		
		StringTemplateGroup group = new StringTemplateGroup(
				new StringReader(templateString), DefaultTemplateLexer.class);

		StringTemplate template = group.getInstanceOf("outerTemplate");
		template.setAttribute("_categoryList", _categoryList);

		GraphQueryResult queryResult = repositoryHandler
				.constructSPARQL(template.toString());

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, _outputFormat, out);

		return out.toString();
	}

	public String lookup(URI _namespace, String _operationName,
			RDFFormat _outputFormat) throws FileNotFoundException, IOException,
			QueryEvaluationException, RepositoryException,
			MalformedQueryException, RDFHandlerException,
			UnsupportedRDFormatException {

		StringTemplate lookupQuery = new StringTemplate(
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
				"PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n"+
				"PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n"+
				"PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n"+
		
				"CONSTRUCT { \n"+
				"?messageReference ?p ?o . \n"+
				"?faultMessageReference ?p1 ?o1 . \n"+
				"} WHERE { \n"+
		
				"?serviceID rdf:type msm_ext:Service . \n"+
				"?serviceID msm_ext:wsdlDescription ?descriptionBlock . \n"+
				"?descriptionBlock wsdl:namespace <$_namespace$> . \n"+
				"?descriptionBlock wsdl:interface ?interfaceBlock . \n"+
				"?interfaceBlock wsdl:interfaceOperation ?interfaceOperation . \n"+
				"?interfaceOperation wsdl:interfaceMessageReference ?messageReference . \n"+
				"?interfaceOperation rdfs:label \"$_operationName$\" . \n"+
				"?messageReference ?p ?o . \n"+
				"OPTIONAL { ?interfaceOperation wsdl:interfaceFaultReference ?faultMessageReference . \n"+
				"?faultMessageReference ?p1 ?o1 . } \n"+
				"}");
		
		lookupQuery.setAttribute("_namespace", _namespace);
		lookupQuery.setAttribute("_operationName", _operationName);

		GraphQueryResult queryResult = repositoryHandler.constructSPARQL(lookupQuery.toString());

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
