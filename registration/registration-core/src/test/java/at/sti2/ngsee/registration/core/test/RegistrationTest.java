package at.sti2.ngsee.registration.core.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.wsdl.WSDLException;

import org.junit.Assert;
import org.openrdf.repository.RepositoryException;

import at.sti2.ngsee.registration.core.transformation.TransformationWSDL11;

import org.junit.Test;

/**
 * @author Corneliu Stanciu
 */
public class RegistrationTest {
	
	@Test
	public void testWSDL() throws FileNotFoundException, RepositoryException, WSDLException, IOException, URISyntaxException {
		String serviceID = TransformationWSDL11.transformWSDL("http://sesa.sti2.at/services/globalweather.sawsdl");

		Assert.assertTrue(checkServiceID(serviceID));
	}
	
	private boolean checkServiceID(String serviceID) {
		boolean hasServiceID = false;
		if ( serviceID != null)
			hasServiceID = true;
		
		return hasServiceID;
	}
}
