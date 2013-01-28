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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.openrdf.rio.RDFFormat;

import at.sti2.msee.discovery.core.ServiceDiscovery;


/**
 * @author Benjamin Hiltpolt
 * 
 * 
 */
public class DiscoveryTest {
	
	
	/**
	 * TODO: Make better test
	 * @param args
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testDiscovery() throws Exception {
		List<URI> categoryList = new ArrayList<URI>();
		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#BUSINESS"));
//		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#AUTHORITY"));
//		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#Maritime"));
//		categoryList.add(new URI("http://www.sti2.at/E-Freight/ServiceCategories#HealthDeclaration"));
		System.out.println(ServiceDiscovery.discover(categoryList, RDFFormat.N3));
//		System.out.println("---");
		
		List<URI> inputParamList = new ArrayList<URI>();
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		inputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
		List<URI> outputParamList = new ArrayList<URI>();
		outputParamList.add(new URI("http://www.w3.org/TR/xmlschema-2/#string"));
//		System.out.println(ServiceDiscovery.discover(categoryList, inputParamList, outputParamList, RDFFormat.N3));
		
//		System.out.println(ServiceDiscovery.lookup(new URI("http://www.webserviceX.NET"), "GetWeather", RDFFormat.N3));	
//		System.out.println("---");
		
//		System.out.println(ServiceDiscovery.getIServeModel("http://www.webserviceX.NET#GlobalWeather", RDFFormat.N3));
//		System.out.println("---");
	}
}
