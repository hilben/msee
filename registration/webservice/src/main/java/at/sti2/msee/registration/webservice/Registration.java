package at.sti2.msee.registration.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.sti2.msee.registration.api.ServiceRegistration;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;

@WebService(targetNamespace="http://msee.sti2.at/delivery/")
public interface Registration extends ServiceRegistration
{
	@Override
	@WebMethod
	public String register(@WebParam(name="serviceDescriptionURL")String serviceDescriptionURL) throws ServiceRegistrationException; 

	@Override
	@WebMethod
	public String deregister(@WebParam(name="serviceURI")String serviceURI) throws ServiceRegistrationException;

	@Override
	@WebMethod
	public String update(@WebParam(name="serviceURI")String serviceURI, 
			@WebParam(name="serviceURL")String serviceURL)
			throws ServiceRegistrationException; 
}