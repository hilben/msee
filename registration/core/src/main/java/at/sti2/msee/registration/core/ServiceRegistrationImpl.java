package at.sti2.msee.registration.core;

import at.sti2.msee.registration.api.ServiceRegistration;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.management.RegistrationWSDLToTriplestoreWriter;

public class ServiceRegistrationImpl implements ServiceRegistration {

	
	public ServiceRegistrationImpl()
	{
		
	}
	
	@Override
	public String register(String serviceDescriptionURL) throws ServiceRegistrationException {
		
		//read file from URL
		
		//convert to wsdl
		
//		return TransformationWSDL.transformWSDL(serviceDescriptionURL);
		RegistrationWSDLToTriplestoreWriter writer = new RegistrationWSDLToTriplestoreWriter();
		return writer.transformWSDLtoTriplesAndStoreInTripleStore(serviceDescriptionURL);
		
	}

	@Override
	public String deregister(String serviceURI)
			throws ServiceRegistrationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String serviceURI, String serviceURL)
			throws ServiceRegistrationException {
		// TODO Auto-generated method stub
		return null;
	}

}
