/**
 * 
 */
package at.sti2.msee.registration.core.test;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
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
	private RegistrationConfig configFail;
	
	private static final String testPropertyLocation = "/test.properties";
	private static final String testPropertyFailLocation = "/testfail.properties";
	
	private static final String testValueEndpoint = "TESTENDPOINT";
	private static final String testValueReposiotry = "TESTREPO";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		config = new RegistrationConfig(testPropertyLocation);
		configFail = new RegistrationConfig(testPropertyFailLocation);
		
		assertNotNull(config);
		assertNotNull(configFail);
	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.common.RegistrationConfig#Config()}.
	 */
	@Test
	public void testConfig() {
		assertNotNull(this.config);
	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.common.RegistrationConfig#getSesameEndpoint()}.
	 */
	@Test
	public void testGetSesameEndpoint() {
		assertNotNull(this.config.getSesameEndpoint());
		
		logger.info("Config endpoint pass: " + this.config.getSesameEndpoint());
		logger.info("Config endpoint fail: " + this.configFail.getSesameEndpoint());
		
		String strPass = this.config.getSesameEndpoint();
		String strFail = this.configFail.getSesameEndpoint();
		
		assertTrue(strPass.compareTo(testValueEndpoint)==0);
		assertFalse(strFail.compareTo(testPropertyFailLocation)==0);
		

	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.common.RegistrationConfig#getSesameReposID()}.
	 */
	@Test
	public void testGetSesameReposID() {
		assertNotNull(this.config.getSesameReposID());
		
		logger.info("Config repository: " + this.config.getSesameReposID());
		
		String strPass = this.config.getSesameReposID();
		String strFail = this.configFail.getSesameReposID();
		
		assertTrue(strPass.compareTo(testValueReposiotry)==0);
		assertFalse(strFail.compareTo(testPropertyFailLocation)==0);		
	}
}
