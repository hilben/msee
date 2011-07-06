package at.sti2.ngsee.invoker.dummy.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.apache.log4j.Logger;

@WebService(targetNamespace="http://see.sti2.at/")
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA Invoker Component.")
	)
public class ValenciaPortWebService {
	protected static Logger logger = Logger.getLogger(ValenciaPortWebService.class);
	
	@WebMethod
	public String submitFALForm(@WebParam(name="falForm")String falForm){
		return "Ping Pong "+falForm;
	}

}
