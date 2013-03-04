package at.sti2.msee.discovery.test;

import java.io.IOException;
import java.util.Properties;

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
	String resourceLocation = "/default.properties";
	String endpointName = "discovery.sesame.endpoint";
	String sesameRepositoryIDName = "discovery.sesame.reposid";
	private DiscoveryConfig config;
	private Properties properties = new Properties();
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
		config = new DiscoveryConfig();
		config.setResourceLocation(resourceLocation);
		
		properties.load(DiscoveryConfig.class.getResourceAsStream(resourceLocation )); 
		logger.info("DiscoveryConfigTest");
	}

	/**
	 * Run the String getSesameEndpoint() method test
	 * @throws IOException 
	 */
	public void testGetSesameEndpoint() throws IOException {
		String endpoint = properties.getProperty(endpointName);
		DiscoveryConfig fixture = new DiscoveryConfig();
		fixture.setResourceLocation(resourceLocation);
		fixture.setSesameEndpointName(endpointName);
		String result = fixture.getSesameEndpoint();
		
		logger.info("Endpoint: " + result);
		assertEquals(endpoint, result);
	}

	/**
	 * Run the String getSesameRepositoryID() method test
	 * @throws IOException 
	 */
	public void testGetSesameRepositoryID() throws IOException {
		String sesameRepositoryID = properties.getProperty(sesameRepositoryIDName);
		DiscoveryConfig fixture = new DiscoveryConfig();
		fixture.setSesameRepositoryIDName(sesameRepositoryIDName);
		fixture.setResourceLocation(resourceLocation);
		String result = fixture.getSesameRepositoryID();
		
		logger.info("Repo: " + result);
		assertEquals(sesameRepositoryID, result);
	}
}