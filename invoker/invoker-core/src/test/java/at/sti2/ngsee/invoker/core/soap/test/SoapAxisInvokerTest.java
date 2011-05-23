package at.sti2.ngsee.invoker.core.soap.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axis2.AxisFault;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.ngsee.invoker.core.soap.SoapAxisInvoker;

public class SoapAxisInvokerTest extends AbstractSoapTest {

	private SoapAxisInvoker invoker;

	@Before
	public void setUp() throws Exception {
		invoker = new SoapAxisInvoker();
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	/**
	 * This weather service is sometimes down!
	 */
	public void invokeGlobalWeather() throws AxisFault, XMLStreamException,
			MalformedURLException {

		int loops = 1;

		for (int i = 0; i < loops; i++) {

			URL wsdlURL = new URL(
					"http://www.webservicex.com/globalweather.asmx?WSDL");

			String input = "<web:GetWeather xmlns:web=\"http://www.webserviceX.NET\">"
					+ "<web:CityName>Innsbruck</web:CityName>"
					+ "<web:CountryName>Austria</web:CountryName>"
					+ "</web:GetWeather>";

			List<QName> list = new ArrayList<QName>();

			invoker.invoke(wsdlURL, list,
					"http://www.webserviceX.NET/GetWeather", input);

		}
	}

	// @Test
	/**
	 * Doesn't work
	 */
	public void testInvokeLocalPing() throws Exception {
		int loops = 1;

		for (int i = 0; i < loops; i++) {
			startTimer();
			String input = "<see:ping xmlns:see=\"http://see.sti2.at/\"><serviceID>Michael</serviceID></see:ping>";
			URL wsdlURL = new URL(
					"http://localhost:9090/invoker-dummy-webservice/services/ping?wsdl");
			QName operation = new QName("http://see.sti2.at/", "ping");
			QName serviceName = new QName("http://see.sti2.at/",
					"PingWebServiceService");

			List<QName> list = new ArrayList<QName>();
			String result = invoker.invoke(wsdlURL, list, "", input);
			System.out.println(result);
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
		}
	}

//	@Test
	/**
	 * Doesn't work
	 */
	public void testBLZService() throws Exception {
		int loops = 1;

		for (int i = 0; i < loops; i++) {
			startTimer();
			String input = "<ns1:blz xmlns:ns1=\"http://thomas-bayer.com/blz/\">60050101</ns1:blz>";
			URL wsdlURL = new URL(
					"http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");

			QName operation = new QName("http://see.sti2.at/", "ping");
			QName serviceName = new QName("http://see.sti2.at/",
					"PingWebServiceService");

			List<QName> list = new ArrayList<QName>();
			String result = invoker.invoke(wsdlURL, list, "http://thomas-bayer.com/blz/getBank/", input);
			System.out.println(result);
			System.out.print("ServiceName " + serviceName + " ");
			stopTimer();
		}
	}

}
