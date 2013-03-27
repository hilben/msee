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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.UnsupportedQueryResultFormatException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.discovery.api.webservice.Discovery;
import at.sti2.msee.discovery.core.DiscoveryService;
import at.sti2.msee.discovery.webservice.DiscoveryImpl;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.ServiceRegistrationImpl;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 */
public class DiscoveryImplTest {
	// private String resourceLocation = "/default.properties";
	private Discovery discoveryWebService;

	@Before
	public void setup() throws FileNotFoundException, IOException,
			ServiceRegistrationException, QueryEvaluationException, RepositoryException, MalformedQueryException, TupleQueryResultHandlerException, UnsupportedQueryResultFormatException {
		discoveryWebService = new DiscoveryImpl();
		
		// is already in triple store
		DiscoveryService ds = new DiscoveryService();
		boolean alreadyThere = ds.alreadyInTripleStore("http://greath.example.com/2004/wsdl/resSvc#reservationService");
		
		// register
		if (!alreadyThere){
			ServiceRegistrationImpl registration = new ServiceRegistrationImpl();
			URL wsdlInput = DiscoveryImplTest.class
					.getResource("/ReservationService.wsdl");
			registration.register(wsdlInput.toString());
		}
	}

	@Test
	public void testDiscovery2() throws Exception {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#BUSINESS"));
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#Authority"));
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#Maritime"));
		categoryList.add(new URI(
				"http://www.sti2.at/MSEE/ServiceCategories#HealthDeclaration"));

		List<URI> inputParamList = new ArrayList<URI>();
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		List<URI> outputParamList = new ArrayList<URI>();
		outputParamList
				.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		discoveryWebService.discoverAdvanced(categoryList, inputParamList,
				outputParamList);

	}

	/**
	 * TODO: make test right
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testDiscover() throws Exception {
		final List<URI> categoryList = new ArrayList<URI>();
		
		discoveryWebService.discover(categoryList);
		
		categoryList.add(new URI(
				"http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS"));
		discoveryWebService.discover(categoryList);
		System.out.println(discoveryWebService.discover(categoryList));
	}

	@Test
	public void testLookup() throws URISyntaxException, Exception {
		discoveryWebService.lookup(new URI("http://www.webserviceX.NET"),
				"GetWeather");
	}

	@Test
	public void testIServeModel() throws Exception {
		discoveryWebService
				.getIServeModel("http://www.webserviceX.NET#GlobalWeather");
	}
}
