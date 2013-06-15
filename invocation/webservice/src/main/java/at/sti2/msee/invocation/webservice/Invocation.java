package at.sti2.msee.invocation.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;

import at.sti2.msee.invocation.api.ServiceInvocation;
import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

/**
 * @author benni
 * 
 */

public interface Invocation extends ServiceInvocation {

	@Override
	@WebMethod
	public String invokeSOAP(@WebParam(name = "serviceIDURL") String serviceIDURL,
			@WebParam(name = "inputData") String inputData)
			throws ServiceInvokerException;
}
