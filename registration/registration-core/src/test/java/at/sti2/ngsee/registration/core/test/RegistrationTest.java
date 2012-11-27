/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.ngsee.registration.core.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.wsdl.WSDLException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import at.sti2.ngsee.registration.api.exception.RegistrationException;
import at.sti2.ngsee.registration.core.management.TransformationWSDL;

/**
 * @author Corneliu Stanciu
 * 
 * @author Benjamin Hiltpolt
 */
public class RegistrationTest {

	private Logger logger = Logger.getLogger(RegistrationTest.class);

    private List<URL> webservicePass = new ArrayList<URL>();
    private List<URL> webservicefail = new ArrayList<URL>();
    
	/**
	 * 
	 */
	@Before
	public void setUp() {
		webservicePass = TestWebserviceExtractor.getPassingWSDLs();
		webservicefail = TestWebserviceExtractor.getFailingWSDLs();
	}

	@Test
	public void testWSDL() throws RepositoryException,
			WSDLException, IOException, URISyntaxException,
			RegistrationException {

		for (URL url : webservicePass) {

			logger.info("Test WSDL " + url);

			String serviceID = TransformationWSDL.transformWSDL(url
					.toExternalForm());

			Assert.assertTrue(checkServiceID(serviceID));
		}
		
		
	}

	private boolean checkServiceID(String serviceID) {
		boolean hasServiceID = false;
		if (serviceID != null)
			hasServiceID = true;

		return hasServiceID;
	}
}
