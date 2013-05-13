package at.sti2.msee.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ServiceModelFormatDetectorTest {

	@Test
	public void testDetectSAWSDLURL() throws MalformedURLException {
		
		URL serviceDescriptionURL = new URL(this.getClass().getResource("/webservices/sawsdl/HelloService.sawsdl").toString());
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		ServiceModelFormat format = detector.detect(serviceDescriptionURL);		
		assertEquals(ServiceModelFormat.SAWSDL,format);
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testNoFile() {
		thrown.expect(NullPointerException.class);
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		detector.detect((URI)null);
		thrown.expect(IllegalArgumentException.class);
		detector.detect(URI.create("http://not-a-valid-uri.abc"));
	}

	@Test
	public void testUnkown() {
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		assertTrue(ServiceModelFormat.UNKNOWN.equals(detector.detect(URI
				.create("htTp://msee.sti2.at"))));
	}

	@Test
	public void testHrests() throws URISyntaxException {
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		URI file = this.getClass().getResource("/formats/hrests1.html").toURI();
		assertTrue(ServiceModelFormat.HRESTS.equals(detector.detect(file)));
	}
	
	@Test
	public void testHrestsURL() throws URISyntaxException {
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		URL file = this.getClass().getResource("/formats/hrests1.html");
		assertTrue(ServiceModelFormat.HRESTS.equals(detector.detect(file)));
	}

	@Test
	public void testSawsdl() throws URISyntaxException {
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		URI file = this.getClass().getResource("/formats/Indesit_webapp.wsdl")
				.toURI();
		assertTrue(ServiceModelFormat.SAWSDL.equals(detector.detect(file)));
	}

	@Test
	public void testMsm() throws URISyntaxException {
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		URI file = this.getClass().getResource("/formats/msm-testfile.msm")
				.toURI();
		ServiceModelFormat format = detector.detect(file);
		assertTrue("MSM assumed but format is: " + format,
				ServiceModelFormat.MSM.equals(format));
	}

}
