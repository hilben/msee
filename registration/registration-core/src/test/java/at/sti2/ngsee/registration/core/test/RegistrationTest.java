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
