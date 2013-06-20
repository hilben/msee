package at.sti2.msee.config;

import org.junit.Assert;

import org.junit.Test;

public class ConfigTest {
	
	@Test
	public void testEndpoint() {
		Assert.assertTrue(Config.INSTANCE.getRepositoryEndpoint().contains("openrdf-sesame"));
	}

	@Test
	public void testRepositoryID() {
		Assert.assertTrue(Config.INSTANCE.getRepositoryID().contains("msee-test"));
	}
	
	@Test
	public void testMonitoringRepositoryID() {
		Assert.assertTrue(Config.INSTANCE.getMonitoringRepositoryID().contains("wsmf"));
	}
}