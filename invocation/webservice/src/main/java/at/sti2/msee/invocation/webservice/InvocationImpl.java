package at.sti2.msee.invocation.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.sti2.msee.invocation.api.ServiceInvocation;
import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.invocation.core.ServiceInvocationImpl;

@WebService(targetNamespace="http://msee.sti2.at/delivery/",
endpointInterface="at.sti2.msee.invocation.webservice.Invocation")
public class InvocationImpl implements Invocation {

	private ServiceInvocation invoker = new ServiceInvocationImpl();
	
	@Override
	@WebMethod
	public String invoke(@WebParam(name = "serviceID") String serviceID,
			@WebParam(name = "operationName") String operationName,
			@WebParam(name = "inputData") String inputData)
			throws ServiceInvokerException {

		return invoker.invoke(serviceID, operationName, inputData);
	}

}
