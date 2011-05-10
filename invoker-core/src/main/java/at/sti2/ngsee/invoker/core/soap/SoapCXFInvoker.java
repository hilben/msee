package at.sti2.ngsee.invoker.core.soap;

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.api.core.ISOAPInvoker;

/**
 * Soap invoker based on CXF framework
 * @author michaelrogger
 *
 */
public class SoapCXFInvoker{
	protected static Logger logger = Logger.getLogger(SoapCXFInvoker.class);


	public String invoke(URL wsdlURL, String operation,String... parameters) throws Exception {
		
		long start = System.currentTimeMillis();
		useDynamicClient(wsdlURL,operation,parameters);
		long end = System.currentTimeMillis();
		
		System.out.println("took ms: "+(end-start));
		
//		useDispatchClient(wsdlURL);
		
		return "";
	}
	
	private void useDynamicClient(URL wsdlURL, String operation, String... parameters) throws Exception{
		
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(wsdlURL);

		Object[] res = client.invoke(operation, parameters);
		System.out.println("Response: " + res[0]);
	}
	
//	private void useDispatchClient(URL wsdlURL){
//		Service service = Service.create(wsdlURL, new QName("http://www.webserviceX.NET/GlobalWeather"));
//		Dispatch<Source> disp = service.createDispatch(new QName("GlobalWeatherSoap12"), Source.class, Service.Mode.PAYLOAD);
//
//		Source request = new StreamSource("<hello/>");
//		Source response = disp.invoke(request);
//		System.out.println("Response" + response);
//	}

}
