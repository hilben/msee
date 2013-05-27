package at.sti2.msee.ranking.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.ranking.api.exception.RankingException;

public class RankingRepositoryHandlerTest {

	private RankingRepositoryHandler handler;

	private String ws1str = new String("http://www.example.com/ranking/ws1");
	private String ws2str = new String("http://www.example.com/ranking/ws2");

	private String pathRule1 = new String("/WSMullerFixed_new.rdf.n3");
	private String pathRule2 = new String("/WSRacerFixed_new.rdf.n3");

	private String rules1;
	private String rules2;

	private URL ws1;
	private URL ws2;

	@Before
	public void setUp() throws Exception {
		this.handler = RankingRepositoryHandler.getInstance();

		this.ws1 = new URL(ws1str);
		this.ws2 = new URL(ws2str);

		this.rules1 = RankingRepositoryHandlerTest.readFile(pathRule1);
		this.rules2 = RankingRepositoryHandlerTest.readFile(pathRule2);
		// this.rules1 = "a ";
		// this.rules2 = "b ";
	}

	@Test
	public void testSetAndGetRulesForWebservice() {

		try {
			this.handler.setRulesForWebservice(ws1, rules1);

			this.handler.setRulesForWebservice(ws2, rules2);

			assertTrue(this.handler.getRulesForWebservice(ws1)
					.compareTo(rules1) == 0);
			assertTrue(this.handler.getRulesForWebservice(ws2)
					.compareTo(rules2) == 0);

			this.handler.setRulesForWebservice(ws1, rules2);
			this.handler.setRulesForWebservice(ws2, "");

			assertTrue(this.handler.getRulesForWebservice(ws1)
					.compareTo(rules2) == 0);
			assertTrue(this.handler.getRulesForWebservice(ws2).compareTo("") == 0);

		} catch (RankingException | RepositoryException
				| MalformedQueryException | UpdateExecutionException
				| IOException e) {
			e.printStackTrace();
			fail();
		}

	}

	private static String readFile(String path) throws IOException {
		Scanner sc = new Scanner(
				RankingRepositoryHandlerTest.class.getResourceAsStream(path));
		String text = sc.useDelimiter("\\A").next();
		sc.close();
		return text;
	}

}
