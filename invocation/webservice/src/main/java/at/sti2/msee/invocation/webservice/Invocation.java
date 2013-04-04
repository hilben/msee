package at.sti2.msee.invocation.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.sti2.msee.invocation.api.ServiceInvocation;
import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

/**
 * @author benni
 * 
 */

@WebService(targetNamespace = "http://msee.sti2.at/delivery/")
public interface Invocation extends ServiceInvocation {

	@Override
	@WebMethod
	public String invoke(@WebParam(name = "serviceID") String serviceID,
			@WebParam(name = "operationName") String operationName,
			@WebParam(name = "inputData") String inputData)
			throws ServiceInvokerException;
}
