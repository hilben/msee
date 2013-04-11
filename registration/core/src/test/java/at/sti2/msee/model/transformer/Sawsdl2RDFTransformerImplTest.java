package at.sti2.msee.model.transformer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.TriplePattern;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.vocabulary.RDF;
import org.openrdf.rdf2go.RepositoryModelFactory;

import at.sti2.msee.model.ServiceModelFormat;
import at.sti2.msee.model.transformer.Service2RDFTransformer;
import at.sti2.msee.model.transformer.Service2RDFTransformerException;
import at.sti2.msee.model.transformer.Service2RDFTransformerFactory;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;

public class Sawsdl2RDFTransformerImplTest {

	@Test
	public void testToMSM_HelloService() throws Service2RDFTransformerException, ModelRuntimeException, IOException {
		
		URL serviceDescriptionURL = new URL(this.getClass().getResource("/webservices/sawsdl/HelloService.sawsdl").toString());

		Service2RDFTransformer transformer = Service2RDFTransformerFactory.newInstance(ServiceModelFormat.SAWSDL);
		Syntax syntax = Syntax.RdfXml;

		transformer.setDefaultSyntax(syntax);

		String msmRdf = transformer.toMSM(serviceDescriptionURL);		

		//Read generated MSM
		StringReader reader = new StringReader(msmRdf);
		RDF2Go.register(new RepositoryModelFactory());		
		Model msmModel = RDF2Go.getModelFactory().createModel();
		msmModel.open();
		msmModel.readFrom(reader, syntax);
				
		//We check for the service, only one
		TriplePattern pattern = msmModel.createTriplePattern(Variable.ANY, RDF.type , MSM.Service);
		ClosableIterator<Statement> statements = msmModel.findStatements(pattern);
		assertTrue(statements.hasNext());
		Statement statement = statements.next();
		Resource serviceResource = statement.getSubject();
		assertTrue(statement.getSubject().toString().contains("http://msee.sti2.at"));
		assertFalse(statements.hasNext());
		
		//check for the category, only one
		pattern = msmModel.createTriplePattern(serviceResource, MSM.modelReference , Variable.ANY);
		statements = msmModel.findStatements(pattern);
		assertTrue(statements.hasNext());
		statement = statements.next();
		Node categoryResource = statement.getObject();
		assertTrue(categoryResource.asResource().toString().contains("http://msee.sti2.at/categories/business"));
		assertFalse(statements.hasNext());
					
		//Check for operation
		pattern = msmModel.createTriplePattern(serviceResource, MSM.hasOperation , Variable.ANY);
		statements = msmModel.findStatements(pattern);
		assertTrue(statements.hasNext());
		statement = statements.next();
		Node operationResource = statement.getObject();
		assertTrue(operationResource.asResource().toString().contains("#wsdl.service(helloService)/Hello"));
		assertFalse(statements.hasNext());
		
	}
}