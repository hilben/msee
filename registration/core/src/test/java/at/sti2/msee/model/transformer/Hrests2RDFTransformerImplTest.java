package at.sti2.msee.model.transformer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Hrests2RDFTransformerImplTest {

	static String generatedRdf = "";
	static Model msmModel;
	static Resource serviceResource;

	@BeforeClass
	public static void setup() {
		RDF2Go.register(new RepositoryModelFactory());
		msmModel = RDF2Go.getModelFactory().createModel();
		msmModel.open();
	}

	@Test
	public void test1ToMSM() throws Service2RDFTransformerException, ModelRuntimeException,
			IOException {

		URL serviceDescriptionURL = new URL(this.getClass().getResource("/formats/hrests1.html")
				.toString());

		Service2RDFTransformer transformer = Service2RDFTransformerFactory
				.newInstance(ServiceModelFormat.HRESTS);
		Syntax syntax = Syntax.RdfXml;

		transformer.setDefaultSyntax(syntax);

		generatedRdf = transformer.toMSM(serviceDescriptionURL);

		test2ReadGeneratedRDF();
	}

	@Test
	public void test2ReadGeneratedRDF() throws ModelRuntimeException, IOException {
		Syntax syntax = Syntax.RdfXml;
		StringReader reader = new StringReader(generatedRdf);
		msmModel.readFrom(reader, syntax);
	}

	@Test
	public void test3CheckService() {
		TriplePattern pattern = msmModel.createTriplePattern(Variable.ANY, RDF.type, MSM.Service);
		ClosableIterator<Statement> statements = msmModel.findStatements(pattern);
		assertTrue(statements.hasNext());
		Statement statement = statements.next();
		serviceResource = statement.getSubject();
		assertTrue(statement.getSubject().toString().contains("http://msee.sti2.at"));
		assertFalse(statements.hasNext());
	}

	@Test
	public void test4CheckCategory() {
		TriplePattern pattern = msmModel.createTriplePattern(serviceResource, MSM.modelReference,
				Variable.ANY);
		ClosableIterator<Statement> statements = msmModel.findStatements(pattern);
		assertTrue(statements.hasNext());
		Statement statement = statements.next();
		Node categoryResource = statement.getObject();
		assertTrue(categoryResource.asResource().toString()
				.contains("http://msee.sti2.at/categories#BUSINESS"));
		assertFalse(statements.hasNext());
	}

	@Test
	public void test5CheckOperation() {
		TriplePattern pattern = msmModel.createTriplePattern(serviceResource, MSM.hasOperation,
				Variable.ANY);
		ClosableIterator<Statement> statements = msmModel.findStatements(pattern);
		assertTrue(statements.hasNext());
		Statement statement = statements.next();
		Node operationResource = statement.getObject();

		assertTrue(operationResource.asResource().toString().contains("#op1"));
		assertFalse(statements.hasNext());
	}
}