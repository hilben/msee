package at.sti2.ngsee.invoker.core.soap;

import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;

/**
 * Soap invoker based on CXF framework
 * @author michaelrogger
 *
 */
public class SoapCXFInvoker{
	protected static Logger logger = Logger.getLogger(SoapCXFInvoker.class);
	
	private JaxWsDynamicClientFactory dcf;
	
	public SoapCXFInvoker() {
		dcf = JaxWsDynamicClientFactory.newInstance();
	}

	
	/**
	 * Dynamic
	 */
	public String invokeDynamicClient(URL wsdlURL, String operation, String... parameters) throws Exception{
		
		Client client = dcf.createClient(wsdlURL);

		Object[] res = client.invoke(operation, parameters);
		return (String)res[0];
	}
	
	/**
	 * 
	 */
//	public String invokeJaxWSProxy(URL wsdlURL, QName serviceName, String inputData){
//		
//		Service service = Service.create(wsdlURL, serviceName);
//		Iterator<QName> ports = service.getPorts();
//		while(ports.hasNext()){
//			QName port = ports.next();
//			System.out.println(port);
//		}
//		Dispatch<Source> disp = service.createDispatch(new QName("http://see.sti2.at/","PingWebServicePort"), Source.class, Service.Mode.MESSAGE);
//		
//		Source request = new StreamSource((new StringReader("<serviceID>Michael</serviceID>")));
//		Source response = disp.invoke(request);
//		
//		return response.toString();
//	}

}
