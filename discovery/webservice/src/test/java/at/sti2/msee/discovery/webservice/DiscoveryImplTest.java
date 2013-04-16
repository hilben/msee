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
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.lang.Exception;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.openrdf.query.MalformedQueryException;
//import org.openrdf.query.QueryEvaluationException;
//import org.openrdf.query.TupleQueryResultHandlerException;
//import org.openrdf.query.resultio.UnsupportedQueryResultFormatException;
//import org.openrdf.repository.RepositoryException;
//
//import at.sti2.msee.discovery.api.webservice.Discovery;
//import at.sti2.msee.discovery.webservice.DiscoveryImpl;
//import at.sti2.msee.registration.api.ServiceRegistration;
//import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
//import at.sti2.msee.registration.core.ServiceRegistrationFactory;
//import at.sti2.msee.registration.core.ServiceRegistrationImpl;
//import at.sti2.msee.registration.core.configuration.ServiceRegistrationConfiguration;
//import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
//
///**
// * @author Benjamin Hiltpolt
// * 
// * 
// */
//public class DiscoveryImplTest {
//	// private String resourceLocation = "/default.properties";
//	private Discovery discoveryWebService;
//
//	@Before
//	public void setup() throws FileNotFoundException, IOException,
//			ServiceRegistrationException, QueryEvaluationException,
//			RepositoryException, MalformedQueryException,
//			TupleQueryResultHandlerException,
//			UnsupportedQueryResultFormatException {
//		discoveryWebService = new DiscoveryImpl();
//
//		// is already in triple store
//		DiscoveryService ds = new DiscoveryService();
//		boolean alreadyThere = ds
//				.alreadyInTripleStore("http://greath.example.com/2004/wsdl/resSvc#reservationService");
//
//		// register
//		if (!alreadyThere) {
//			ServiceRegistration registration = this.createRegistrationService();
//			URL wsdlInput = DiscoveryImplTest.class
//					.getResource("/ReservationService.sawsdl");
//			registration.register(wsdlInput.toString());
//		}
//	}
//
//	private ServiceRegistration createRegistrationService()
//	{
//		ServiceRegistrationConfiguration registrationConfiguration = new ServiceRegistrationConfiguration();
//
//		ServiceRepositoryConfiguration repositoryConfiguration = new ServiceRepositoryConfiguration();
//		//Empty configuration means inmemory store
////		repositoryConfiguration.setRepositoryID(repositoryId);
////		repositoryConfiguration.setServerEndpoint(serverEndpoint);
//		registrationConfiguration.setRepositoryConfiguration(repositoryConfiguration);
//
//		return ServiceRegistrationFactory.createServiceRegistration(registrationConfiguration);
//	}
//		
//	@Test
//	public void testDiscoverAdvanced() throws Exception {
//		String[] categoryList = new String[4];
//		categoryList[0]="http://www.sti2.at/MSEE/ServiceCategories#BUSINESS";
//		categoryList[1]="http://www.sti2.at/MSEE/ServiceCategories#Authority";
//		categoryList[2]="http://www.sti2.at/MSEE/ServiceCategories#Maritime";
//		categoryList[3]="http://www.sti2.at/MSEE/ServiceCategories#HealthDeclaration";
//
//		String[] inputParamList = new String[2];
//		inputParamList[0]="http://www.w3.org/TR/xmlschema-2/#string";
//		inputParamList[1]="http://www.w3.org/TR/xmlschema-2/#string";
//		String[] outputParamList = new String[1];
//		outputParamList[0]="http://www.w3.org/TR/xmlschema-2/#string";
//		discoveryWebService.discoverAdvanced(categoryList, inputParamList,
//				outputParamList);
//
//	}
//
//	/**
//	 * TODO: make test right
//	 * 
//	 * @throws Exception
//	 */
//	@Test()
//	public void testDiscover() throws Exception {
//		String[] categoryList = new String[1];
//		categoryList[0] = "";
//
//		try {
//			discoveryWebService.discover(categoryList);
//		} catch (Exception e) {
//			if (e.getMessage() != null
//					&& e.getMessage().contains("Not a valid (absolute) URI")) {
//			} else if (e.getMessage() != null
//					&& e.getMessage().equals("Category list is null")) {
//				throw new IllegalArgumentException(e);
//			}
//		}
//
//		categoryList[0] = "http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS";
//		discoveryWebService.discover(categoryList);
//	}
//
//	@Test
//	public void testLookup() throws URISyntaxException, Exception {
//		discoveryWebService.lookup("http://www.webserviceX.NET",
//				"GetWeather");
//	}
//
//	@Test
//	public void testIServeModel() throws Exception {
//		discoveryWebService
//				.getIServeModel("http://www.webserviceX.NET#GlobalWeather");
//	}
//}
