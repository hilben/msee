package at.sti2.util.triplestore;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
	public void testSelectSPARQLStringTupleQueryResultHandler() {
		try {
			TupleQueryResult res = repohandler
					.selectSPARQL("SELECT * WHERE { ?s ?p ?o}");
			while (res.hasNext()) {
				System.out.println(res.next());
			}
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
	public void testAddLiteralTriple() {
		// Add some random triples for testing
		try {
			repohandler.addLiteralTriple("http://localhost/TestSubject",
					"http://localhost/TestPredicate",
					"http://localhost/TestObject",
					"http://localhost/TestContext");
		} catch (RepositoryException e) {
			e.printStackTrace();
			logger.error("Do there exists a repository named " + REPO_NAME
					+ " at " + REPO_URL);
			fail(e.toString());
		}
	}

	@Test
	public void testUpdateResourceTriple() {
		// TODO testUpdateResourceTriple
	}

	@Test
	public void testUpdateLiteralTriple() {
		// TODO testUpdateLiteralTriple
	}

	@Test
	public void testSelectSPARQL() {
		// TODO testSelectSPARQL
	}

	@Test
	public void testConstructSPARQL() {
		// TODO testConstructSPARQL
	}

	@Test
	public void testStoreEntity() {
		 // TODO testStoreEntity
	}


	@Test
	public void testDelete() {
		// TODO testDelete
	}

	@Test
	public void testDeleteContext() {
		// TODO testDeleteContext
	}

	@Test
	public void testCommit() {
    // TODO testCommit
	}

	@Test
	public void testAddResourceTriple() {
		// Add some random triples for testing
		try {
			repohandler.addLiteralTriple("http://localhost/TestSubject",
					"http://localhost/TestPredicatLiteral", "TestLiteral",
					"http://localhost/TestContext");
		} catch (RepositoryException e) {
			e.printStackTrace();
			logger.error("Do there exists a repository named " + REPO_NAME
					+ " at " + REPO_URL);
			fail(e.toString());
		}
	}

}
