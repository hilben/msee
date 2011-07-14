package at.sti2.ngsee.invoker.dummy.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.dummy.webservice.valenciaport.GeneralDeclarationBean;

@WebService(targetNamespace="http://see.sti2.at/dummy")
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA e-Freight Dummy Service.")
	)
public class ValenciaPortWebService {
	protected static Logger logger = Logger.getLogger(ValenciaPortWebService.class);
	
	
	@WebMethod
	public String submitFALForm(@WebParam(name="falForm")GeneralDeclarationBean falForm) {
		return "Received falForm "+ falForm;
	}

}
