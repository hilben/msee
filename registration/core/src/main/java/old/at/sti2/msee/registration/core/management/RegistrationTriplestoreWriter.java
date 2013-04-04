/**
 * 
 */
package old.at.sti2.msee.registration.core.management;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import old.at.sti2.msee.registration.core.common.RegistrationConfig;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLException;
import org.ow2.easywsdl.extensions.sawsdl.api.schema.Element;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.util.triplestore.QueryHelper;
import at.sti2.util.triplestore.RepositoryHandler;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 *         This class is an interface to write wsdl file information to a
 *         repository. Used by the {@link RegistrationWSDLToTriplestoreWriter}
 */
public class RegistrationTriplestoreWriter {

	private static Logger logger = Logger
			.getLogger(RegistrationTriplestoreWriter.class);

	private RepositoryHandler reposHandler;

	private String serviceName;
	private String serviceNamespace;

	private Map<QName, Element> elementsMap;

	/**
	 * 
	 * Creates the WSDLtoRepositoryWriter
	 * 
	 * @param reposHandler
	 *            the RepositoryHandler used by the writer
	 * @param serviceName
	 *            the name of the service
	 * @param serviceNamespace
	 *            the name of the namespace of the service
	 * @param elementsMap
	 *            the parsed elements contained of the wsdl file
	 * @throws IOException 
	 */
	public RegistrationTriplestoreWriter(String serviceName, String serviceNamespace,
			Map<QName, Element> elementsMap) throws IOException {
		RegistrationConfig cfg = new RegistrationConfig();
		this.reposHandler = new RepositoryHandler(
				cfg.getSesameEndpoint(), cfg.getSesameReposID(), false);

		this.serviceName = serviceName;
		this.serviceNamespace = serviceNamespace;

		this.elementsMap = elementsMap;
	}

	/**
	 * @param elementQName
	 * @param subelementQName
	 * @param _wsdlURI
	 * @throws RepositoryException
	 * @throws SAWSDLException
	 */
	public void writeSubelementDeclaration(QName elementQName,
			QName subelementQName, String _wsdlURI) throws RepositoryException,
			SAWSDLException {
		logger.info("SUBELEMENT DECL INFO : Triple + Context: "
				+ getElementDeclarationNode(subelementQName.getLocalPart())
				+ " , " + QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("QName"));
		logger.info("SUBELEMENT DECL INFO : Triple + Context: "
				+ getElementDeclarationNode(subelementQName.getLocalPart())
				+ " , " + QueryHelper.getWSDLURI("localName") + " , "
				+ subelementQName.getLocalPart());
		logger.info("SUBELEMENT DECL INFO : Triple + Context: "
				+ getElementDeclarationNode(subelementQName.getLocalPart())
				+ " , " + QueryHelper.getWSDLURI("namespace") + " , "
				+ subelementQName.getNamespaceURI());

		logger.info("SUBELEMENT DECL FOR  ELEMENT DECL INFO: Triple + Context: "
				+ getElementDeclarationNode(elementQName.getLocalPart())
				+ " , "
				+ QueryHelper.getWSDLURI("elementDeclaration")
				+ " , "
				+ getElementDeclarationNode(subelementQName.getLocalPart()));

		List<URI> modelReferences = elementsMap.get(subelementQName)
				.getModelReference();
		for (URI modelReferece : modelReferences) {
			logger.info("SUBELEMENT DECL INFO : Triple + Context: "
					+ getElementDeclarationNode(subelementQName.getLocalPart())
					+ " , " + QueryHelper.getSAWSDLURI("modelReference")
					+ " , " + modelReferece);

			// Writing persistent
			reposHandler.addResourceTriple(
					getElementDeclarationNode(subelementQName.getLocalPart()),
					QueryHelper.getSAWSDLURI("modelReference"),
					modelReferece.toString(), _wsdlURI);
		}

		// Writing persistent
		reposHandler.addResourceTriple(
				getElementDeclarationNode(subelementQName.getLocalPart()),
				QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("QName"),
				_wsdlURI);
		reposHandler.addLiteralTriple(
				getElementDeclarationNode(subelementQName.getLocalPart()),
				QueryHelper.getWSDLURI("localName"),
				subelementQName.getLocalPart(), _wsdlURI);
		reposHandler.addResourceTriple(
				getElementDeclarationNode(subelementQName.getLocalPart()),
				QueryHelper.getWSDLURI("namespace"),
				subelementQName.getNamespaceURI(), _wsdlURI);

		reposHandler.addResourceTriple(
				getElementDeclarationNode(elementQName.getLocalPart()),
				QueryHelper.getWSDLURI("elementDeclaration"),
				getElementDeclarationNode(subelementQName.getLocalPart()),
				_wsdlURI);
	}

	/**
	 * @param elementQName
	 * @param interfaceName
	 * @param operationName
	 * @param messageLabel
	 * @param _wsdlURI
	 * @throws RepositoryException
	 * @throws SAWSDLException
	 * @throws RegistrationException
	 */
	public void writeElementDeclaration(QName elementQName,
			String interfaceName, String operationName, String messageLabel,
			String _wsdlURI) throws RepositoryException, SAWSDLException,
			ServiceRegistrationException {
		logger.info("ELEMENT DECL INFO : Triple + Context: "
				+ getElementDeclarationNode(elementQName.getLocalPart())
				+ " , " + QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("QName"));
		logger.info("ELEMENT DECL INFO : Triple + Context: "
				+ getElementDeclarationNode(elementQName.getLocalPart())
				+ " , " + QueryHelper.getWSDLURI("localName") + " , "
				+ elementQName.getLocalPart());
		logger.info("ELEMENT DECL INFO : Triple + Context: "
				+ getElementDeclarationNode(elementQName.getLocalPart())
				+ " , " + QueryHelper.getWSDLURI("namespace") + " , "
				+ elementQName.getNamespaceURI());

		logger.info("ELEMENT DECL FOR INPUT INTERFACE MSG REF INFO : Triple + Context: "
				+ getInterfaceMessageReferenceNode(interfaceName,
						operationName, messageLabel)
				+ " , "
				+ QueryHelper.getWSDLURI("elementDeclaration")
				+ " , "
				+ getElementDeclarationNode(elementQName.getLocalPart()));

		List<URI> loweringURIs = elementsMap.get(elementQName)
				.getLoweringSchemaMapping();
		for (URI loweringURI : loweringURIs) {
			logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: "
					+ getInterfaceMessageReferenceNode(interfaceName,
							operationName, messageLabel) + " , "
					+ QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , "
					+ loweringURI);

			// Writing persistent
			reposHandler.addResourceTriple(
					getInterfaceMessageReferenceNode(interfaceName,
							operationName, messageLabel), QueryHelper
							.getSAWSDLURI("loweringSchemaMapping"), loweringURI
							.toString(), _wsdlURI);
		}

		List<URI> liftingURIs = elementsMap.get(elementQName)
				.getLiftingSchemaMapping();
		for (URI liftingURI : liftingURIs) {
			logger.info("OUTPUT INTERFACE MSG REF INFO : Triple + Context: "
					+ getInterfaceMessageReferenceNode(interfaceName,
							operationName, messageLabel) + " , "
					+ QueryHelper.getSAWSDLURI("liftingSchemaMapping") + " , "
					+ liftingURI);

			// Writing persistent
			reposHandler.addResourceTriple(
					getInterfaceMessageReferenceNode(interfaceName,
							operationName, messageLabel), QueryHelper
							.getSAWSDLURI("liftingSchemaMapping"), liftingURI
							.toString(), _wsdlURI);
		}

		List<URI> modelReferences = elementsMap.get(elementQName)
				.getModelReference();
		for (URI modelReferece : modelReferences) {
			logger.info("ELEMENT DECL INFO : Triple + Context: "
					+ getElementDeclarationNode(elementQName.getLocalPart())
					+ " , " + QueryHelper.getSAWSDLURI("modelReference")
					+ " , " + modelReferece);

			// Writing persistent
			reposHandler.addResourceTriple(
					getElementDeclarationNode(elementQName.getLocalPart()),
					QueryHelper.getSAWSDLURI("modelReference"),
					modelReferece.toString(), _wsdlURI);
		}

		// Writing persistent
		reposHandler.addResourceTriple(
				getElementDeclarationNode(elementQName.getLocalPart()),
				QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("QName"),
				_wsdlURI);
		reposHandler.addLiteralTriple(
				getElementDeclarationNode(elementQName.getLocalPart()),
				QueryHelper.getWSDLURI("localName"),
				elementQName.getLocalPart(), _wsdlURI);
		reposHandler.addResourceTriple(
				getElementDeclarationNode(elementQName.getLocalPart()),
				QueryHelper.getWSDLURI("namespace"),
				elementQName.getNamespaceURI(), _wsdlURI);

		reposHandler.addResourceTriple(
				getInterfaceMessageReferenceNode(interfaceName, operationName,
						messageLabel), QueryHelper
						.getWSDLURI("elementDeclaration"),
				getElementDeclarationNode(elementQName.getLocalPart()),
				_wsdlURI);
	}

	/**
	 * @param serviceCategories
	 * @param namespaceURI
	 * @param _wsdlURI
	 * @throws RepositoryException
	 */
	public void writeServiceToTriples(List<URI> serviceCategories,
			String namespaceURI, String _wsdlURI) throws RepositoryException {
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()
				+ " , " + QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getMSMEXTURI("Service"));
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()
				+ " , " + QueryHelper.getRDFSURI("isDefinedBy") + " , "
				+ _wsdlURI);
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()
				+ " , " + QueryHelper.getMSMEXTURI("wsdlDescription") + " , "
				+ getDescriptionNode());
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()
				+ " , " + QueryHelper.getRDFSURI("label") + " , " + serviceName);
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()
				+ " , " + QueryHelper.getDCURI("creator") + " , "
				+ "STI Innsbruck");

		for (URI category : serviceCategories) {
			logger.info("SERVICE INFO : Triple + Context: " + getServiceID()
					+ " , " + QueryHelper.getSAWSDLURI("modelReference")
					+ " , " + category);

			// Writing persistent
			reposHandler.addResourceTriple(getServiceID(),
					QueryHelper.getSAWSDLURI("modelReference"),
					category.toString(), _wsdlURI);
		}

		logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode()
				+ " , " + QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getMSMEXTURI("Service"));
		logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode()
				+ " , " + QueryHelper.getRDFSURI("label") + " , " + serviceName);

		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: "
				+ getDescriptionNode() + " , " + QueryHelper.getRDFURI("type")
				+ " , " + QueryHelper.getWSDLURI("Description"));
		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: "
				+ getDescriptionNode() + " , "
				+ QueryHelper.getWSDLURI("service") + " , " + getServiceNode());
		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: "
				+ getDescriptionNode() + " , "
				+ QueryHelper.getWSDLURI("namespace") + " , " + namespaceURI);

		// Writing persistent
		reposHandler.addResourceTriple(getServiceID(),
				QueryHelper.getRDFURI("type"),
				QueryHelper.getMSMEXTURI("Service"), _wsdlURI);
		reposHandler.addResourceTriple(getServiceID(),
				QueryHelper.getRDFSURI("isDefinedBy"), _wsdlURI, _wsdlURI);
		reposHandler.addResourceTriple(getServiceID(),
				QueryHelper.getMSMEXTURI("wsdlDescription"),
				getDescriptionNode(), _wsdlURI);
		reposHandler.addLiteralTriple(getServiceID(),
				QueryHelper.getRDFSURI("label"), serviceName, _wsdlURI);
		reposHandler.addLiteralTriple(getServiceID(),
				QueryHelper.getDCURI("creator"), "STI Innsbruck", _wsdlURI);

		reposHandler.addResourceTriple(getServiceNode(),
				QueryHelper.getRDFURI("type"),
				QueryHelper.getMSMEXTURI("Service"), _wsdlURI);
		reposHandler.addLiteralTriple(getServiceNode(),
				QueryHelper.getRDFSURI("label"), serviceName, _wsdlURI);

		reposHandler.addResourceTriple(getDescriptionNode(),
				QueryHelper.getRDFURI("type"),
				QueryHelper.getWSDLURI("Description"), _wsdlURI);
		reposHandler.addResourceTriple(getDescriptionNode(),
				QueryHelper.getWSDLURI("service"), getServiceNode(), _wsdlURI);
		reposHandler.addResourceTriple(getDescriptionNode(),
				QueryHelper.getWSDLURI("namespace"), namespaceURI, _wsdlURI);
	}

	/**
	 * @param endpointName
	 * @param endpointAddress
	 * @param _wsdlURI
	 * @throws RepositoryException
	 */
	public void writeEndpointsToTriples(String endpointName,
			String endpointAddress, String _wsdlURI) throws RepositoryException {
		logger.info("ENDPOINTS : Triple + Context: "
				+ getEndpointNode(endpointName) + " , "
				+ QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("Endpoint"));
		logger.info("ENDPOINTS : Triple + Context: "
				+ getEndpointNode(endpointName) + " , "
				+ QueryHelper.getRDFSURI("label") + " , " + endpointName);
		logger.info("ENDPOINTS : Triple + Context: "
				+ getEndpointNode(endpointName) + " , "
				+ QueryHelper.getWSDLURI("address") + " , " + endpointAddress);
		logger.info("ENDPOINTS : Triple + Context: "
				+ getEndpointNode(endpointName) + " , "
				+ QueryHelper.getWSDLURI("useBinding") + " , "
				+ getBindingNode(endpointName));

		logger.info("SERVICE ENDPOINTS : Triple + Context: " + getServiceNode()
				+ " , " + QueryHelper.getWSDLURI("endpoint") + " , "
				+ getEndpointNode(endpointName));

		logger.info("DESCRIPTION ENDPOINTS : Triple + Context: "
				+ getDescriptionNode() + " , "
				+ QueryHelper.getWSDLURI("endpoint") + " , "
				+ getEndpointNode(endpointName));

		// Writing persistent
		reposHandler.addResourceTriple(getEndpointNode(endpointName),
				QueryHelper.getRDFURI("type"),
				QueryHelper.getWSDLURI("Endpoint"), _wsdlURI);
		reposHandler.addLiteralTriple(getEndpointNode(endpointName),
				QueryHelper.getRDFSURI("label"), endpointName, _wsdlURI);
		reposHandler.addResourceTriple(getEndpointNode(endpointName),
				QueryHelper.getWSDLURI("address"), endpointAddress, _wsdlURI);
		reposHandler.addResourceTriple(getEndpointNode(endpointName),
				QueryHelper.getWSDLURI("useBinding"),
				getEndpointNode(endpointName), _wsdlURI);

		reposHandler.addResourceTriple(getServiceNode(),
				QueryHelper.getWSDLURI("endpoint"),
				getEndpointNode(endpointName), _wsdlURI);

		reposHandler.addResourceTriple(getDescriptionNode(),
				QueryHelper.getWSDLURI("endpoint"),
				getEndpointNode(endpointName), _wsdlURI);
	}

	/**
	 * @param interfaceName
	 * @param operationName
	 * @param messageLabel
	 * @param pattern
	 * @param _wsdlURI
	 * @throws RepositoryException
	 */
	public void writeInterfaceMessageReferenceToTriples(String interfaceName,
			String operationName, String messageLabel, String pattern,
			String _wsdlURI) throws RepositoryException {
		logger.info("INTERFACE MSG REF INFO : Triple + Context: "
				+ getInterfaceMessageReferenceNode(interfaceName,
						operationName, messageLabel) + " , "
				+ QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("InputMessage"));
		logger.info("INTERFACE MSG REF INFO : Triple + Context: "
				+ getInterfaceMessageReferenceNode(interfaceName,
						operationName, messageLabel) + " , "
				+ QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("InterfaceMessageReference"));
		logger.info("INTERFACE MSG REF INFO : Triple + Context: "
				+ getInterfaceMessageReferenceNode(interfaceName,
						operationName, messageLabel) + " , "
				+ QueryHelper.getWSDLURI("messageLabel") + " , " + pattern
				+ "#" + messageLabel);

		logger.info("INTERFACE MSG REF INFO FOR INPUT : Triple + Context: "
				+ getInterfaceOperationNode(interfaceName, operationName)
				+ " , "
				+ QueryHelper.getWSDLURI("interfaceMessageReference")
				+ " , "
				+ getInterfaceMessageReferenceNode(interfaceName,
						operationName, messageLabel));

		// Writing persistent
		reposHandler.addResourceTriple(
				getInterfaceMessageReferenceNode(interfaceName, operationName,
						messageLabel), QueryHelper.getRDFURI("type"),
				QueryHelper.getWSDLURI("InputMessage"), _wsdlURI);
		reposHandler.addResourceTriple(
				getInterfaceMessageReferenceNode(interfaceName, operationName,
						messageLabel), QueryHelper.getRDFURI("type"),
				QueryHelper.getWSDLURI("InterfaceMessageReference"), _wsdlURI);
		reposHandler.addResourceTriple(
				getInterfaceMessageReferenceNode(interfaceName, operationName,
						messageLabel), QueryHelper.getWSDLURI("messageLabel"),
				pattern + "#" + messageLabel, _wsdlURI);

		reposHandler.addResourceTriple(
				getInterfaceOperationNode(interfaceName, operationName),
				QueryHelper.getWSDLURI("interfaceMessageReference"),
				getInterfaceMessageReferenceNode(interfaceName, operationName,
						messageLabel), _wsdlURI);
	}

	/**
	 * @param bindingName
	 * @param bindingType
	 * @param _wsdlURI
	 * @throws RepositoryException
	 * @throws URISyntaxException
	 */
	public void writeBindingsToTriples(String bindingName, String bindingType,
			String _wsdlURI) throws RepositoryException, URISyntaxException {
		logger.info("BINDINGS : Triple + Context: "
				+ getBindingNode(bindingName) + " , "
				+ QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("Binding"));
		logger.info("BINDINGS : Triple + Context: "
				+ getBindingNode(bindingName) + " , "
				+ QueryHelper.getRDFURI("type") + " , " + bindingType);
		logger.info("BINDINGS : Triple + Context: "
				+ getBindingNode(bindingName) + " , "
				+ QueryHelper.getRDFSURI("label") + " , " + bindingName);
		logger.info("BINDINGS : Triple + Context: "
				+ getBindingNode(bindingName) + " , "
				+ QueryHelper.getWSDLURI("binds") + " , "
				+ getInterfaceNode(bindingName));
		logger.info("BINDINGS : Triple + Context: "
				+ getBindingNode(bindingName) + " , "
				+ QueryHelper.getWSDLURI("bindingFault") + " , " + "");

		logger.info("BINDINGS FOR DESCRIPTION: Triple + Context: "
				+ getDescriptionNode() + " , "
				+ QueryHelper.getWSDLURI("binding") + " , "
				+ getBindingNode(bindingName));

		// Writing persistent
		reposHandler.addResourceTriple(getBindingNode(bindingName),
				QueryHelper.getRDFURI("type"),
				QueryHelper.getWSDLURI("Binding"), _wsdlURI);
		// reposHandler.addResourceTriple(getBindingNode(bindingName),
		// QueryHelper.getRDFURI("type"), bindingType, _wsdlURI);
		reposHandler.addLiteralTriple(getBindingNode(bindingName),
				QueryHelper.getRDFSURI("label"), bindingName, _wsdlURI);
		reposHandler.addResourceTriple(getBindingNode(bindingName),
				QueryHelper.getWSDLURI("binds"), getInterfaceNode(bindingName),
				_wsdlURI);
		reposHandler.addResourceTriple(getBindingNode(bindingName),
				QueryHelper.getWSDLURI("bindingFault"), serviceNamespace,
				_wsdlURI);

		reposHandler.addResourceTriple(getDescriptionNode(),
				QueryHelper.getWSDLURI("binding"), getBindingNode(bindingName),
				_wsdlURI);
	}

	/**
	 * @param bindingName
	 * @param bindingOperationName
	 * @param soapActionURI
	 * @param _wsdlURI
	 * @throws RepositoryException
	 */
	public void writeBindingOperationsToTriples(String bindingName,
			String bindingOperationName, String soapActionURI, String _wsdlURI)
			throws RepositoryException {
		logger.info("BINDINGS OPERATION: Triple + Context: "
				+ getBindingOperationNode(bindingOperationName) + " , "
				+ QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("BindingOperation"));

		logger.info("BINDINGS OPERATION FOR BINDING PARENT: Triple + Context: "
				+ getBindingNode(bindingOperationName) + " , "
				+ QueryHelper.getWSDLURI("bindingOperation") + " , "
				+ getBindingOperationNode(bindingOperationName));

		// Writing persistent
		reposHandler.addResourceTriple(
				getBindingOperationNode(bindingOperationName),
				QueryHelper.getRDFURI("type"),
				QueryHelper.getWSDLURI("BindingOperation"), _wsdlURI);

		/**
		 * XXX: Fix this. SOAPAction could be also only a string or empty
		 */
		if (soapActionURI != null) {
			logger.info("BINDINGS OPERATION: Triple + Context: "
					+ getBindingOperationNode(bindingOperationName) + " , "
					+ QueryHelper.getWSOAPURI("action") + " , " + soapActionURI);

			// Writing persistent
			reposHandler.addResourceTriple(
					getBindingOperationNode(bindingOperationName),
					QueryHelper.getWSOAPURI("action"), soapActionURI, _wsdlURI);
		}

		reposHandler.addResourceTriple(getBindingNode(bindingName),
				QueryHelper.getWSDLURI("bindingOperation"),
				getBindingOperationNode(bindingOperationName), _wsdlURI);
	}

	/**
	 * @param interfaceName
	 * @param _wsdlURI
	 * @throws RepositoryException
	 */
	public void writeInterfaceToTriples(String interfaceName, String _wsdlURI)
			throws RepositoryException {
		logger.info("INTERFACE : Triple + Context: "
				+ getInterfaceNode(interfaceName) + " , "
				+ QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("Interface"));
		logger.info("INTERFACE : Triple + Context: "
				+ getInterfaceNode(interfaceName) + " , "
				+ QueryHelper.getRDFSURI("label") + " , " + interfaceName);

		logger.info("INTERFACE FOR DESCRIPTION : Triple + Context: "
				+ getDescriptionNode() + " , "
				+ QueryHelper.getWSDLURI("interface") + " , "
				+ getInterfaceNode(interfaceName));

		// Writing persistent
		reposHandler.addResourceTriple(getInterfaceNode(interfaceName),
				QueryHelper.getRDFURI("type"),
				QueryHelper.getWSDLURI("Interface"), _wsdlURI);
		reposHandler.addLiteralTriple(getInterfaceNode(interfaceName),
				QueryHelper.getRDFSURI("label"), interfaceName, _wsdlURI);

		reposHandler.addResourceTriple(getDescriptionNode(),
				QueryHelper.getWSDLURI("interface"),
				getInterfaceNode(interfaceName), _wsdlURI);
	}

	/**
	 * @param interfaceName
	 * @param interfaceOperationName
	 * @param _wsdlURI
	 * @throws RepositoryException
	 */
	public void writeInterfaceOperationsToTriples(String interfaceName,
			String interfaceOperationName, String _wsdlURI)
			throws RepositoryException {
		logger.info("INTERFACE OPERATION: Triple + Context: "
				+ getInterfaceOperationNode(interfaceName,
						interfaceOperationName) + " , "
				+ QueryHelper.getRDFURI("type") + " , "
				+ QueryHelper.getWSDLURI("InterfaceOperation"));
		logger.info("INTERFACE OPERATION: Triple + Context: "
				+ getInterfaceOperationNode(interfaceName,
						interfaceOperationName) + " , "
				+ QueryHelper.getRDFSURI("label") + " , "
				+ interfaceOperationName);

		logger.info("INTERFACE OPERATION FOR INTERFACE: Triple + Context: "
				+ getInterfaceNode(interfaceName)
				+ " , "
				+ QueryHelper.getWSDLURI("interfaceOperation")
				+ " , "
				+ getInterfaceOperationNode(interfaceName,
						interfaceOperationName));

		// Writing persistent
		reposHandler
				.addResourceTriple(
						getInterfaceOperationNode(interfaceName,
								interfaceOperationName), QueryHelper
								.getRDFURI("type"), QueryHelper
								.getWSDLURI("InterfaceOperation"), _wsdlURI);
		reposHandler
				.addLiteralTriple(
						getInterfaceOperationNode(interfaceName,
								interfaceOperationName), QueryHelper
								.getRDFSURI("label"), interfaceOperationName,
						_wsdlURI);

		reposHandler
				.addResourceTriple(
						getInterfaceNode(interfaceName),
						QueryHelper.getWSDLURI("interfaceOperation"),
						getInterfaceOperationNode(interfaceName,
								interfaceOperationName), _wsdlURI);
	}

	/**
	 * @param elementName
	 * @return
	 */
	public String getElementDeclarationNode(String elementName) {
		return serviceNamespace + "wsdl.elementDeclaration(" + elementName
				+ ")";
	}

	/**
	 * @param interfaceName
	 * @param operationName
	 * @param messageLabel
	 * @return
	 */
	public String getInterfaceMessageReferenceNode(String interfaceName,
			String operationName, String messageLabel) {
		return serviceNamespace + "wsdl.interfaceMessageReference("
				+ interfaceName + "/" + operationName + "/" + messageLabel
				+ ")";
	}

	/**
	 * @param interfaceName
	 * @return
	 */
	public String getInterfaceNode(String interfaceName) {
		return serviceNamespace + "wsdl.interface(" + interfaceName + ")";
	}

	/**
	 * @param interfaceName
	 * @param interfaceOperationName
	 * @return
	 */
	public String getInterfaceOperationNode(String interfaceName,
			String interfaceOperationName) {
		return serviceNamespace + "wsdl.interfaceOperation(" + interfaceName
				+ "/" + interfaceOperationName + ")";
	}

	/**
	 * @param bindingName
	 * @return
	 */
	public String getBindingNode(String bindingName) {
		return serviceNamespace + "wsdl.binding(" + serviceName + "/"
				+ bindingName + ")";
	}

	/**
	 * @param bindingOperationName
	 * @return
	 */
	public String getBindingOperationNode(String bindingOperationName) {
		return serviceNamespace + "wsdl.bindingOperation(" + serviceName + "/"
				+ bindingOperationName + ")";
	}

	/**
	 * @param bindingName
	 * @return
	 */
	public String getEndpointNode(String bindingName) {
		return serviceNamespace + "wsdl.endpoint(" + serviceName + "/"
				+ bindingName + ")";
	}

	/**
	 * @return
	 */
	public String getServiceNode() {
		return serviceNamespace + "wsdl.service(" + serviceName + ")";
	}

	/**
	 * @return
	 */
	public String getServiceID() {
		return serviceNamespace + serviceName;
	}

	/**
	 * @return
	 */
	public String getDescriptionNode() {
		return serviceNamespace + "wsdl.description()";
	}

	/**
	 * Checking if the elements have elements annotated with
	 * "loweringSchemaMapping" or "liftingSchemaMapping"
	 * 
	 * @param elementQName
	 * @throws RegistrationException
	 * @throws SAWSDLException
	 */
	public void checkAnnotations(QName elementQName)
			throws ServiceRegistrationException, SAWSDLException {
		List<URI> loweringURIs = elementsMap.get(elementQName)
				.getLoweringSchemaMapping();
		List<URI> liftingURIs = elementsMap.get(elementQName)
				.getLiftingSchemaMapping();

		if (loweringURIs.size() == 0 && liftingURIs.size() == 0) {
			throw new ServiceRegistrationException(
					"The service MUST contains at least one element anotated with liftingSchemaMapping. "
							+ "For documentation see: http://www.sesa.sti2.at/doc/service_annotation");
		}
	}
	
	/**
	 * Finally commits all the triples to the store
	 * @throws RepositoryException 
	 */
	public void commit() throws RepositoryException {
		this.reposHandler.commit();
	}
}
