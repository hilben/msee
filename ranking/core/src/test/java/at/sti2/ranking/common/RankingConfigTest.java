package at.sti2.ranking.common;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import at.sti2.msee.ranking.common.RankingConfig;

public class RankingConfigTest {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	private RankingConfig config;

	@Before
	public void setUp() throws Exception {
		this.config = RankingConfig.getConfig();
	}

	@Test
	public void testGetTriplestoreEndpoint() {
		assertTrue(this.config.getTriplestoreEndpoint().compareTo(
				"http://sesa.sti2.at:8080/openrdf-sesame") == 0);
	}

	@Test
	public void testGetTriplestoreReposID() {
		assertTrue(this.config.getTriplestoreReposID().compareTo("ranking") == 0);
	}

	@Test
	public void testGetPrefixInstance() {
		assertTrue(this.config.getInstancePrefix().compareTo(
				"http://sti2.at/ranking/instances#") == 0);
	}

}
