package at.sti2.ngsee.registration.core.management;

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

import at.sti2.ngsee.registration.api.exception.RegistrationException;
import at.sti2.ngsee.registration.core.common.Config;
import at.sti2.util.triplestore.RepositoryHandler;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class TransformationWSDL {

	private static Logger logger = Logger.getLogger(TransformationWSDL.class);

	private static Map<QName, Element> elementsMap;

	/**
	 * @deprecated
	 * @param args
	 * @throws RegistrationException
	 * @throws RepositoryException
	 */
	public static void main(String[] args) throws RegistrationException,
			RepositoryException {
		transformWSDL("file:////C:/Users/benhil.STI/Desktop/00-all.wsdl");
	}

	public static String transformWSDL(String _wsdlURI)
			throws RegistrationException {
		try {
			Config cfg = new Config();
			RepositoryHandler reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(),
					cfg.getSesameReposID(),false);
			
			WSDLtoRepositoryWriter repowriter = null;

			// Read a SAWSDL description
			SAWSDLReader readerSAWSDL = SAWSDLFactory.newInstance()
					.newSAWSDLReader();
			WSDL4ComplexWsdlReader reader = WSDL4ComplexWsdlFactory
					.newInstance().newWSDLReader();

			URL wsdlURI = new URL(_wsdlURI);
			org.ow2.easywsdl.extensions.sawsdl.api.Description descSAWSDL = readerSAWSDL
					.read(wsdlURI);
			Description desc = reader.read(wsdlURI);

			desc.addImportedDocumentsInWsdl();

			elementsMap = new HashMap<QName, Element>();
			Types types = descSAWSDL.getTypes();
			List<Schema> schemas = types.getSchemas();
			for (org.ow2.easywsdl.extensions.sawsdl.api.schema.Schema schema : schemas) {
				List<Element> elements = schema.getElements();

				for (Element elem : elements) {
					Object obj = elem.getType();
					if (obj instanceof ComplexTypeImpl) {
						ComplexTypeImpl type = (ComplexTypeImpl) obj;
						List<Element> subElements = type.getSequence()
								.getElements();

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
				
				if (serviceNamespace == null || SERVICE_NAME == null) {
					return null;
				}
				
				repowriter = new WSDLtoRepositoryWriter(reposHandler, SERVICE_NAME, serviceNamespace, elementsMap);

				/*
				 * Get categories
				 */
				org.ow2.easywsdl.extensions.sawsdl.api.Service serviceSAWSDL = descSAWSDL
						.getService(service.getQName());
				final List<URI> categories = serviceSAWSDL.getModelReference();

				if (categories.size() == 0) {
					throw new RegistrationException(
							"The service MUST be at least annotated with one service category. "
									+ "For documentation see: http://www.sesa.sti2.at/doc/service_annotation");
				}

				repowriter.writeServiceToTriples(categories, namespaceURI, _wsdlURI);

				// End-points
				for (Endpoint endpoint : service.getEndpoints()) {
					String endpointName = endpoint.getName();
					String endpointAddress = endpoint.getAddress();

					repowriter.writeEndpointsToTriples(endpointName, endpointAddress,
							_wsdlURI);

					// Bindings
					Binding binding = endpoint.getBinding();
					String bindingName = binding.getQName().getLocalPart();
					String bindingType = binding.getTypeOfBinding().value()
							.toString();

					repowriter.writeBindingsToTriples(bindingName, bindingType, _wsdlURI);

					for (BindingOperation bindingOperation : binding
							.getBindingOperations()) {
						String bindingOperationName = bindingOperation
								.getQName().getLocalPart();
						String soapAction = bindingOperation.getSoapAction();

						repowriter.writeBindingOperationsToTriples(bindingName,
								bindingOperationName, soapAction, _wsdlURI);
					}

					// Interface
					InterfaceType wsdlInterface = binding.getInterface();
					String interfaceName = wsdlInterface.getQName()
							.getLocalPart();

					repowriter.writeInterfaceToTriples(interfaceName, _wsdlURI);

					for (Operation interfaceOperation : wsdlInterface
							.getOperations()) {
						String interfaceOperationName = interfaceOperation
								.getQName().getLocalPart();
						String pattern = interfaceOperation.getPattern()
								.toString();

						repowriter.writeInterfaceOperationsToTriples(interfaceName,
								interfaceOperationName, _wsdlURI);

						// Input
						Input input = interfaceOperation.getInput();
						QName inputMsgLabel = input.getMessageName();
						String inputMsgLabelName = null;
						if (inputMsgLabel != null)
							inputMsgLabelName = inputMsgLabel.getLocalPart();

						repowriter.writeInterfaceMessageReferenceToTriples(interfaceName,
								interfaceOperationName, inputMsgLabelName,
								pattern, _wsdlURI);

						org.ow2.easywsdl.schema.api.Element inputElem = input
								.getElement();
						if (inputElem != null) {
							repowriter.checkAnnotations(inputElem.getQName());
							repowriter.writeElementDeclaration(inputElem.getQName(),
									interfaceName, interfaceOperationName,
									inputMsgLabelName, _wsdlURI);

							Object intype = inputElem.getType();
							if (intype instanceof org.ow2.easywsdl.schema.impl.ComplexTypeImpl) {
								org.ow2.easywsdl.schema.impl.ComplexTypeImpl complexType = (org.ow2.easywsdl.schema.impl.ComplexTypeImpl) intype;
								List<org.ow2.easywsdl.schema.api.Element> elements = complexType
										.getSequence().getElements();
								for (org.ow2.easywsdl.schema.api.Element elem : elements) {
									repowriter.writeSubelementDeclaration(
											inputElem.getQName(),
											elem.getQName(), _wsdlURI);
								}
							}
						}

						// Output
						Output output = interfaceOperation.getOutput();
						QName outputMsgLabel = output.getMessageName();
						String outputMsgLabelName = null;
						if (outputMsgLabel != null)
							outputMsgLabelName = outputMsgLabel.getLocalPart();

						repowriter.writeInterfaceMessageReferenceToTriples(interfaceName,
								interfaceOperationName, outputMsgLabelName,
								pattern, _wsdlURI);

						org.ow2.easywsdl.schema.api.Element outputElem = output
								.getElement();
						if (outputElem != null) {
							repowriter.checkAnnotations(outputElem.getQName());
							repowriter.writeElementDeclaration(outputElem.getQName(),
									interfaceName, interfaceOperationName,
									outputMsgLabelName, _wsdlURI);

							Object outtype = outputElem.getType();
							if (outtype instanceof org.ow2.easywsdl.schema.impl.ComplexTypeImpl) {
								org.ow2.easywsdl.schema.impl.ComplexTypeImpl complexType = (org.ow2.easywsdl.schema.impl.ComplexTypeImpl) outtype;
								List<org.ow2.easywsdl.schema.api.Element> elements = complexType
										.getSequence().getElements();
								for (org.ow2.easywsdl.schema.api.Element elem : elements) {
									repowriter.writeSubelementDeclaration(
											outputElem.getQName(),
											elem.getQName(), _wsdlURI);
								}
							}
						}
					}
				}
			}

			// TODO: fix the RepositoryException thrown by reposHandler.commit()
			// which is NOT catched
				reposHandler.commit(); //TODO:
				return repowriter.getServiceID();

		} catch (RepositoryException e) {
			throw new RegistrationException(
					"Errors durring saving of triples into repository.",
					e.getCause());
		} catch (FileNotFoundException e) {
			throw new RegistrationException(
					"The configuration file was NOT found.", e.getCause());
		} catch (MalformedURLException e) {
			throw new RegistrationException("The provided URL is malformed.",
					e.getCause());
		} catch (URISyntaxException e) {
			throw new RegistrationException(
					"The URI syntax is not well formed.", e.getCause());
		} catch (SAWSDLException e) {
			throw new RegistrationException(
					"Errors durring parsing the service.", e.getCause());
		} catch (WSDL4ComplexWsdlException e) {
			throw new RegistrationException(
					"Errors durring parsing the service.", e.getCause());
		} catch (IOException e) {
			throw new RegistrationException(
					"The repository endpoint ID or the WSDL file could NOT be found.",
					e.getCause());
		}
	}



}
