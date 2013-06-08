package at.sti2.msee.invocation.api;

import java.net.URL;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

public interface ServiceInvocation {

	public String invokeSOAP(URL serviceID, /* op ? */
			String soapMessage) throws ServiceInvokerException;

	public String invoke(URL serviceID, String operation, String inputData)
			throws ServiceInvokerException;
}
