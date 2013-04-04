package at.sti2.msee.invocation.api;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

public interface ServiceInvocation {

	public String invoke(String serviceID, String operationName,
			String inputData) throws ServiceInvokerException;
}
