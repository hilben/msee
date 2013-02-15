/**
 * 
 */
package at.sti2.msee.registration.core.test;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.registration.core.common.RegistrationConfig;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class RegistrationConfigTest {

	private Logger logger = Logger.getLogger(RegistrationConfigTest.class);

	private RegistrationConfig config;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		config = new RegistrationConfig();
		assertNotNull(config);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.common.RegistrationConfig#getSesameEndpoint()}.
	 */
	@Test
	public void testGetSesameEndpoint() {
		assertNotNull(this.config.getSesameEndpoint());
		logger.info("Config endpoint: " + this.config.getSesameEndpoint());
	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.common.RegistrationConfig#getSesameReposID()}.
	 */
	@Test
	public void testGetSesameReposID() {
		assertNotNull(this.config.getSesameReposID());
		logger.info("Config repository: " + this.config.getSesameReposID());
	}

}
