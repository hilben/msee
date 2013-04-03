/**
 * 
 */
package at.sti2.wsmf.core.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import at.sti2.wsmf.core.common.MonitoringConfig;


/**
 * @author Benjamin Hiltpolt
 *
 */
public class ConfigTest {

	private Logger logger = Logger.getLogger(ConfigTest.class);
	private MonitoringConfig config;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() {
	
		try {
			config = MonitoringConfig.getConfig();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
		
		assertNotNull(config);
	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link at.sti2.MonitoringConfig.registration.core.common.Config#Config()}.
	 */
	@Test
	public void testConfig() {
		assertNotNull(this.config);
	}

	/**
	 * Test method for {@link at.sti2.MonitoringConfig.registration.core.common.Config#getSesameEndpoint()}.
	 */
	@Test
	public void testGetConfigProperties() {
		assertNotNull(this.config.getInstancePrefix());
		assertNotNull(this.config.getTriplestoreEndpoint());
		assertNotNull(this.config.getTriplestoreReposID());
		logger.info("Config endpoint: " + this.config.getInstancePrefix());
		logger.info("Config endpoint: " + this.config.getTriplestoreEndpoint());
		logger.info("Config endpoint: " + this.config.getTriplestoreReposID());
		logger.info(this.config.toString());
	}


}
