package at.sti2.msee.invocation.api;

import java.net.URL;

import javax.xml.soap.SOAPMessage;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

/**
 * This interface provides the methods for invoking of registered services.
 * 
 */
public interface ServiceInvocation {

	/**
	 * This method takes the given serviceID, operation name, and the input data
	 * (path or query parameters for RESTful services or {@link SOAPMessage} for
	 * WSDL based services) and tries to invoke the registered service. The
	 * {@link ServiceInvokerException} is thrown when the service is unknown or
	 * not registered, the operation is unknown, or the inputData is not
	 * well-formed or simply incorrect.
	 * 
	 * @param serviceID
	 * @param operation
	 * @param inputData
	 * @return result of the invocation
	 * @throws ServiceInvokerException
	 */
	public String invoke(URL serviceID, String operation, String inputData)
			throws ServiceInvokerException;

	/**
	 * Invocation method for legacy purpose.
	 * 
	 * @param serviceID
	 * @param soapMessage
	 * @return
	 * @throws ServiceInvokerException
	 */
	@Deprecated
	public String invokeSOAP(URL serviceID, /* op ? */
			String soapMessage) throws ServiceInvokerException;

}
