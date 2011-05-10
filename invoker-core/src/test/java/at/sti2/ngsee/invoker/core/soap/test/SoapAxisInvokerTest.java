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

public class SoapAxisInvokerTest {

	private SoapAxisInvoker invoker;
	@Before
	public void setUp() throws Exception {
		invoker = new SoapAxisInvoker();
	}

	@After
	public void tearDown() throws Exception {
	}
	
//	@Test
	public void invokeGlobalWeather() throws AxisFault, XMLStreamException, MalformedURLException{
		URL wsdlURL = new URL("http://www.webservicex.com/globalweather.asmx?WSDL");
		
		String input = "<web:GetWeather xmlns:web=\"http://www.webserviceX.NET\">"
			+ "<web:CityName>Innsbruck</web:CityName>"
			+ "<web:CountryName>Austria</web:CountryName>"
			+ "</web:GetWeather>";
		
		List<QName> list = new ArrayList<QName>();
		
		invoker.invoke(wsdlURL, list, "http://www.webserviceX.NET/GetWeather", input);
		
	}

}
