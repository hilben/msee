package at.sti2.msee.registration.webservice.configuration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadDefault() throws IOException {
		Configuration configuration = new Configuration();
		configuration.load();
		
		String sesameEndpoint = configuration.getSesameEndpoint();
		assertEquals("http://sesa.sti2.at:8080/openrdf-sesame",sesameEndpoint);
		
		String repositoryId = configuration.getSesameReposID();
		assertEquals("msee",repositoryId);		
	}
	
	@Test
	public void testLoadPropertFile() throws IOException {
		Configuration configuration = new Configuration();
		configuration.load("/test.properties");
		
		String sesameEndpoint = configuration.getSesameEndpoint();
		assertEquals("http://localhost:8080/openrdf-sesame",sesameEndpoint);
		
		String repositoryId = configuration.getSesameReposID();
		assertEquals("msee-test",repositoryId);		
	}
}