package org.ontoware.rdf2go;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.TriplePattern;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.vocabulary.RDF;
import org.openrdf.model.Resource;
import org.openrdf.rdf2go.RepositoryModel;
import org.openrdf.rdf2go.RepositoryModelFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;

public class RDF2GoTest {

	Repository repository;
	
	@Before
	public void setUp() throws Exception {
		
		RDF2Go.register(new RepositoryModelFactory());
		
//		String serverEndpoint = "http://sesa.sti2.at:8080/openrdf-sesame";
//		String repositoryId = "test-msee";
//		repository = new HTTPRepository(serverEndpoint,repositoryId);
		repository = new SailRepository(new MemoryStore());		
		
		repository.initialize();		
		
		RepositoryConnection connection = repository.getConnection();	
		connection.clearNamespaces();
		connection.clear();				
		connection.commit();
		connection.close();
	}

	@After
	public void tearDown() throws Exception {	
		repository.shutDown();
	}

	@Test
	public void testLoadMSMIntoRepository_HelloService() throws ModelRuntimeException, IOException, RepositoryException {
		URL msmURL = this.getClass().getResource("/HelloServiceMSM.rdf");
		InputStream reader = msmURL.openStream();
		
		//We need to create an empty model to be able to create URIs...
		URI contextURI = RDF2Go.getModelFactory().createModel().createURI(msmURL.toString());
		
		Model msmModel = RDF2Go.getModelFactory().createModel();
		msmModel.open();		
		msmModel.readFrom(reader);				
				
		Model repositoryModel = new RepositoryModel(contextURI,repository);
		repositoryModel.open();		
		repositoryModel.addModel(msmModel);
		repositoryModel.setNamespace("msm", "http://cms-wg.sti2.org/ns/minimal-service-model#");
//		repositoryModel.dump();
		repositoryModel.close();		
				
		String serviceURI = "http://msee.sti2.at/services/a0acb8ac-dca7-4fc8-9859-a08e3be817fb#wsdl.service(helloService)";
		this.isContextInRepository(contextURI);
		this.isServiceInRepository(contextURI, serviceURI );
	}
	
	private void isContextInRepository(URI contextURI) throws RepositoryException {
		RepositoryConnection connection = repository.getConnection();
		RepositoryResult<Resource> contextURIs = connection.getContextIDs();	
		Resource insertedContextURI = contextURIs.next();
		assertEquals(contextURI.toString(),insertedContextURI.toString());
		assertFalse(contextURIs.hasNext());		
		connection.close();
	}

	private void isServiceInRepository(URI contextURI, String serviceURI) throws RepositoryException
	{			
		Model repositoryModel = new RepositoryModel(contextURI, repository);
		repositoryModel.open();		
//		repositoryModel.dump();	
		
		TriplePattern pattern = repositoryModel.createTriplePattern(Variable.ANY, RDF.type , MSM.Service);
		ClosableIterator<Statement> statements = repositoryModel.findStatements(pattern);	
		assertTrue(statements.hasNext());
		
		Statement statement = statements.next();
		assertTrue(statement.getSubject().toString().equals(serviceURI));
		assertFalse(statements.hasNext());
		
		repositoryModel.close();
	}
}