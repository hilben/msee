package at.sti2.msee.monitoring.core.common;

import static org.junit.Assert.*;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.monitoring.core.common.MonitoringConfig;

public class MonitoringConfigTest {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());
	
	private MonitoringConfig config;
	
	@Before
	public void setUp() throws Exception {
		this.config = MonitoringConfig.getConfig();
//		System.out.println(this.config);
	}

	@Test
	public void testGetInstancePrefix() {
		assertTrue(this.config.getInstancePrefix().compareTo("http://sti2.at/wsmf/instances#")==0);
	}

	@Test
	public void testGetTriplestoreEndpoint() {
		assertTrue(this.config.getTriplestoreEndpoint().compareTo("http://sesa.sti2.at:8080/openrdf-sesame")==0);
	}

	@Test
	public void testGetTriplestoreReposID() {
		assertTrue(this.config.getTriplestoreReposID().compareTo("wsmf")==0);
	}

}
