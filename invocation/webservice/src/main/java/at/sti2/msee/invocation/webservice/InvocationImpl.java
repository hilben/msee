package at.sti2.msee.invocation.webservice;

import javax.jws.WebService;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.invocation.core.ServiceInvocationImpl;

@WebService(targetNamespace = "http://msee.sti2.at/delivery/", endpointInterface = "at.sti2.msee.invocation.webservice.Invocation")
public class InvocationImpl implements Invocation {

	private ServiceInvocationImpl invoker = new ServiceInvocationImpl();

	@Override
	public String invoke(String serviceID, String operationName,
			String inputData) throws ServiceInvokerException {

		return invoker.invoke(serviceID, operationName, inputData);
	}

}
