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
package at.sti2.msee.discovery.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import at.sti2.msee.discovery.api.webservice.ServiceDiscovery;
import at.sti2.msee.discovery.webservice.DiscoveryWebServiceImpl;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 */
public class DiscoveryWebServiceImplTest {
	//private String resourceLocation = "/default.test.properties";
	private ServiceDiscovery discoveryWebService;

	@Before
	public void setup() throws FileNotFoundException, IOException {
		discoveryWebService = new DiscoveryWebServiceImpl();
	}

	@Test
	public void testDiscovery2() throws Exception {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI(
				"http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS"));
		categoryList.add(new URI(
				"http://www.sti2.at/E-Freight/ServiceCategories#AUTHORITY"));
		categoryList.add(new URI(
				"http://www.sti2.at/E-Freight/ServiceCategories#Maritime"));
		categoryList.add(new URI(
				"http://www.sti2.at/E-Freight/ServiceCategories#HealthDeclaration"));

		List<URI> inputParamList = new ArrayList<URI>();
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		List<URI> outputParamList = new ArrayList<URI>();
		outputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		System.out.println(discoveryWebService.discover(categoryList, inputParamList,
				outputParamList));

	}

	@Test
	public void testDiscover() throws Exception {
		final List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI(
				"http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS"));
		System.out.println(discoveryWebService.discover(categoryList));
	}

	@Test
	public void testLookup() throws URISyntaxException, Exception {
		System.out.println(discoveryWebService.lookup(new URI(
				"http://www.webserviceX.NET"), "GetWeather"));
	}

	@Test
	public void testIServeModel() throws Exception {
		System.out.println(discoveryWebService
				.getIServeModel("http://www.webserviceX.NET#GlobalWeather"));
	}
}
