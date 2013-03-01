package at.sti2.msee.registration.core;

import java.net.MalformedURLException;
import java.net.URL;

import at.sti2.msee.registration.api.ServiceRegistration;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.wsdl2rdf.Service2RDFTransformer;
import at.sti2.msee.registration.core.wsdl2rdf.Service2RDFTransformerException;
import at.sti2.msee.registration.core.wsdl2rdf.Service2RDFTransformerFactory;

public class ServiceRegistrationImpl implements ServiceRegistration {

	public ServiceRegistrationImpl() {

	}

	@Override
	public String register(String serviceDescriptionURL) throws ServiceRegistrationException {
		
		URL descriptionURL = this.getServiceDescriptionURL(serviceDescriptionURL);
		
		//Detect type of content?? content negotiation??
		
		try 
		{
			//SAWSDL2RDF
			Service2RDFTransformer wsdl2RDFTransformer = Service2RDFTransformerFactory.newInstance(Service2RDFTransformer.SAWSDL);
			String serviceDescriptionRDF = wsdl2RDFTransformer.toMSM(descriptionURL);
			return serviceDescriptionRDF;
		} 
		catch (Service2RDFTransformerException e) {
			throw new ServiceRegistrationException(e.getCause());
		}		
		
		//Save RDF in repository
		
//		RegistrationWSDLToTriplestoreWriter writer = new RegistrationWSDLToTriplestoreWriter();
//		return writer.transformWSDLtoTriplesAndStoreInTripleStore(serviceDescriptionURL);

	}	

	private URL getServiceDescriptionURL(String serviceDescriptionURL) throws ServiceRegistrationException {
		try {
			return new URL(serviceDescriptionURL);
		} catch (MalformedURLException e) {
			throw new ServiceRegistrationException(e.getCause());
		}
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
