package at.sti2.ngsee.invoker.client;

import at.sti2.see.Exception_Exception;
import at.sti2.see.InvokerWebService;
import at.sti2.see.InvokerWebServiceService;

public class InvokerClient {

	private static final int LOOPS = 5;
	
	/**
	 * @param args
	 * @throws Exception_Exception 
	 */
	public static void main(String[] args) throws Exception_Exception {
		InvokerWebServiceService factory = new InvokerWebServiceService();
		InvokerWebService service = factory.getInvokerWebServicePort();
		
		String serviceID = "http://www.sti2.at/sesa/service/WeatherService";
		String operationName = "GetWeather";
		
		StringBuffer inputData = new StringBuffer();
		inputData.append("<GetWeather xmlns='http://www.webserviceX.NET'>");
		inputData.append("<CountryName>Austria</CountryName>");
		inputData.append("<CityName>Innsbruck</CityName>");
		inputData.append("</GetWeather>");

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
