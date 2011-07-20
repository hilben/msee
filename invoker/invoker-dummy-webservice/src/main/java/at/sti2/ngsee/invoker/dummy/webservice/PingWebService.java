package at.sti2.ngsee.invoker.dummy.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.apache.log4j.Logger;

@WebService(targetNamespace="http://sesa.sti2.at/services/dummy/")
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA Example Service")
	)
public class PingWebService {
	protected static Logger logger = Logger.getLogger(PingWebService.class);
	
	@WebMethod
	public String ping(@WebParam(name="serviceID")String msg){
		return "Ping Pong "+msg;
	}

}
