package at.sti2.ngsee.invoker.core.soap.test;

import java.net.URL;

import javax.xml.namespace.QName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.ngsee.invoker.core.soap.SoapAxisSimpleClient;

public class SoapAxisSimpleTest {
	private SoapAxisSimpleClient invoker;
	private long start;

	@Before
	public void setUp() throws Exception {
		invoker = new SoapAxisSimpleClient();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private void startTimer(){
		this.start = System.currentTimeMillis();
	}
	
	private void stopTimer(){
		long end = System.currentTimeMillis();
		System.out.println("took ms: " + (end-start));
	}

	/**
	 * Test invoker-dummy-webservice
	 */
//	 @Test
	public void testInvokeLocalPing() throws Exception {
		startTimer();
		String input = "<see:ping xmlns:see=\"http://see.sti2.at/\"><serviceID>Michael</serviceID></see:ping>";
		URL wsdlURL = new URL(
				"http://localhost:9090/invoker-dummy-webservice/services/ping?wsdl");
		QName operation = new QName("http://see.sti2.at/", "ping");
		invoker.invoke(wsdlURL, operation, input);
		stopTimer();
	}

//	@Test
	public void testInvokeGlobalWeather() throws Exception {
		startTimer();
		String input = "<web:GetWeather xmlns:web=\"http://www.webserviceX.NET\">"
				+ "<web:CityName>Innsbruck</web:CityName>"
				+ "<web:CountryName>Austria</web:CountryName>"
				+ "</web:GetWeather>";

		URL wsdlURL = new URL(
				"http://www.webservicex.com/globalweather.asmx?WSDL");
		QName operation = new QName("http://www.webserviceX.NET", "GetWeather");
		String result = invoker.invoke(wsdlURL, operation, input);
		System.out.println(result);
		stopTimer();
	}

	@Test
	public void testInvokeBLZService() throws Exception {
		startTimer();
		String input = "<blz:getBank xmlns:blz=\"http://thomas-bayer.com/blz/\">"
					+ "<blz:blz>60050101</blz:blz>"
					+ "</blz:getBank>";

		URL wsdlURL = new URL(
				"http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
		QName operation = new QName("http://thomas-bayer.com/blz/", "getBank");
		String result = invoker.invoke(wsdlURL, operation, input);
		System.out.println(result);
		stopTimer();
	}

}
