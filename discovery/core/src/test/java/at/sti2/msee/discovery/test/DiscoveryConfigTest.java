package at.sti2.msee.discovery.test;

import java.io.IOException;

import org.apache.log4j.Logger;

import at.sti2.msee.discovery.core.common.DiscoveryConfig;
import junit.framework.TestCase;

/**
 * The class <code>DiscoveryConfigTest</code> contains tests for the class
 * {@link <code>DiscoveryConfig</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 1/28/13 10:59 AM
 *
 * @author benni
 *
 * @version $Revision$
 */
public class DiscoveryConfigTest extends TestCase {

	
	private static Logger logger = Logger.getLogger(DiscoveryConfigTest.class);
	/**
	 * Construct new test instance
	 *
	 * @param name the test name
	 */
	public DiscoveryConfigTest(String name) {
		super(name);
	}

	/**
	 * Perform pre-test initialization
	 *
	 * @throws Exception
	 *
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		logger.info("DiscoveryConfigTest");
	}


	/**
	 * Run the String getSesameEndpoint() method test
	 * @throws IOException 
	 */
	public void testGetSesameEndpoint() throws IOException {

		DiscoveryConfig fixture = new DiscoveryConfig();
		String result = fixture.getSesameEndpoint();
		
		logger.info("Endpoint: " + result);
		assertNotNull(result);
	}

	/**
	 * Run the String getSesameReposID() method test
	 * @throws IOException 
	 */
	public void testGetSesameReposID() throws IOException {

		DiscoveryConfig fixture = new DiscoveryConfig();
		String result = fixture.getSesameReposID();
		
		logger.info("Repo: " + result);
		assertNotNull(result);
	}
}