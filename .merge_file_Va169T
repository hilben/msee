/**
 * 
 */
package at.sti2.msee.registration.core.test;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.management.RegistrationManagement;
import at.sti2.msee.registration.core.management.RegistrationWSDLToTriplestoreWriter;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class RegistrationManagementTest {

	
	private Logger logger = Logger.getLogger(RegistrationManagementTest.class);
	
	private URL url;
	
	private RegistrationWSDLToTriplestoreWriter registration;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		
		
		url = this.getClass().getResource("/00-all.wsdl");
		
        
		logger.info("Add wsdl file for test case " + url.toExternalForm());
		assertNotNull(url);
		
		registration = new RegistrationWSDLToTriplestoreWriter();
		
		// insert if not already present
		try {
			registration.transformWSDLtoTriplesAndStoreInTripleStore(url.toExternalForm());
		} catch (ServiceRegistrationException e) {
			Assert.assertEquals("Service already registered", e.getMessage());
		}
	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.management.RegistrationManagement#delete(java.lang.String)}.
	 */
	@Test
	public void testDelete() {
		try {
			logger.info("Delete " + url.toExternalForm());
			RegistrationManagement.delete(url.toExternalForm());
		} catch (ServiceRegistrationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.management.RegistrationManagement#update(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdate() {
		try {
			logger.info("Update " + url.toExternalForm() + " to " + url.toExternalForm());
			RegistrationManagement.update(url.toExternalForm(), url.toExternalForm());
		} catch (ServiceRegistrationException e) {
			Assert.assertEquals("Service already registered", e.getMessage());
			e.printStackTrace();
		}
	}

}
