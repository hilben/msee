package at.sti2.ngsee.invoker_client;

import at.sti2.see.InvokerWebService;
import at.sti2.see.InvokerWebServiceService;

public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InvokerWebServiceService factory = new InvokerWebServiceService();
		InvokerWebService service = factory.getInvokerWebServicePort();
		System.out.println(service.getVersion());
		
//		String inputData0 = "<id>http://sigimera.networld.to/instances/entries#drone_00-00-00-00-00-00</id>";
//		System.out.println(service.invoke("http://localhost:9091/groundstation-webservice/services/sigimeraEntity?wsdl", "get", inputData0));
		
		System.out.println(service.invoke("http://localhost:9091/groundstation-webservice/services/sigimeraID?wsdl", "getDrones", null));
	}

}
