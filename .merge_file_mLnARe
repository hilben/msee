/**
 * 
 */
package at.sti2.msee.registration.core.test;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.management.RegistrationWSDLToTriplestoreWriter;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class RegistrationWSDLToTriplestoreWriterTest {

	private List<URL> webservicePass = new ArrayList<URL>();
	private List<URL> webservicefail = new ArrayList<URL>();

	private RegistrationWSDLToTriplestoreWriter registration;

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.webservicePass = TestWebserviceExtractor.getPassingWSDLs();
		this.webservicefail = TestWebserviceExtractor.getFailingWSDLs();

		this.registration = new RegistrationWSDLToTriplestoreWriter();
	}

	/**
	 * Test method for
	 * {@link at.sti2.msee.registration.core.management.RegistrationWSDLToTriplestoreWriter#transformWSDLtoTriplesAndStoreInTripleStore(java.lang.String)}
	 * .
	 */
	@Test
	public void testTransformWSDLPass() {
		for (URL url : webservicePass) {
			logger.info("Tranform: " + url);

			try {
				this.registration
						.transformWSDLtoTriplesAndStoreInTripleStore(url
								.toExternalForm());
			} catch (ServiceRegistrationException e) {
				logger.error(e);
				fail(e.toString());
			}
		}

	}

	/**
	 * Test method for
	 * {@link at.sti2.msee.registration.core.management.RegistrationWSDLToTriplestoreWriter#transformWSDLtoTriplesAndStoreInTripleStore(java.lang.String)}
	 * .
	 */
	@Test
	public void testTransformWSDLFail() {
		for (URL url : webservicefail) {
			logger.info("Transform: " + url);

			try {
				this.registration
						.transformWSDLtoTriplesAndStoreInTripleStore(url
								.toExternalForm());

				// no exception
				fail("WSDL file was valid but shouldn't");
			} catch (ServiceRegistrationException e) {
				logger.info(e);
				logger.info("WSDL File was rejected");
			}
		}
	}

}
