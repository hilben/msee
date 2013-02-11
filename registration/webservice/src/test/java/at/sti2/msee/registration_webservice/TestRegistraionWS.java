package at.sti2.msee.registration_webservice;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.registration.webservice.RegistrationWebService;

public class TestRegistraionWS {
	
	private static Logger logger = Logger.getLogger(TestRegistraionWS.class);
	private RegistrationWebService ws ;

	@Before
	public void setUp() throws Exception {
		logger.info("Testing Registration Web Service Class");
		ws  = new RegistrationWebService();
		assertNotNull(ws);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testRegister() {
		// TODO testRegister
	}

	@Test
	public final void testDelete() {
		// TODO testDelete
	}

	@Test
	public final void testUpdate() {
		// TODO testUpdate
	}

}
