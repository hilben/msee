package at.sti2.msee.invocation.api;

import java.net.URL;
import java.util.Map;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

public interface ServiceInvocation {

	public String invokeSOAP(URL serviceID, /* op ? */
			String soapMessage) throws ServiceInvokerException;

	String invokeREST(URL serviceID, String address, String method, Map<String, String> parameters)
			throws ServiceInvokerException;
}
