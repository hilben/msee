package at.sti2.msee.registration.core.wsdl2rdf;

import java.net.URL;

import org.ontoware.rdf2go.model.Syntax;

public abstract class Service2RDFTransformer {

	public final static String SAWSDL = "SAWSDL";
	
	private Syntax defaultSyntax = Syntax.RdfXml;
		
//	String toRDF(URL descriptionURL) throws Service2RDFTransformerException;

	public abstract String toMSM(URL descriptionURL) throws Service2RDFTransformerException;

	public Syntax getDefaultSyntax() {
		return defaultSyntax;
	}

	public void setDefaultSyntax(Syntax defaultSyntax) {
		this.defaultSyntax = defaultSyntax;
	}

}
