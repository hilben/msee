/**
 * ServiceDiscovery.java - at.sti2.ngsee.discovery.core
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
 * @author Alex Oberhauser
 *
 */
public class ServiceDiscovery {
	
	public static RepositoryHandler getReposHandler() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(ServiceDiscovery.class.getResourceAsStream("/default.properties"));
		return new RepositoryHandler(prop.getProperty("sesame.endpoint"), prop.getProperty("sesame.reposid"));
	}
	
	public static String discover(List<URI> _categoryList, RDFFormat _outputFormat) throws FileNotFoundException, IOException, QueryEvaluationException, RepositoryException, MalformedQueryException, RDFHandlerException, UnsupportedRDFormatException {
		RepositoryHandler reposHandler = getReposHandler();
		
		StringBuffer discoveryQuery = new StringBuffer();
		
		discoveryQuery.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		discoveryQuery.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		discoveryQuery.append("PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n");
		discoveryQuery.append("PREFIX msm:<http://cms-wg.sti2.org/ns/minimal-service-model#> \n");
		discoveryQuery.append("PREFIX msm_ext: <http://sesa.sti2.org/ns/minimal-service-model-ext#> \n");
		discoveryQuery.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");
		
		discoveryQuery.append("CONSTRUCT { \n");
		discoveryQuery.append("?serviceID msm:hasOperation ?operationName . \n");
		discoveryQuery.append("?serviceID wsdl:namespace ?namespace");
		discoveryQuery.append("} WHERE { \n");
		discoveryQuery.append("?serviceID rdf:type msm:Service . \n");
		for ( URI category : _categoryList ) {
			discoveryQuery.append("?serviceID sawsdl:modelReference <" + category + "> . \n");
		}
		
		discoveryQuery.append("?serviceID msm_ext:wsdlDescription ?descriptionBlock . \n");
		discoveryQuery.append("?descriptionBlock wsdl:namespace ?namespace . \n");
		discoveryQuery.append("?descriptionBlock wsdl:interface ?interfaceBlock . \n");
		discoveryQuery.append("?interfaceBlock wsdl:interfaceOperation ?interfaceOperation . \n");
		discoveryQuery.append("?interfaceOperation rdfs:label ?operationName . \n");
		
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
		lookupQuery.append("PREFIX msm:<http://cms-wg.sti2.org/ns/minimal-service-model#> \n");
		lookupQuery.append("PREFIX msm_ext: <http://sesa.sti2.org/ns/minimal-service-model-ext#> \n");
		lookupQuery.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");
		
		lookupQuery.append("CONSTRUCT { \n");
		lookupQuery.append("?messageReference ?p ?o . \n");
		lookupQuery.append("?faultMessageReference ?p1 ?o1 . \n");
		lookupQuery.append("} WHERE { \n");
		lookupQuery.append("?serviceID rdf:type msm:Service . \n");
		
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
	
	public static void main(String[] args) throws Exception {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS"));
		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#AUTHORITY"));
		System.out.println(ServiceDiscovery.discover(categoryList, RDFFormat.N3));
		System.out.println("---");
		
		System.out.println(ServiceDiscovery.lookup(new URI("http://www.webserviceX.NET"), "GetWeather", RDFFormat.N3));
		
	}
}
