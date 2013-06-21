package at.sti2.msee.registration.core;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.util.RDFTool;
import org.ontoware.rdf2go.vocabulary.RDF;
import org.ontoware.rdf2go.vocabulary.RDFS;
import org.openrdf.repository.RepositoryException;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;

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
	public String registerInContext(String serviceDescriptionURL, String contextURI)
			throws ServiceRegistrationException {

		URL descriptionURL = this.getServiceDescriptionURL(serviceDescriptionURL);
		ServiceModelFormat format = this.getServiceDescriptionFormat(descriptionURL);

		String serviceURI = register(descriptionURL, format, contextURI);

		return serviceURI;
	}

	@Override
	public String register(String serviceDescriptionURL) throws ServiceRegistrationException {
		return this.registerInContext(serviceDescriptionURL, null);
	}

	private String register(URL descriptionURL, ServiceModelFormat format, String contextURI)
			throws ServiceRegistrationException {
		try {
			Service2RDFTransformer transformer = Service2RDFTransformerFactory.newInstance(format);
			String msmRDF = new MSMChecker(transformer.toMSM(descriptionURL)).check();

			if (contextURI == null) {
				contextURI = findContextURI(msmRDF);
			}

			String serviceURI = this.serviceRepository.insert(msmRDF, contextURI);

			Model isdefinedbyModel = RDF2Go.getModelFactory().createModel();
			isdefinedbyModel.open();
			isdefinedbyModel.addStatement(contextURI, RDFS.isDefinedBy, descriptionURL.toString());
			serviceRepository.insertModel(isdefinedbyModel, new URIImpl(contextURI));
			isdefinedbyModel.close();

			return serviceURI;
		} catch (ModelRuntimeException | IOException | Service2RDFTransformerException e) {
			throw new ServiceRegistrationException(e);
		}
	}

	/**
	 * Returns the service URI for a given rdf model.
	 * 
	 * @param msmRDF
	 * @throws ModelRuntimeException
	 * @throws IOException
	 */
	private String findContextURI(String msmRDF) throws ModelRuntimeException, IOException {
		Model msmModel = RDF2Go.getModelFactory().createModel();
		msmModel.open();
		StringReader reader = new StringReader(msmRDF);
		msmModel.readFrom(reader);
		Statement serviceStatement = RDFTool.findStatement(msmModel, Variable.ANY, RDF.type,
				MSM.Service);
		String contextURI = serviceStatement.getSubject().toString();
		msmModel.close();
		return contextURI;
	}

	private ServiceModelFormat getServiceDescriptionFormat(URL descriptionURL)
			throws ServiceRegistrationException {
		ServiceModelFormatDetector detector = new ServiceModelFormatDetector();
		ServiceModelFormat format = detector.detect(descriptionURL);
		if (format == ServiceModelFormat.UNKNOWN) {
			throw new ServiceRegistrationException("Service format not recognized - Check "
					+ "that xmlns:sawsdl=\"http://www.w3.org/ns/sawsdl\" is contained");
		}
		return format;
	}

	private URL getServiceDescriptionURL(String serviceDescriptionURL)
			throws ServiceRegistrationException {
		try {
			return new URL(serviceDescriptionURL);
		} catch (MalformedURLException e) {
			throw new ServiceRegistrationException(e.getCause());
		}
	}

	@Override
	public String deregister(String serviceURI) throws ServiceRegistrationException {
		Model contextModel = serviceRepository.getModel(serviceURI);
		contextModel.open();
		contextModel.removeAll();
		contextModel.close();
		return serviceURI;

	}

	@Override
	public String update(String serviceURI, String serviceURL) throws ServiceRegistrationException {
		deregister(serviceURI);
		return register(serviceURL);
	}
}