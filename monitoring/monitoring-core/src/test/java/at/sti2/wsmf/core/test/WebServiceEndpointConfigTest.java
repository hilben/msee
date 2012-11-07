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
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.core.common.MonitoringConfig;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;


/**
 * @author Benjamin Hiltpolt
 *
 */
public class WebServiceEndpointConfigTest {

	private Logger logger = Logger.getLogger(WebServiceEndpointConfigTest.class);
	private WebServiceEndpointConfig config;

	/**
	 * @throws RepositoryException 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws RepositoryException {
	
		try {
			config = WebServiceEndpointConfig.getConfig("http://www.noconfig.com");
		} catch (IOException e) {
			
			e.printStackTrace();
			fail();
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
//		assertNotNull(this.config.getTripleStoreEndpoint());
		assertNotNull(this.config.getTripleStorereposID());
		assertNotNull(this.config.getEndpointURL());
		assertNotNull(this.config.getTripleStorendpoint());
		assertNotNull(this.config.getWebServiceName());
		assertNotNull(this.config.getWebServiceNamespace());
		
		logger.info(this.config.toString());
	}


}
