package at.sti2.msee.invocation.api;

import java.net.URL;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

public interface ServiceInvocation {

	public String invoke(URL serviceID, String operationName,
			String inputData) throws ServiceInvokerException;
}
