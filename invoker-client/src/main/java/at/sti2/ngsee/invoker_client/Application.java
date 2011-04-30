package at.sti2.ngsee.invoker_client;

import java.util.List;
import java.util.ArrayList;

import at.sti2.see.InvokerWebService;
import at.sti2.see.InvokerWebServiceService;

public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InvokerWebServiceService factory = new InvokerWebServiceService();
		InvokerWebService service = factory.getInvokerWebServicePort();
		System.out.println("Starting Service Invocation Testing Code. Proxy Service " + service.getVersion() + "\n");

		/**
		 * Test 'Global Weather Service'
		 */
		List<String> inputData1 = new ArrayList<String>();
		inputData1.add("<web:CityName xmlns:web=\"http://www.webserviceX.NET\">Innsbruck</web:CityName>");
		inputData1.add("<web:CountryName xmlns:web=\"http://www.webserviceX.NET\">Austria</web:CountryName>");
		System.out.println(service.invoke("http://www.webservicex.com/globalweather.asmx?WSDL", 
				"http://www.webserviceX.NET/GetWeather", inputData1));
		System.out.println("---\n");
		
		/**
		 * Test 'Stock Quote'
		 */
		List<String> inputData2 = new ArrayList<String>();
		inputData2.add("<web:symbol xmlns:web=\"http://www.webserviceX.NET/\">^TECDAX</web:symbol>");
		System.out.println(service.invoke("http://www.webservicex.com/stockquote.asmx?WSDL", 
				"http://www.webserviceX.NET/GetQuote/", inputData2));
		System.out.println("---\n");
		
		/**
		 * Test 'Earthquake Service'
		 * Could take a little bit longer.
		 */
//		List<String> inputData3 = new ArrayList<String>();
//		inputData3.add("<services>OBN</services>");
//		inputData3.add("<dateFrom>2010-01-01</dateFrom>");
//		inputData3.add("<dateTo>2011-04-01</dateTo>");
//		System.out.println(service.invoke("http://titan.wdcb.ru/axis/services/EarthquakeService?wsdl", 
//				"http://webapp/getData", inputData3));
//		System.out.println("---\n");
		
		/**
		 * Test 'BLZ Service'
		 */
		List<String> inputData4 = new ArrayList<String>();
		inputData4.add("<ns1:blz xmlns:ns1=\"http://thomas-bayer.com/blz/\">60050101</ns1:blz>");
		System.out.println(service.invoke("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl", 
				"http://thomas-bayer.com/blz/getBank/", inputData4));
		System.out.println("---\n");
		
		/**
		 * XXX: Reaching Timeout! Problem with SSL?
		 */
//		List<String> inputData5 = new ArrayList<String>();
//		inputData5.add("<ns1:ipAddress xmlns:ns1=\"http://ws.cdyne.com/\">138.232.65.141</ns1:ipAddress>");
//		inputData5.add("<ns1:licenseKey xmlns:ns1=\"http://ws.cdyne.com/\">0</ns1:licenseKey>");
//		System.out.println(service.invoke("http://ws.cdyne.com/ip2geo/ip2geo.asmx?wsdl", 
//				"http://ws.cdyne.com/ResolveIP/", inputData5));
//		System.out.println("---\n");
	}

}
