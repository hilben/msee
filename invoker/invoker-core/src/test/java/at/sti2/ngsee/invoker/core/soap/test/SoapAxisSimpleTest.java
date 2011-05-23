package at.sti2.ngsee.invoker.core.soap.test;

import java.net.URL;

import javax.xml.namespace.QName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.ngsee.invoker.core.soap.SoapAxisSimpleClient;

/**
 * TODO: use log4j
 * 
 * @author michaelrogger
 * 
 */
public class SoapAxisSimpleTest extends AbstractSoapTest{
	private SoapAxisSimpleClient invoker;

	@Before
	public void setUp() throws Exception {
		invoker = new SoapAxisSimpleClient();
	}

	@After
	public void tearDown() throws Exception {
	}


	/**
	 * Test invoker-dummy-webservice
	 */
//	@Test
	public void testInvokeLocalPing() throws Exception {

		// >1 loop doesn't work. WFT??
		int loops = 10;

		for (int i = 0; i < loops; i++) {

			startTimer();
			String input = "<see:ping xmlns:see=\"http://see.sti2.at/\"><serviceID>Michael</serviceID></see:ping>";
			URL wsdlURL = new URL(
					"http://localhost:9090/invoker-dummy-webservice/services/ping?wsdl");
			QName operation = new QName("http://see.sti2.at/", "ping");
			QName serviceName = new QName("http://see.sti2.at/",
					"PingWebServiceService");
			String result = invoker.invoke(wsdlURL, serviceName, operation,
					input);
			System.out.println(result);
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
			System.out.println();

			//not overload the server
//			Thread.sleep(1000);
		}
	}

	// @Test
	/**
	 * This weather service is sometimes down!
	 */
	public void testInvokeGlobalWeather() throws Exception {
		startTimer();
		String input = "<web:GetWeather xmlns:web=\"http://www.webserviceX.NET\">"
				+ "<web:CityName>Innsbruck</web:CityName>"
				+ "<web:CountryName>Austria</web:CountryName>"
				+ "</web:GetWeather>";

		URL wsdlURL = new URL(
				"http://www.webservicex.com/globalweather.asmx?WSDL");
		QName operation = new QName("http://www.webserviceX.NET", "GetWeather");
		QName serviceName = new QName("http://see.sti2.at/",
				"PingWebServiceService");
		String result = invoker.invoke(wsdlURL, serviceName, operation, input);
		System.out.println(result);
		stopTimer();
	}

//	@Test
	public void testInvokeBLZService() throws Exception {
		
		// >2 loop doesn't work. WFT??
		int loops = 2;

		for (int i = 0; i < loops; i++) {
			startTimer();
			String input = "<blz:getBank xmlns:blz=\"http://thomas-bayer.com/blz/\">"
				+ "<blz:blz>60050101</blz:blz>" + "</blz:getBank>";
			
			URL wsdlURL = new URL(
			"http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
			QName operation = new QName("http://thomas-bayer.com/blz/", "getBank");
			QName serviceName = new QName("http://thomas-bayer.com/blz/",
			"BLZService");
			String result = invoker.invoke(wsdlURL, serviceName, operation, input);
			System.out.println(result);
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
			System.out.println();
			
			//not overload the server
//			Thread.sleep(1000);
		}
	}

}
