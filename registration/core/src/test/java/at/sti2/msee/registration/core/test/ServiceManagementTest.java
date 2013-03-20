/**
 * 
 */
package at.sti2.msee.registration.core.test;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.management.ServiceManagement;
import at.sti2.msee.registration.core.management.TransformationWSDL;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class ServiceManagementTest {

	
	private Logger logger = Logger.getLogger(ServiceManagementTest.class);
	
	private URL url;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		
		
		url = this.getClass().getResource("/00-all.wsdl");
		
        
		logger.info("Add wsdl file for test case " + url.toExternalForm());
		assertNotNull(url);
		
		try {
			TransformationWSDL.transformWSDL(url.toExternalForm());
		} catch (ServiceRegistrationException e) {
			if (!e.getMessage().equals("Service already registered")){
				throw new Exception(e);
			}
		}	
	    
	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.management.ServiceManagement#delete(java.lang.String)}.
	 */
	@Test
	public void testDelete() {
		try {
			logger.info("Delete " + url.toExternalForm());
			ServiceManagement.delete(url.toExternalForm());
		} catch (ServiceRegistrationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link at.sti2.msee.registration.core.management.ServiceManagement#update(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testUpdate() throws Exception {
		try {
			logger.info("Update " + url.toExternalForm() + " to " + url.toExternalForm());
			ServiceManagement.update(url.toExternalForm(), url.toExternalForm());
		} catch (ServiceRegistrationException e) {
			if (!e.getMessage().equals("Service already registered")){
				throw new Exception(e);
			}
		}
	}

}
