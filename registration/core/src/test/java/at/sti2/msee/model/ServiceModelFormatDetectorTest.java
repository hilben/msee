package at.sti2.msee.model;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class ServiceModelFormatDetectorTest {

	@Test
	public void testDetectSAWSDL() throws MalformedURLException {
		
		URL serviceDescriptionURL = new URL(this.getClass().getResource("/webservices/sawsdl/HelloService.sawsdl").toString());		
		ServiceModelFormat format = ServiceModelFormatDetector.detect(serviceDescriptionURL);		
		assertEquals(ServiceModelFormat.SAWSDL,format);
	}

}
