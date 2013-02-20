package at.sti2.msee.registration.api;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;

public interface ServiceRegistration {

	public String register(String serviceDescriptionURL) throws ServiceRegistrationException;
	
	public String deregister(String serviceURI) throws ServiceRegistrationException;
	
	public String update(String serviceURI, String serviceURL) throws ServiceRegistrationException;
	
}
