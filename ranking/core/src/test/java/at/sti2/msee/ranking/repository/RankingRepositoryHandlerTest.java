package at.sti2.msee.ranking.repository;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import at.sti2.msee.ranking.api.exception.RankingException;

public class RankingRepositoryHandlerTest {

	private RankingRepositoryHandler handler;

	private String ws1str = new String("http://www.example.com/ranking/ws1");
	private String ws2str = new String("http://www.example.com/ranking/ws1");

	private String rules1;
	private String rules2;

	private URL ws1;
	private URL ws2;

	@Ignore
	@Before
	public void setUp() throws Exception {
		this.handler = RankingRepositoryHandler.getInstance();

		// TODO LOAD RULES
		fail("loading of rules not yet implemented!"); // TODO

		this.ws1 = new URL(ws1str);
		this.ws2 = new URL(ws2str);
	}

	@Ignore
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

		} catch (RankingException e) {
			fail();
		}

	}

}
