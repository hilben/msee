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
import org.openrdf.repository.config.RepositoryConfig;
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
 * @author
 * 
 * @author Benjamin Hiltpolt
 * 
 */
public class TransformationWSDL {

	private static Logger logger = Logger.getLogger(TransformationWSDL.class);

	private static Map<QName, Element> elementsMap;

	/**
	 * 
	 * Transforms a WSDL file by adding its information into the triple store if
	 * valid
	 * 
	 * @param wsdlURL
	 *            the url of the wsdl file
	 * @return
	 * @throws ServiceRegistrationException
	 *             if there occur any parsing, repository, etc. error this
	 *             exception is thrown
	 */
	public static String transformWSDL(String wsdlURL) throws ServiceRegistrationException {
		try {

			RegistrationConfig cfg = new RegistrationConfig();
			RepositoryHandler reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID(), false);

			WSDLtoRepositoryWriter repowriter = null;

			// Read a SAWSDL description
			SAWSDLReader readerSAWSDL = SAWSDLFactory.newInstance().newSAWSDLReader();
			WSDL4ComplexWsdlReader reader = WSDL4ComplexWsdlFactory.newInstance().newWSDLReader();

			URL wsdlURI = new URL(wsdlURL);
			org.ow2.easywsdl.extensions.sawsdl.api.Description descSAWSDL = readerSAWSDL.read(wsdlURI);
			Description desc = reader.read(wsdlURI);

			desc.addImportedDocumentsInWsdl();

			// Get all elements of the xml
			elementsMap = new HashMap<QName, Element>();
			Types types = descSAWSDL.getTypes();
			List<Schema> schemas = types.getSchemas();
			for (org.ow2.easywsdl.extensions.sawsdl.api.schema.Schema schema : schemas) {
				List<Element> elements = schema.getElements();

				for (Element elem : elements) {
					Object obj = elem.getType();
					if (obj instanceof ComplexTypeImpl) {
						ComplexTypeImpl type = (ComplexTypeImpl) obj;
						List<Element> subElements = type.getSequence().getElements();

						for (Element subElem : subElements) {
							elementsMap.put(subElem.getQName(), subElem);
						}
					}
					elementsMap.put(elem.getQName(), elem);
				}
			}

			// Services
			for (Service service : desc.getServices()) {

				String SERVICE_NAME = service.getQName().getLocalPart();
				String serviceNamespace = service.getQName().getNamespaceURI() + "#";
				String namespaceURI = service.getQName().getNamespaceURI();
				
				if(alreadyInTripleStore(serviceNamespace + SERVICE_NAME)){
					throw new ServiceRegistrationException("Service already registered");
				}

				if (serviceNamespace == null || SERVICE_NAME == null) {
					return null;
				}

				repowriter = new WSDLtoRepositoryWriter(reposHandler, SERVICE_NAME, serviceNamespace, elementsMap);

				/*
				 * Get categories
				 */
				org.ow2.easywsdl.extensions.sawsdl.api.Service serviceSAWSDL = descSAWSDL.getService(service.getQName());
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

				repowriter.writeServiceToTriples(categories, namespaceURI, wsdlURL);

				// End-points
				for (Endpoint endpoint : service.getEndpoints()) {
					String endpointName = endpoint.getName();
					String endpointAddress = endpoint.getAddress();

					repowriter.writeEndpointsToTriples(endpointName,endpointAddress, wsdlURL);

					// Bindings
					Binding binding = endpoint.getBinding();
					String bindingName = binding.getQName().getLocalPart();
					String bindingType = binding.getTypeOfBinding().value().toString();

					repowriter.writeBindingsToTriples(bindingName, bindingType,	wsdlURL);

					for (BindingOperation bindingOperation : binding.getBindingOperations()) {
						String bindingOperationName = bindingOperation.getQName().getLocalPart();
						String soapAction = bindingOperation.getSoapAction();

						repowriter.writeBindingOperationsToTriples(bindingName, bindingOperationName, soapAction, wsdlURL);
					}

					// Interface
					InterfaceType wsdlInterface = binding.getInterface();
					String interfaceName = wsdlInterface.getQName().getLocalPart();

					repowriter.writeInterfaceToTriples(interfaceName, wsdlURL);

					for (Operation interfaceOperation : wsdlInterface.getOperations()) {
						String interfaceOperationName = interfaceOperation.getQName().getLocalPart();
						String pattern = interfaceOperation.getPattern().toString();

						repowriter.writeInterfaceOperationsToTriples(interfaceName, interfaceOperationName,wsdlURL);

						// Input
						Input input = interfaceOperation.getInput();
						QName inputMsgLabel = input.getMessageName();
						String inputMsgLabelName = null;
						if (inputMsgLabel != null) {
							inputMsgLabelName = inputMsgLabel.getLocalPart();
						}

						repowriter.writeInterfaceMessageReferenceToTriples(
								interfaceName, interfaceOperationName,
								inputMsgLabelName, pattern, wsdlURL);

						org.ow2.easywsdl.schema.api.Element inputElem = input
								.getElement();
						if (inputElem != null) {
							// Checks for lowering lifting schemas
							repowriter.checkAnnotations(inputElem.getQName());
							repowriter.writeElementDeclaration(
									inputElem.getQName(), interfaceName,
									interfaceOperationName, inputMsgLabelName,
									wsdlURL);

							Object intype = inputElem.getType();
							if (intype instanceof org.ow2.easywsdl.schema.impl.ComplexTypeImpl) {
								org.ow2.easywsdl.schema.impl.ComplexTypeImpl complexType = (org.ow2.easywsdl.schema.impl.ComplexTypeImpl) intype;
								List<org.ow2.easywsdl.schema.api.Element> elements = complexType
										.getSequence().getElements();
								for (org.ow2.easywsdl.schema.api.Element elem : elements) {
									repowriter.writeSubelementDeclaration(
											inputElem.getQName(),
											elem.getQName(), wsdlURL);
								}
							}
						}

						// Output
						Output output = interfaceOperation.getOutput();
						QName outputMsgLabel = output.getMessageName();
						String outputMsgLabelName = null;
						if (outputMsgLabel != null) {
							outputMsgLabelName = outputMsgLabel.getLocalPart();
						}

						repowriter.writeInterfaceMessageReferenceToTriples(
								interfaceName, interfaceOperationName,
								outputMsgLabelName, pattern, wsdlURL);

						org.ow2.easywsdl.schema.api.Element outputElem = output
								.getElement();
						if (outputElem != null) {
							repowriter.checkAnnotations(outputElem.getQName());
							repowriter.writeElementDeclaration(
									outputElem.getQName(), interfaceName,
									interfaceOperationName, outputMsgLabelName,
									wsdlURL);

							Object outtype = outputElem.getType();
							if (outtype instanceof org.ow2.easywsdl.schema.impl.ComplexTypeImpl) {
								org.ow2.easywsdl.schema.impl.ComplexTypeImpl complexType = (org.ow2.easywsdl.schema.impl.ComplexTypeImpl) outtype;
								List<org.ow2.easywsdl.schema.api.Element> elements = complexType
										.getSequence().getElements();
								for (org.ow2.easywsdl.schema.api.Element elem : elements) {
									repowriter.writeSubelementDeclaration(
											outputElem.getQName(),
											elem.getQName(), wsdlURL);
								}
							}
						}
					}
				}
			}

			reposHandler.commit();
			System.out.println(repowriter.getServiceID());
			return repowriter.getServiceID();

		} catch (RepositoryException e) {
			throw new ServiceRegistrationException(
					"Errors durring saving of triples into repository.",
					e.getCause());
		} catch (FileNotFoundException e) {
			throw new ServiceRegistrationException(
					"The configuration file was NOT found.", e.getCause());
		} catch (MalformedURLException e) {
			throw new ServiceRegistrationException(
					"The provided URL is malformed.", e.getCause());
		} catch (URISyntaxException e) {
			throw new ServiceRegistrationException(
					"The URI syntax is not well formed.", e.getCause());
		} catch (SAWSDLException e) {
			throw new ServiceRegistrationException(
					"Errors durring parsing the service.", e.getCause());
		} catch (WSDL4ComplexWsdlException e) {
			throw new ServiceRegistrationException(
					"Errors durring parsing the service.", e.getCause());
		} catch (IOException e) {
			throw new ServiceRegistrationException(
					"The repository endpoint ID or the WSDL file could NOT be found.",
					e.getCause());
		} catch (QueryEvaluationException e) {
			throw new ServiceRegistrationException(e);
		} catch (MalformedQueryException e) {
			throw new ServiceRegistrationException(e);
		} catch (TupleQueryResultHandlerException e) {
			throw new ServiceRegistrationException(e);
		} catch (UnsupportedQueryResultFormatException e) {
			throw new ServiceRegistrationException(e);
		}
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
