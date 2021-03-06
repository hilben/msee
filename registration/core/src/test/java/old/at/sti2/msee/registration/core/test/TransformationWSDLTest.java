package old.at.sti2.msee.registration.core.test;
///**
// * 
// */
//package at.sti2.msee.registration.core.test;
//
//import static org.junit.Assert.fail;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.junit.Before;
//import org.junit.Test;
//
//import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
//import at.sti2.msee.registration.core.management.old.TransformationWSDL;
//
///**
// * @author Benjamin Hiltpolt
// *
// */
//public class TransformationWSDLTest {
//
//	
//    private List<URL> webservicePass = new ArrayList<URL>();
//    private List<URL> webservicefail = new ArrayList<URL>();
//    
//    
//    private Logger logger = Logger.getLogger(this.getClass());
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		this.webservicePass = TestWebserviceExtractor.getPassingWSDLs();
//		this.webservicefail = TestWebserviceExtractor.getFailingWSDLs();
//	}
//
//	/**
//	 * Test method for {@link at.sti2.msee.registration.core.management.old.TransformationWSDL#transformWSDL(java.lang.String)}.
//	 */
//	@Test
//	public void testTransformWSDLPass() {
//		for (URL url : webservicePass) {
//			logger.info("Tranform: " + url);
//			
//			try {
//				TransformationWSDL.transformWSDL(url.toExternalForm());
//			} catch (ServiceRegistrationException e) {
//				logger.error(e);
//				fail(e.toString());
//			}
//		}
//
//	}
//	
//	/**
//	 * Test method for {@link at.sti2.msee.registration.core.management.old.TransformationWSDL#transformWSDL(java.lang.String)}.
//	 */
//	@Test
//	public void testTransformWSDLFail() {
//		for (URL url : webservicefail) {
//			logger.info("Tranform: " + url);
//			
//			try {
//				TransformationWSDL.transformWSDL(url.toExternalForm());
//				
//				//no exception
//				fail("WSDL file was valid but shouldn't");
//			} catch (ServiceRegistrationException e) {
//				logger.info(e);
//				logger.info("WSDL File was rejected");
//			}
//		}
//	}
//	
//
//}
