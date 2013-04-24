package at.sti2.monitoring.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class MonitoringRepositoryHandlerTest {

	MonitoringRepositoryHandler repositoryHandler;

	String ws = "http://www.example.com/examplews";
	URL wsURL = null;

	@Before
	public void setUp() throws Exception {
		repositoryHandler = MonitoringRepositoryHandler.getMonitoringRepositoryHandler();
		wsURL = new URL(ws);
	}

	@Test
	public void testSetMonitoredWebservice() throws IOException {
		repositoryHandler.setMonitoredWebservice(wsURL, true);
	}

	@Test
	public void testGetServiceRepository() {
		assertNotNull(repositoryHandler.getServiceRepository());
	}

}
