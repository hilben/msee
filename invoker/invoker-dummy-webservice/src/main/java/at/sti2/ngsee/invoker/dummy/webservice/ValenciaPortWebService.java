package at.sti2.ngsee.invoker.dummy.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.dummy.webservice.valenciaport.GeneralDeclarationBean;
import at.sti2.ngsee.invoker.dummy.webservice.valenciaport.GeneralDeclarationResponseBean;

@WebService(targetNamespace="http://sesa.sti2.at/services/dummy/")
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA e-Freight Dummy Service.")
	)
public class ValenciaPortWebService {
	protected static Logger logger = Logger.getLogger(ValenciaPortWebService.class);
	
	
	@WebMethod
	@WebResult(name="GeneralDeclarationResponse")
	public GeneralDeclarationResponseBean submitFALForm(@WebParam(name="falForm")GeneralDeclarationBean falForm) {
		GeneralDeclarationResponseBean response = new GeneralDeclarationResponseBean();
		response.setID(falForm.getGeneralDeclarationId());
		response.setShipID(falForm.getShip().getCallSign());
		return response;
	}

}
