package at.sti2.msee.invocation.webservice;

import java.net.URL;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang.NotImplementedException;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.invocation.core.ServiceInvocationImpl;

@WebService(targetNamespace = "http://msee.sti2.at/delivery/", endpointInterface = "at.sti2.msee.invocation.webservice.Invocation")
public class InvocationImpl implements Invocation {

	private ServiceInvocationImpl invoker = new ServiceInvocationImpl();

	@Override
	public String invokeSOAP(URL serviceID, String inputData) throws ServiceInvokerException {
		return invoker.invokeSOAP(serviceID, inputData);
	}

	@Override
	public String invokeREST(URL serviceID, String address, String method,
			Map<String, String> parameters) throws ServiceInvokerException {
		throw new NotImplementedException();
	}

	@Override
	public String invoke(URL serviceID, String operation, String inputData)
			throws ServiceInvokerException {
		throw new NotImplementedException();
	}

}
