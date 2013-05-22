package at.sti2.msee.registration.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.model.ServiceModelFormat;
import at.sti2.msee.model.ServiceModelFormatDetector;
import at.sti2.msee.model.transformer.Service2RDFTransformer;
import at.sti2.msee.model.transformer.Service2RDFTransformerException;
import at.sti2.msee.model.transformer.Service2RDFTransformerFactory;

import at.sti2.msee.registration.api.ServiceRegistration;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;

import at.sti2.msee.triplestore.ServiceRepository;

public class ServiceRegistrationImpl implements ServiceRegistration {
	Logger LOGGER = Logger.getLogger(this.getClass().getCanonicalName());

	private ServiceRepository serviceRepository = null;
			
	public ServiceRegistrationImpl(ServiceRepository serviceRepository) {
		this.serviceRepository = serviceRepository;
		try {
			this.serviceRepository.init();
		} catch (RepositoryException e) {
			LOGGER.error(e.getMessage() + Arrays.toString(e.getStackTrace()));
		}
	}
	
	@Override
	public String registerInContext(String serviceDescriptionURL, String contextURI) throws ServiceRegistrationException {
		
		URL descriptionURL = this.getServiceDescriptionURL(serviceDescriptionURL);
		ServiceModelFormat format = this.getServiceDescriptionFormat(descriptionURL);
		
		String serviceURI = register(descriptionURL, format, contextURI);
			
		return serviceURI;
	}

	@Override
	public String register(String serviceDescriptionURL) throws ServiceRegistrationException {
		return this.registerInContext(serviceDescriptionURL,null);			
	}

	
	private String register(URL descriptionURL, ServiceModelFormat format, String contextURI)
			throws ServiceRegistrationException {
		try 
		{
			Service2RDFTransformer transformer = Service2RDFTransformerFactory.newInstance(format);
			String msmRDF = transformer.toMSM(descriptionURL);		
			
			String serviceURI = this.serviceRepository.insert(msmRDF, contextURI);
			return serviceURI;
		} 
		catch (ModelRuntimeException | IOException | Service2RDFTransformerException e) {
			throw new ServiceRegistrationException(e);
		}
	}

	private ServiceModelFormat getServiceDescriptionFormat(URL descriptionURL)
			throws ServiceRegistrationException {
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		ServiceModelFormat format = detector.detect(descriptionURL);
		if (format == ServiceModelFormat.UNKNOWN){
			throw new ServiceRegistrationException("Service format not recognized");
		}
		return format;
	}	

	private URL getServiceDescriptionURL(String serviceDescriptionURL) throws ServiceRegistrationException {
		try {
			return new URL(serviceDescriptionURL);
		} catch (MalformedURLException e) {
			throw new ServiceRegistrationException(e.getCause());
		}
	}

	@Override
	public String deregister(String serviceURI) throws ServiceRegistrationException {
		throw new ServiceRegistrationException("Not implemented");
	}
	
	@Override
	public String update(String serviceURI, String serviceURL) throws ServiceRegistrationException {
		throw new ServiceRegistrationException("Not implemented");
	}
}