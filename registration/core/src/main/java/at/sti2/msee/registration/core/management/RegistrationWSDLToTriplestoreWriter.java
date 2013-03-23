package at.sti2.msee.registration.core.management;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.query.resultio.UnsupportedQueryResultFormatException;
import org.openrdf.repository.RepositoryException;
import org.ow2.easywsdl.extensions.sawsdl.SAWSDLFactory;
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLException;
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLReader;
import org.ow2.easywsdl.extensions.sawsdl.api.Types;
import org.ow2.easywsdl.extensions.sawsdl.api.schema.Element;
import org.ow2.easywsdl.extensions.sawsdl.api.schema.Schema;
import org.ow2.easywsdl.extensions.sawsdl.impl.schema.ComplexTypeImpl;
import org.ow2.easywsdl.extensions.wsdl4complexwsdl.WSDL4ComplexWsdlFactory;
import org.ow2.easywsdl.extensions.wsdl4complexwsdl.api.Description;
import org.ow2.easywsdl.extensions.wsdl4complexwsdl.api.WSDL4ComplexWsdlException;
import org.ow2.easywsdl.extensions.wsdl4complexwsdl.api.WSDL4ComplexWsdlReader;
import org.ow2.easywsdl.wsdl.api.Binding;
import org.ow2.easywsdl.wsdl.api.BindingOperation;
import org.ow2.easywsdl.wsdl.api.Endpoint;
import org.ow2.easywsdl.wsdl.api.Input;
import org.ow2.easywsdl.wsdl.api.InterfaceType;
import org.ow2.easywsdl.wsdl.api.Operation;
import org.ow2.easywsdl.wsdl.api.Output;
import org.ow2.easywsdl.wsdl.api.Service;


import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.common.RegistrationConfig;
import at.sti2.util.triplestore.RepositoryHandler;

/**
 * 
 * TODO: Better exception handling!
 * 
 * @author 
 * 
 * @author Benjamin Hiltpolt
 * 
 *         This class converts a wsdlURI to triples and stores in into a triple
 *         store.
 * 
 *         Using the WSDL Url as context to identify WSDL file related tripples
 * 
 */
/**
 * @author benni
 *
 */
public class RegistrationWSDLToTriplestoreWriter {

	private static Logger logger = Logger
			.getLogger(RegistrationWSDLToTriplestoreWriter.class);

	private Map<QName, Element> elementsMap;
	private RegistrationTriplestoreWriter repowriter = null;
	private String wsdlURLString;
	private URL wsdlURL;

	/**
	 * Reader for SAWSDL descriptions
	 */
	private SAWSDLReader readerSAWSDL;

	private org.ow2.easywsdl.extensions.sawsdl.api.Description descSAWSDL;
	private WSDL4ComplexWsdlReader reader;
	private Description desc;
	private Types types;
	private List<Schema> schemas;

	/**
	 * 
	 * Transforms a WSDL file by adding its information into the triple store if
	 * valid
	 * 
	 * @param wsdlURLString
	 *            the url of the wsdl file
	 * @return
	 * @throws ServiceRegistrationException
	 *             if there occur any parsing, repository, etc. error this
	 *             exception is thrown
	 * @throws URISyntaxException
	 */
	public String transformWSDLtoTriplesAndStoreInTripleStore(String wsdlURLString) throws ServiceRegistrationException {
		try {
			// Init readers
			this.doPreperations(wsdlURLString);

			// Go through all the services and write them to the triple store
			for (Service service : desc.getServices()) {
				String serviceComplete = service.getQName().getNamespaceURI()+"#"+service.getQName().getLocalPart();
				this.writeServiceToTriplestore(service);
				if(alreadyInTripleStore(serviceComplete)){
					repowriter.rollback();
				} else {
					repowriter.commit();
				}
			}

			return repowriter.getServiceID();

		} catch (RepositoryException e) {
			throw new ServiceRegistrationException(
					"Errors durring saving of triples into repository.",
					e.getCause());
		} catch (SAWSDLException e) {
			throw new ServiceRegistrationException(
					"Errors durring parsing the service.", e.getCause());
		} catch (FileNotFoundException e) {
			throw new ServiceRegistrationException(
					"The configuration file was NOT found.", e.getCause());
		} catch (MalformedURLException e) {
			throw new ServiceRegistrationException("The provided URL is malformed.",
					e.getCause());
		} catch (IOException e) {
			throw new ServiceRegistrationException(
					"The repository endpoint ID or the WSDL file could NOT be found.",
					e.getCause());
		} catch (WSDL4ComplexWsdlException e) {
			throw new ServiceRegistrationException(
					"Errors durring parsing the service.", e.getCause());
		} catch (URISyntaxException e) {
			throw new ServiceRegistrationException(
					"Errors durring parsing the service. An URI seems to have a bad format",
					e.getCause());
		} catch (QueryEvaluationException e) {
			throw new ServiceRegistrationException(e);
		} catch (MalformedQueryException e) {
			throw new ServiceRegistrationException(e);
		} catch (TupleQueryResultHandlerException e) {
			throw new ServiceRegistrationException(e);
		} catch (UnsupportedQueryResultFormatException e) {
			throw new ServiceRegistrationException(e);
		} finally {
			/*
			 * Something went wrong
			 */
		}
	}

	/**
	 * 
	 * Writes a web service to the triple store
	 * 
	 * @param service
	 * @throws IOException
	 * @throws SAWSDLException
	 * @throws ServiceRegistrationException
	 * @throws RepositoryException
	 * @throws URISyntaxException
	 */
	private void writeServiceToTriplestore(Service service) throws IOException,
			SAWSDLException, ServiceRegistrationException, RepositoryException,
			URISyntaxException {
		String SERVICE_NAME = service.getQName().getLocalPart();
		String serviceNamespace = service.getQName().getNamespaceURI() + "#";
		String namespaceURI = service.getQName().getNamespaceURI();

		if (serviceNamespace == null || SERVICE_NAME == null) {
			throw new ServiceRegistrationException(
					"service name or service namespace is null");
		}

		this.repowriter = new RegistrationTriplestoreWriter(SERVICE_NAME,
				serviceNamespace, elementsMap);

		/*
		 * Get categories of the current service
		 */
		org.ow2.easywsdl.extensions.sawsdl.api.Service serviceSAWSDL = descSAWSDL
				.getService(service.getQName());
		final List<URI> categories = serviceSAWSDL.getModelReference();

		/*
		 * Check if there are categories else abort
		 */
		if (categories.size() == 0) {
			logger.error("The service MUST be at least annotated with one service category.");
			throw new ServiceRegistrationException(
					"The service MUST be at least annotated with one service category. "
							+ "For documentation see: http://www.sesa.sti2.at/doc/service_annotation");
		}

		this.repowriter.writeServiceToTriples(categories, namespaceURI,
				wsdlURLString);

		// Go through all endpoints of the services and store them into
		// the triple store
		this.writeEndpointIntoTripleStore(service);

	}

	/**
	 * 
	 * Set up some variables needed by the class
	 * 
	 * @param wsdlURLString
	 * @throws ServiceRegistrationException
	 * @throws SAWSDLException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws WSDL4ComplexWsdlException
	 */
	private void doPreperations(String wsdlURLString)
			throws ServiceRegistrationException, SAWSDLException, IOException,
			URISyntaxException, WSDL4ComplexWsdlException {

		this.wsdlURLString = wsdlURLString;
		this.wsdlURL = new URL(wsdlURLString);

		// Initialize Reader for SAWSDL descriptions
		this.readerSAWSDL = SAWSDLFactory.newInstance().newSAWSDLReader();

		this.reader = WSDL4ComplexWsdlFactory.newInstance().newWSDLReader();

		this.descSAWSDL = this.readerSAWSDL.read(this.wsdlURL);

		this.desc = reader.read(this.wsdlURL);

		this.desc.addImportedDocumentsInWsdl();

		// Get all elements of the xml
		this.elementsMap = new HashMap<QName, Element>();
		this.types = this.descSAWSDL.getTypes();

		this.schemas = this.types.getSchemas();

		for (org.ow2.easywsdl.extensions.sawsdl.api.schema.Schema schema : this.schemas) {
			List<Element> elements = schema.getElements();

			for (Element elem : elements) {
				Object obj = elem.getType();
				if (obj instanceof ComplexTypeImpl) {
					ComplexTypeImpl type = (ComplexTypeImpl) obj;
					List<Element> subElements = type.getSequence()
							.getElements();

					for (Element subElem : subElements) {
						this.elementsMap.put(subElem.getQName(), subElem);
					}
				}
				this.elementsMap.put(elem.getQName(), elem);
			}
		}

	}

	/**
	 * 
	 * Writes all the bindings to the triplestore
	 * 
	 * @param binding
	 * @throws RepositoryException
	 * @throws URISyntaxException
	 */
	private void writeBindingsToTriplestore(Binding binding)
			throws RepositoryException, URISyntaxException {
		String bindingName = binding.getQName().getLocalPart();
		String bindingType = binding.getTypeOfBinding().value().toString();

		this.repowriter.writeBindingsToTriples(bindingName, bindingType,
				this.wsdlURLString);

		for (BindingOperation bindingOperation : binding.getBindingOperations()) {
			String bindingOperationName = bindingOperation.getQName()
					.getLocalPart();
			String soapAction = bindingOperation.getSoapAction();

			this.repowriter.writeBindingOperationsToTriples(bindingName,
					bindingOperationName, soapAction, this.wsdlURLString);
		}
	}

	
	/**
	 * Writes all the operation inputs to the triple store
	 * 
	 * @param input
	 * @param interfaceName
	 * @param interfaceOperationName
	 * @param pattern
	 * @throws RepositoryException
	 * @throws ServiceRegistrationException 
	 * @throws SAWSDLException 
	 */
	private void writeOperationInputToTriplestore(Input input,
			String interfaceName, String interfaceOperationName, String pattern)
			throws RepositoryException, SAWSDLException, ServiceRegistrationException {

		QName inputMsgLabel = input.getMessageName();
		String inputMsgLabelName = null;
		if (inputMsgLabel != null) {
			inputMsgLabelName = inputMsgLabel.getLocalPart();
		}

		this.repowriter.writeInterfaceMessageReferenceToTriples(interfaceName,
				interfaceOperationName, inputMsgLabelName, pattern,
				this.wsdlURLString);

		org.ow2.easywsdl.schema.api.Element inputElem = input.getElement();
		if (inputElem != null) {
			// Checks for lowering lifting schemas
				this.repowriter.checkAnnotations(inputElem.getQName());

				this.repowriter.writeElementDeclaration(inputElem.getQName(),
						interfaceName, interfaceOperationName,
						inputMsgLabelName, this.wsdlURLString);

				Object intype = inputElem.getType();
				if (intype instanceof org.ow2.easywsdl.schema.impl.ComplexTypeImpl) {
					org.ow2.easywsdl.schema.impl.ComplexTypeImpl complexType = (org.ow2.easywsdl.schema.impl.ComplexTypeImpl) intype;
					List<org.ow2.easywsdl.schema.api.Element> elements = complexType
							.getSequence().getElements();
					for (org.ow2.easywsdl.schema.api.Element elem : elements) {
						this.repowriter.writeSubelementDeclaration(
								inputElem.getQName(), elem.getQName(),
								this.wsdlURLString);
					}
				}

		}
	}

	
	/**
	 *  Writes all the operation outputs to the triple store
	 * 
	 * @param output
	 * @param interfaceName
	 * @param interfaceOperationName
	 * @param pattern
	 * @throws RepositoryException
	 */
	private void writeOperationOutputToTriplestore(Output output,
			String interfaceName, String interfaceOperationName, String pattern)
			throws RepositoryException {

		QName outputMsgLabel = output.getMessageName();
		String outputMsgLabelName = null;
		if (outputMsgLabel != null) {
			outputMsgLabelName = outputMsgLabel.getLocalPart();
		}

		this.repowriter.writeInterfaceMessageReferenceToTriples(interfaceName,
				interfaceOperationName, outputMsgLabelName, pattern,
				this.wsdlURLString);

		org.ow2.easywsdl.schema.api.Element outputElem = output.getElement();
		if (outputElem != null) {
			try {
				this.repowriter.checkAnnotations(outputElem.getQName());

				this.repowriter.writeElementDeclaration(outputElem.getQName(),
						interfaceName, interfaceOperationName,
						outputMsgLabelName, this.wsdlURLString);

				Object outtype = outputElem.getType();
				if (outtype instanceof org.ow2.easywsdl.schema.impl.ComplexTypeImpl) {
					org.ow2.easywsdl.schema.impl.ComplexTypeImpl complexType = (org.ow2.easywsdl.schema.impl.ComplexTypeImpl) outtype;
					List<org.ow2.easywsdl.schema.api.Element> elements = complexType
							.getSequence().getElements();
					for (org.ow2.easywsdl.schema.api.Element elem : elements) {
						this.repowriter.writeSubelementDeclaration(
								outputElem.getQName(), elem.getQName(),
								this.wsdlURLString);
					}
				}
			} catch (SAWSDLException e) {
				e.printStackTrace();
			} catch (ServiceRegistrationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * Writes endpoints of the WSDL file to the triplestore
	 * 
	 * @param service
	 * @throws RepositoryException
	 * @throws URISyntaxException
	 * @throws ServiceRegistrationException 
	 * @throws SAWSDLException 
	 */
	private void writeEndpointIntoTripleStore(Service service)
			throws RepositoryException, URISyntaxException, SAWSDLException, ServiceRegistrationException {
		for (Endpoint endpoint : service.getEndpoints()) {

			String endpointName = endpoint.getName();
			String endpointAddress = endpoint.getAddress();

			this.repowriter.writeEndpointsToTriples(endpointName,
					endpointAddress, this.wsdlURLString);

			// Obtain all bindings of the endoint and store them to the triple
			// store
			Binding binding = endpoint.getBinding();

			// Store all the bindins
			this.writeBindingsToTriplestore(binding);

			// Store the interface
			this.writeInterfaceToTriplestore(binding.getInterface());

		}
	}

	/**
	 * 
	 * Store an interface
	 * 
	 * @param wsdlInterface
	 * @throws RepositoryException
	 * @throws ServiceRegistrationException 
	 * @throws SAWSDLException 
	 */
	private void writeInterfaceToTriplestore(InterfaceType wsdlInterface)
			throws RepositoryException, SAWSDLException, ServiceRegistrationException {
		// Interface
		String interfaceName = wsdlInterface.getQName().getLocalPart();

		this.repowriter.writeInterfaceToTriples(interfaceName,
				this.wsdlURLString);

		// Store the input and the output for all operations
		for (Operation interfaceOperation : wsdlInterface.getOperations()) {

			this.writeInterfaceOperationToTriplestore(interfaceOperation,
					interfaceName);
		}
	}

	/**
	 * 
	 * Store all the operations of an interface
	 * 
	 * @param interfaceOperation
	 * @param interfaceName
	 * @throws RepositoryException
	 * @throws ServiceRegistrationException 
	 * @throws SAWSDLException 
	 */
	private void writeInterfaceOperationToTriplestore(
			Operation interfaceOperation, String interfaceName)
			throws RepositoryException, SAWSDLException, ServiceRegistrationException {
		String interfaceOperationName = interfaceOperation.getQName()
				.getLocalPart();
		String pattern = interfaceOperation.getPattern().toString();

		this.repowriter.writeInterfaceOperationsToTriples(interfaceName,
				interfaceOperationName, this.wsdlURLString);

		// Store the input
		Input input = interfaceOperation.getInput();
		this.writeOperationInputToTriplestore(input, interfaceName,
				interfaceOperationName, pattern);

		// Store the output
		Output output = interfaceOperation.getOutput();
		this.writeOperationOutputToTriplestore(output, interfaceName,
				interfaceOperationName, pattern);
	}
	
	
	
	private static boolean alreadyInTripleStore(String _serviceID)
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException, TupleQueryResultHandlerException,
			UnsupportedQueryResultFormatException, IOException {
		logger.debug("Starting alreadyInTripleStore()");
		String query = getServiceCount(_serviceID);
		RegistrationConfig cfg = new RegistrationConfig();
		RepositoryHandler repositoryHandler = new RepositoryHandler(
				cfg.getSesameEndpoint(), cfg.getSesameReposID(), false);
		TupleQueryResult queryResult = repositoryHandler.selectSPARQL(query);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QueryResultIO.write(queryResult, TupleQueryResultFormat.JSON, out);
		String beginNum = out.toString().substring(
				out.toString().indexOf("\"num\": {"));
		String beginValue = beginNum.substring(beginNum.indexOf("value")
				+ "value\": \"".length());
		int num = Integer.valueOf(beginValue.substring(0,
				beginValue.indexOf("\"")));
		logger.debug("Number of occurances (" + _serviceID + "): " + num);
		return (num > 0) ? true : false;
	}
	
	private static String getServiceCount(String _serviceID) {

		StringBuffer serviceCountQuery = new StringBuffer();

		serviceCountQuery
				.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		serviceCountQuery
				.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		serviceCountQuery
				.append("PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#> \n");
		serviceCountQuery
				.append("PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n");
		serviceCountQuery
				.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");

		serviceCountQuery.append("SELECT ?_serviceID (COUNT(?_serviceID) AS ?num)  WHERE {\n");
		serviceCountQuery
				.append("BIND(<"+_serviceID+"> AS ?_serviceID) . \n");
		serviceCountQuery
				.append("?_serviceID rdf:type msm_ext:Service . }\n");
		serviceCountQuery.append("GROUP BY ?_serviceID\n");

		return serviceCountQuery.toString();
	}
}
