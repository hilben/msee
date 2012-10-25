/**
 * 
 */
package at.sti2.util.triplestore;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import junit.framework.TestCase;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class TestLocalRepository extends TestCase {

	protected static Logger logger = Logger
			.getLogger(TestLocalRepository.class);
	private static final int MAX_TRIPLES_TO_PRINT = 10;

	public void testLocalRepository() {
		RepositoryHandler repohandler = null;
		try {
			repohandler = new RepositoryHandler(
					"http://localhost/openrdf-sesame", "test");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.toString());
		}

		TestCase.assertNotNull(repohandler);

		try {
			repohandler.addLiteralTriple("http://www.sti2.at/asdf#"+Math.random(),
					"http://www.sti2.at/asdf#"+Math.random(), "http://www.sti2.at/asdf#"+Math.random(),
					"http://www.sti2.at/testcontext");
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail(e.toString());
		}

		try {
			repohandler.commit();
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail(e.toString());
		}

		try {
			TupleQueryResult res = repohandler
					.selectSPARQL("SELECT * WHERE { ?s ?p ?o}");
			int i = MAX_TRIPLES_TO_PRINT;
			while (res.hasNext() && i-- >= 0) {
				System.out.println(res.next());
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		}

	}

}
