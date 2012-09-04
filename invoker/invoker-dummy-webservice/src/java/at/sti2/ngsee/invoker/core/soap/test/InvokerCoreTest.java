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
package at.sti2.ngsee.invoker.core.soap.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.namespace.QName;

//import org.junit.Test;

import at.sti2.ngsee.invoker.core.InvokerCore;

/**
 * @author Alex Oberhauser
 */
public class InvokerCoreTest extends AbstractSoapTest {
	private static int LOOPS = 10;
	
//	@Test
	public void testWeatherService() throws Exception {
		List<QName> emptyHeaders = new ArrayList<QName>();
		String serviceID = "http://www.webserviceX.NET#GlobalWeather";
		String operationName = "GetWeather";
		
		StringBuilder inputData = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner scanner = new Scanner(new FileInputStream(InvokerCoreTest.class.getResource("/weather-input.rdf.xml").getFile()));
		try {
		      while (scanner.hasNextLine()){
			        inputData.append(scanner.nextLine() + NL);
		      }
		} finally {
			scanner.close();
		}
		
		for ( int count=0; count < LOOPS; count++ ) {
			this.startTimer();
			InvokerCore.invoke(serviceID, emptyHeaders, operationName, inputData.toString());
			long executionTime = this.stopTimer();
			System.out.println("[PerfTest] Service '" + serviceID + "' execution time was " + executionTime + " ms");
			
		}
	}
}
