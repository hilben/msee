package at.sti2.ngsee.discovery.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.junit.Ignore;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.UnsupportedRDFormatException;

import at.sti2.ngsee.discovery.core.ServiceDiscovery;

public class StringBuilderTest {
	@Test
	public void teststringtemplate() throws URISyntaxException, FileNotFoundException, QueryEvaluationException, RepositoryException, MalformedQueryException, RDFHandlerException, UnsupportedRDFormatException, IOException {
		String _namespace = "example.com";
		List<URI> uris = new ArrayList<URI>();
		uris.add(new URI("http://1.com"));
		uris.add(new URI("http://2.com"));
		uris.add(new URI("http://3.com"));

		StringTemplate lookupQuery = new StringTemplate(
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
						+ "PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n"
						+ "PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n"
						+ "PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n"
						+

						"CONSTRUCT { \n"
						+ "?messageReference ?p ?o . \n"
						+ "?faultMessageReference ?p1 ?o1 . \n"
						+ "} WHERE { \n"
						+

						"?serviceID rdf:type msm_ext:Service . \n"
						+ "?serviceID msm_ext:wsdlDescription ?descriptionBlock . \n"
						+ "?descriptionBlock wsdl:namespace <$_namespace$> . \n"
						+ "?descriptionBlock wsdl:interface ?interfaceBlock . \n"
						+ "?interfaceBlock wsdl:interfaceOperation ?interfaceOperation . \n"
						+ "?interfaceOperation wsdl:interfaceMessageReference ?messageReference . \n"
						+
						/*
						 * "?interfaceOperation rdfs:label \"$_operationName$\" . \n"
						 * +
						 */
						"?messageReference ?p ?o . \n"
						+ "OPTIONAL { ?interfaceOperation wsdl:interfaceFaultReference ?faultMessageReference . \n"
						+ "?faultMessageReference ?p1 ?o1 . } \n"
						+ "}\n     $uris; separator=\"\\n\"$   ");

		lookupQuery.setAttribute("_namespace", _namespace);
		lookupQuery.setAttribute("uris", uris);

		System.out.println(lookupQuery);

		String s = new String(
				"group group-demo;"
						+

						"outerTemplate(input) ::= <<"
						+ "   In the outer template."
						+ "   input = $input$ <-- I can see the value of the 'input' parameter and use it."
						+ "$innerTemplate(input)$"
						+ ">>"
						+

						"innerTemplate(nestedInput) ::= <<"
						+ "The Paramater 'nestedInput' was passed to this template."
						+ "nestedInput = $nestedInput$ <-- I can see the value of the 'nestedInput' parameter and use it."
						+ ">>");

		StringTemplateGroup group = new StringTemplateGroup(
				new StringReader(s), DefaultTemplateLexer.class);

		StringTemplate template = group.getInstanceOf("outerTemplate");
		template.setAttribute("input", "Hello World");

		System.out.println(template.toString());
		
		
		System.out.println(ServiceDiscovery.discover(uris, RDFFormat.RDFXML));
	}
}
