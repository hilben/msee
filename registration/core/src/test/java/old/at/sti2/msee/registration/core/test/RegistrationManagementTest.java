package old.at.sti2.msee.registration.core.test;
///**
// * 
// */
//package at.sti2.msee.registration.core.test;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.net.URL;
//
//import org.apache.log4j.Logger;
//import org.junit.Before;
//import org.junit.Test;
//
//import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
//import at.sti2.msee.registration.core.management.old.RegistrationManagement;
//import at.sti2.msee.registration.core.management.old.RegistrationWSDLToTriplestoreWriter;
//
///**
// * @author Benjamin Hiltpolt
// *
// */
//public class RegistrationManagementTest {
//
//	
//	private Logger logger = Logger.getLogger(RegistrationManagementTest.class);
//	
//	private URL url;
//	
//	private RegistrationWSDLToTriplestoreWriter registration;
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		
//		
//		
//		url = this.getClass().getResource("/00-all.wsdl");
//		
//        
//		logger.info("Add wsdl file for test case " + url.toExternalForm());
//		assertNotNull(url);
//		
//		registration = new RegistrationWSDLToTriplestoreWriter();
//		registration.transformWSDLtoTriplesAndStoreInTripleStore(url.toExternalForm());
//	}
//
//	/**
//	 * Test method for {@link at.sti2.msee.registration.core.management.old.RegistrationManagement#delete(java.lang.String)}.
//	 */
//	@Test
//	public void testDelete() {
//		try {
//			logger.info("Delete " + url.toExternalForm());
//			RegistrationManagement.delete(url.toExternalForm());
//		} catch (ServiceRegistrationException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Test method for {@link at.sti2.msee.registration.core.management.old.RegistrationManagement#update(java.lang.String, java.lang.String)}.
//	 */
//	@Test
//	public void testUpdate() {
//		try {
//			logger.info("Update " + url.toExternalForm() + " to " + url.toExternalForm());
//			RegistrationManagement.update(url.toExternalForm(), url.toExternalForm());
//		} catch (ServiceRegistrationException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
