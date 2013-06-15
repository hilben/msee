package at.sti2.msee.invocation.webservice;

import java.io.IOException;

import javax.jws.WebService;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.invocation.core.ServiceInvocationImpl;
import at.sti2.msee.triplestore.ServiceRepository;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;
import at.sti2.msee.triplestore.ServiceRepositoryFactory;

/**
 * This class provides the functionality that is called by the web server.
 * 
 * @author Christian Mayr
 * 
 */
@WebService(targetNamespace = "http://msee.sti2.at/delivery/invocation/", endpointInterface = "at.sti2.msee.invocation.api.ServiceInvocation", portName = "InvocationServicePort", serviceName = "service")
public class InvocationImpl implements Invocation {

	private ServiceInvocationImpl invoker = null;

	public InvocationImpl() {
		// RepositoryConfig config = null;
		// try {
		// config = new RepositoryConfig();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		ServiceRepositoryConfiguration serviceRepositoryConfiguration = new ServiceRepositoryConfiguration();
		//serviceRepositoryConfiguration.setServerEndpoint(config.getSesameEndpoint());
		//serviceRepositoryConfiguration.setRepositoryID(config.getSesameRepositoryID());
		serviceRepositoryConfiguration.setServerEndpoint("http://sesa.sti2.at:8080/openrdf-sesame");
		serviceRepositoryConfiguration.setRepositoryID("msee");
		ServiceRepository serviceRepository = ServiceRepositoryFactory
				.newInstance(serviceRepositoryConfiguration);
		invoker = new ServiceInvocationImpl(serviceRepository);
	}

	@Override
	public String invokeSOAP(String serviceIDURL, String inputData) throws ServiceInvokerException {
		return invoker.invokeSOAP(serviceIDURL, inputData);
	}

	@Override
	public String invoke(String serviceIDURL, String operation, String inputData)
			throws ServiceInvokerException {
		return invoker.invoke(serviceIDURL, operation, inputData);
	}

}
