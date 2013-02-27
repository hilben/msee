package at.sti2.util.triplestore;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

public class TestRepositoryHandler {
	protected static Logger logger = Logger
			.getLogger(TestRepositoryHandler.class);

	// TODO: don't hardcorde url
	private static final String REPO_URL = "http://sesa.sti2.at:8080/openrdf-sesame";
	private static final String REPO_NAME = "test";
	
	private static final String subject = "http://localhost/TestSubject";
	private static final String predicate = "http://localhost/TestPredicate";
	private static final String object = "http://localhost/TestObject";
	private static final String context = "http://localhost/TestContext";

	// The repohandler which is tested
	private RepositoryHandler repohandler;

	@Before
	public void setUp() throws Exception {
		this.repohandler = null;
		try {
			this.repohandler = new RepositoryHandler(

			REPO_URL, REPO_NAME, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("Is there a repository running at " + REPO_URL);
			fail(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Is there a repository running at " + REPO_URL);
			fail(e.toString());
		}

		TestCase.assertNotNull(this.repohandler);

		this.testAddLiteralTriple();

	}

	@After
	public void tearDown() throws Exception {
		this.repohandler.shutdown();
	}

	@Test
	public void testSelectSPARQL() throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		TupleQueryResult res = repohandler
				.selectSPARQL("SELECT * WHERE { ?s ?p ?o}");
		Assert.assertTrue(res!=null && res.hasNext());
	}

	@Test
	public void testSelectSPARQLStringTupleQueryResultHandler() {
		try {
			TupleQueryResult res = repohandler
					.selectSPARQL("SELECT * WHERE { ?s ?p ?o}");
			int i =0;
			while (res.hasNext()) {
				//System.out.println(res.next());
				res.next(); i++;
			}
			System.out.println("No of triples: "+i);
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (MalformedQueryException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
			fail(e.toString());
		}

	}

	@Test
	public void testAddLiteralTriple() throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		// Add some random triples for testing		
		try {
			repohandler.addLiteralTriple(subject, predicate, object, context);
		} catch (RepositoryException e) {
			e.printStackTrace();
			logger.error("Do there exists a repository named " + REPO_NAME
					+ " at " + REPO_URL);
			fail(e.toString());
		}
		TupleQueryResult res = repohandler
				.selectSPARQL("SELECT * WHERE { ?s ?p ?o}");
		int i =0;
		while (res.hasNext()) {
			BindingSet bindingSet = res.next();
			if(bindingSet.getValue("s").stringValue().equals(subject) && 
					bindingSet.getValue("p").stringValue().equals(predicate) &&
					bindingSet.getValue("o").stringValue().equals(object)){
				i++;
			}
		}
		Assert.assertTrue(i>0);
	}

	@Test
	public void testUpdateResourceTriple() {
		// TODO testUpdateResourceTriple
	}

	@Test
	public void testUpdateLiteralTriple() {
		// TODO testUpdateLiteralTriple
	}



	public void testConstructSPARQL() {
		// TODO testConstructSPARQL
	}

	@Test
	public void testStoreEntity() {
		 // TODO testStoreEntity
	}


	@Test
	public void testDelete() throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// check size
		TupleQueryResult res = repohandler
				.selectSPARQL("SELECT * WHERE { <"+subject+"> <"+predicate+"> ?o}");
		int size = 0;
		while (res.hasNext()){
			size++;
			res.next();
		}
		Assert.assertTrue(size>0);
		
		// delete something
		String subject = "http://localhost/TestSubject";
		String predicate = "http://localhost/TestPredicate";
		repohandler.delete(subject, predicate);
		
		// check size again
		res = repohandler.selectSPARQL("SELECT * WHERE { <"+subject+"> <"+predicate+"> ?o}");
		int sizeAfter = 0;
		while (res.hasNext()){
			sizeAfter++;
			res.next();
		}
		Assert.assertTrue(size>sizeAfter);
	}

	@Test
	public void testDeleteContext() throws RepositoryException, QueryEvaluationException, MalformedQueryException {
		// add something
		repohandler.addLiteralTriple(subject, predicate, object, context);
		
		// check size
		TupleQueryResult res = repohandler
				.selectSPARQL("SELECT * WHERE { ?s ?p ?o}");
		int size = 0;
		while (res.hasNext()){
			size++;
			res.next();
		}
		Assert.assertTrue(size>0);
		
		// delete according to context
		repohandler.deleteContext(context);
		
		// check size again
		res = repohandler.selectSPARQL("SELECT * WHERE { ?s ?p ?o}");
		int sizeAfter = 0;
		while (res.hasNext()){
			sizeAfter++;
			res.next();
		}
		Assert.assertTrue(size>sizeAfter);
	}

	@Test
	public void testCommit() {
    // TODO testCommit
	}

	@Test
	public void testAddResourceTriple() throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		String subject = "http://localhost/TestResourceSubject";
		String predicate = "http://localhost/TestResourcePredicate";
		String object = "http://localhost/TestResourceObject";
		String context = "http://localhost/TestResourceContext";
		
		try {
			repohandler.addLiteralTriple(subject, predicate, object, context);
		} catch (RepositoryException e) {
			e.printStackTrace();
			logger.error("Do there exists a repository named " + REPO_NAME
					+ " at " + REPO_URL);
			fail(e.toString());
		}
		TupleQueryResult res = repohandler
				.selectSPARQL("SELECT * WHERE { ?s ?p ?o}");
		int i =0;
		while (res.hasNext()) {
			BindingSet bindingSet = res.next();
			if(bindingSet.getValue("s").stringValue().equals(subject) && 
					bindingSet.getValue("p").stringValue().equals(predicate) &&
					bindingSet.getValue("o").stringValue().equals(object)){
				i++;
			}
		}
		Assert.assertTrue(i>0);
	}

}
