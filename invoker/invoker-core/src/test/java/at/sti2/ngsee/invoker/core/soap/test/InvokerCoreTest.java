/**
 * InvokerCoreTest.java - at.sti2.ngsee.invoker.core.soap.test
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
