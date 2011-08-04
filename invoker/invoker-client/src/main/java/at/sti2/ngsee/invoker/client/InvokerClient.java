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
package at.sti2.ngsee.invoker.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import at.sti2.see.Exception_Exception;
import at.sti2.see.InvokerWebService;
import at.sti2.see.InvokerWebServiceService;

public class InvokerClient {

	private static final int LOOPS = 5;
	
	/**
	 * @param args
	 * @throws Exception_Exception 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception_Exception, FileNotFoundException {
		InvokerWebServiceService factory = new InvokerWebServiceService();
		InvokerWebService service = factory.getInvokerWebServicePort();

		String serviceID = "http://www.webserviceX.NET#GlobalWeather";
		String operationName = "GetWeather";

	    StringBuilder inputData = new StringBuilder();
	    String NL = System.getProperty("line.separator");
		Scanner scanner = new Scanner(new FileInputStream(InvokerClient.class.getResource("/weather-input.rdf.xml").getFile()));
		try {
		      while ( scanner.hasNextLine() ){
			        inputData.append(scanner.nextLine() + NL);
		      }
		} finally {
			scanner.close();
		}
		
		String oldResponseMessage = "";
		for ( int count=0; count < LOOPS; count++ ) {
			long startTime = System.currentTimeMillis();
			String responseMessage = service.invoke(serviceID, operationName, inputData.toString());
			if ( !oldResponseMessage.equals(responseMessage) ) {
				oldResponseMessage = responseMessage;
				System.out.println("[ResponseMessage] " + responseMessage);
			}
			System.out.println("[PerfTest]        Service '" + serviceID + "' execution time was " + (System.currentTimeMillis() - startTime) + " ms");
		}

	}

}
