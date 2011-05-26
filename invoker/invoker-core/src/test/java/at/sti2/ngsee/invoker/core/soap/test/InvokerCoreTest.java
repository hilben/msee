/**
 * InvokerCoreTest.java - at.sti2.ngsee.invoker.core.soap.test
 */
package at.sti2.ngsee.invoker.core.soap.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;

import at.sti2.ngsee.invoker.core.InvokerCore;

/**
 * @author Alex Oberhauser
 */
public class InvokerCoreTest extends AbstractSoapTest {
	private static int LOOPS = 10;
	
	@Test
	public void testWeatherService() throws Exception {
		List<QName> emptyHeaders = new ArrayList<QName>();
		String serviceID = "http://www.sti2.at/sesa/service/WeatherService";
		String operationName = "GetWeather";
		
		StringBuffer inputData = new StringBuffer();
		inputData.append("<GetWeather xmlns='http://www.webserviceX.NET'>");
		inputData.append("<CountryName>Austria</CountryName>");
		inputData.append("<CityName>Innsbruck</CityName>");
		inputData.append("</GetWeather>");
		
		for ( int count=0; count < LOOPS; count++ ) {
			this.startTimer();
			InvokerCore.invoke(serviceID, emptyHeaders, operationName, inputData.toString());
			long executionTime = this.stopTimer();
			System.out.println("[PerfTest] Service '" + serviceID + "' execution time was " + executionTime + " ms");
			
		}
	}
}