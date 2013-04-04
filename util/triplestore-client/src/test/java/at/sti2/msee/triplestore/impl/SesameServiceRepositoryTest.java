package at.sti2.msee.triplestore.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.aduna.io.IOUtil;

import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

public class SesameServiceRepositoryTest {

	String serverEndpoint = "http://sesa.sti2.at:8080/openrdf-sesame";
	String repositoryId = "msee-test";
	ServiceRepository repository;
	
	@Before
	public void setUp() throws Exception {
		ServiceRepositoryConfiguration configuration = new ServiceRepositoryConfiguration();		
		configuration.setServerEndpoint(serverEndpoint);
		configuration.setRepositoryID(repositoryId);
		
		repository = ServiceRepositoryFactory.newInstance(configuration);
		assertNotNull(repository);
		repository.init();		
	}

	@After
	public void tearDown() throws Exception {
		repository.shutdown();
	}

	@Test
	public void testSesameServiceRepositoryConfiguration() {
		
		ServiceRepositoryConfiguration configuration = new ServiceRepositoryConfiguration();		
		configuration.setServerEndpoint(serverEndpoint);
		configuration.setRepositoryID(repositoryId);
		
		ServiceRepository repository = ServiceRepositoryFactory.newInstance(configuration);
		assertNotNull(repository);
		
		String serverEndpoint = repository.getServiceRepositoryConfiguration().getServerEndpoint();
		assertEquals(serverEndpoint, this.serverEndpoint);
		
		String repositoryId = repository.getServiceRepositoryConfiguration().getRepositoryID();
		assertEquals(repositoryId, this.repositoryId);
	}

	//We don't test individual methods because it is integrated in the core.

}