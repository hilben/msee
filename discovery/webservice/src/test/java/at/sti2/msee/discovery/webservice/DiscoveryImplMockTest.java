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
package at.sti2.msee.discovery.webservice;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import at.sti2.msee.discovery.api.webservice.Discovery;

/**
 * @author Benjamin Hiltpolt
 * @author Christian Mayr
 * 
 * 
 */
public class DiscoveryImplMockTest {
	// private String resourceLocation = "/default.test.properties";
	Mockery context = new JUnit4Mockery();
	private Discovery discoveryWebService = context
			.mock(Discovery.class);

	@Test
	public void testDiscovery2() throws Exception {
		final List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#BUSINESS"));
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#AUTHORITY"));
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#Maritime"));
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#HealthDeclaration"));

		final List<URI> inputParamList = new ArrayList<URI>();
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		final List<URI> outputParamList = new ArrayList<URI>();
		outputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		context.checking(new Expectations() {
			{
				oneOf(discoveryWebService).discoverAdvanced(categoryList, inputParamList,
						outputParamList);
			}
		});
		System.out.println(discoveryWebService.discoverAdvanced(categoryList, inputParamList,
				outputParamList));

	}

	@Test
	public void testDiscover() throws Exception {
		final List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#BUSINESS"));
		context.checking(new Expectations() {
			{
				oneOf(discoveryWebService).discover(categoryList);
			}
		});
		System.out.println(discoveryWebService.discover(categoryList));
	}

	@Test
	public void testLookup() throws URISyntaxException, Exception {
		context.checking(new Expectations() {
			{
				oneOf(discoveryWebService).lookup(new URI("http://www.webserviceX.NET"),
						"GetWeather");
			}
		});
		System.out.println(discoveryWebService.lookup(new URI(
				"http://www.webserviceX.NET"), "GetWeather"));
	}

	@Test
	public void testIServeModel() throws Exception {
		context.checking(new Expectations() {
			{
				oneOf(discoveryWebService).getIServeModel(
						"http://www.webserviceX.NET#GlobalWeather");
			}
		});
		System.out.println(discoveryWebService
				.getIServeModel("http://www.webserviceX.NET#GlobalWeather"));
	}
}
