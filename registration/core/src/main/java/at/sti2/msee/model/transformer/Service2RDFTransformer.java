package at.sti2.msee.model.transformer;

import java.net.URL;

import org.ontoware.rdf2go.model.Syntax;

public abstract class Service2RDFTransformer {

	private Syntax defaultSyntax = Syntax.RdfXml;

	public abstract String toMSM(URL descriptionURL)
			throws Service2RDFTransformerException;

	public Syntax getDefaultSyntax() {
		return defaultSyntax;
	}

	public void setDefaultSyntax(Syntax defaultSyntax) {
		this.defaultSyntax = defaultSyntax;
	}
}
