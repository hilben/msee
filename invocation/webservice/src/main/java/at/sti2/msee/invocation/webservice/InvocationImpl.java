package at.sti2.msee.invocation.webservice;

import java.net.URL;

import javax.jws.WebService;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.invocation.core.ServiceInvocationImpl;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

@WebService(targetNamespace = "http://msee.sti2.at/delivery/", endpointInterface = "at.sti2.msee.invocation.webservice.Invocation")
public class InvocationImpl implements Invocation {

	private ServiceInvocationImpl invoker = null;

	public InvocationImpl() {
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		ServiceRepository serviceRepository = ServiceRepositoryFactory
				.newInstance(serviceRepositoryConfiguration);
		invoker = new ServiceInvocationImpl(serviceRepository);
	}

	@Override
	public String invokeSOAP(URL serviceID, String inputData) throws ServiceInvokerException {
		return invoker.invokeSOAP(serviceID, inputData);
	}

	@Override
	public String invoke(URL serviceID, String operation, String inputData)
			throws ServiceInvokerException {
		return invoker.invoke(serviceID, operation, inputData);
	}

}
