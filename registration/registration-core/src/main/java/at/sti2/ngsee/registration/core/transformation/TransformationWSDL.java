package at.sti2.ngsee.registration.core.transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;
import org.ow2.easywsdl.wsdl.api.Binding;
import org.ow2.easywsdl.wsdl.api.BindingOperation;
import org.ow2.easywsdl.wsdl.api.Endpoint;
import org.ow2.easywsdl.wsdl.api.Input;
import org.ow2.easywsdl.wsdl.api.InterfaceType;
import org.ow2.easywsdl.wsdl.api.Operation;
import org.ow2.easywsdl.wsdl.api.Output;
import org.ow2.easywsdl.wsdl.api.Service;
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

import at.sti2.ngsee.registration.api.exception.RegistrationException;
import at.sti2.ngsee.registration.core.common.Config;
import at.sti2.util.triplestore.QueryHelper;
import at.sti2.util.triplestore.RepositoryHandler;

public class TransformationWSDL {
	
	private static Logger logger = Logger.getLogger(TransformationWSDL.class);
	private static String SERVICE_NS;
	private static String SERVICE_NAME;
	private static String NAMESPACE_URI;
	private static RepositoryHandler reposHandler;
	private static HashMap<QName, Element> elementsMap;
	
	public static void main(String[] args) throws RegistrationException   {
//		transformWSDL("file:///home/koni/globalweather.sawsdl");
//		transformWSDL("file:///home/koni/sawsdl_2.0.wsdl");
		transformWSDL("file:///home/koni/development/sti/wsdl-2.0-testcase/00-all.wsdl");		
//		transformWSDL("http://www.w3.org/2002/ws/sawsdl/CR/wsdl2.0/00-all.wsd");
	}
	
	public static String transformWSDL(String _wsdlURI) throws RegistrationException {
		try {
			try {
				Config cfg = new Config();		
				reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
			}catch (Exception e) {
				throw new RegistrationException("The repository endpoint od ID cound NOT be found.", e.getCause());
			}
		
			// Read a SAWSDL description
			SAWSDLReader readerSAWSDL = SAWSDLFactory.newInstance().newSAWSDLReader();
			WSDL4ComplexWsdlReader reader = WSDL4ComplexWsdlFactory.newInstance().newWSDLReader();
			
			org.ow2.easywsdl.extensions.sawsdl.api.Description descSAWSDL;
			Description desc;
			try{
				URL wsdlURI = new URL(_wsdlURI);
				descSAWSDL = readerSAWSDL.read(wsdlURI);
				
				desc = reader.read(wsdlURI);				
			}catch(MalformedURLException e){
				throw new RegistrationException("The provided URL is malformed.", e.getCause());
			} catch (IOException e) {
				throw new RegistrationException("The provided WSDL could NOT be found.", e.getCause());			
			}					
			
			desc.addImportedDocumentsInWsdl();
			
			elementsMap = new HashMap<QName, Element>();
			Types types = descSAWSDL.getTypes();
			List<Schema> schemas = types.getSchemas();
			for ( org.ow2.easywsdl.extensions.sawsdl.api.schema.Schema schema : schemas ) {
				List<Element> elements = schema.getElements();
				
				for (Element elem : elements ) {
					Object obj = elem.getType();
					if ( obj instanceof ComplexTypeImpl ) {
						ComplexTypeImpl type = (ComplexTypeImpl) obj;
						List<Element> subElements = type.getSequence().getElements();
						
						for ( Element subElem : subElements ) {
							elementsMap.put(subElem.getQName(), subElem);
						}
					}
					elementsMap.put(elem.getQName(), elem);
				}
			}
			
			//Services
			for( Service service : desc.getServices() ) {
				
				SERVICE_NAME = service.getQName().getLocalPart();
				SERVICE_NS = service.getQName().getNamespaceURI() + "#";
				NAMESPACE_URI = service.getQName().getNamespaceURI();
				
				org.ow2.easywsdl.extensions.sawsdl.api.Service serviceSAWSDL = descSAWSDL.getService(service.getQName());
				List<URI> categories = serviceSAWSDL.getModelReference();
				
				writeServiceToTriples(categories, NAMESPACE_URI, _wsdlURI);
							
				//End-points
				for(Endpoint endpoint : service.getEndpoints()) {
					String endpointName = endpoint.getName();
					String endpointAddress = endpoint.getAddress();
					
					writeEndpointsToTriples(endpointName, endpointAddress, _wsdlURI);
									
					//Bindings
					Binding binding = endpoint.getBinding();
					String bindingName = binding.getQName().getLocalPart();
					String bindingType = binding.getTypeOfBinding().value().toString();
					
					writeBindingsToTriples(bindingName, bindingType, _wsdlURI);
															
					for ( BindingOperation bindingOperation : binding.getBindingOperations() ) {
						String bindingOperationName = bindingOperation.getQName().getLocalPart(); 
						String soapAction = bindingOperation.getSoapAction();
						
						writeBindingOperationsToTriples(bindingName, bindingOperationName, soapAction, _wsdlURI);
					}
					
					//Interface
					InterfaceType wsdlInterface = binding.getInterface();
					String interfaceName = wsdlInterface.getQName().getLocalPart();
					
					writeInterfaceToTriples(interfaceName, _wsdlURI);
					
					for( Operation interfaceOperation : wsdlInterface.getOperations() ) {
						String interfaceOperationName = interfaceOperation.getQName().getLocalPart();
						String pattern = interfaceOperation.getPattern().toString();
						
						writeInterfaceOperationsToTriples(interfaceName, interfaceOperationName, _wsdlURI);
						
						
						//Input
						Input input = interfaceOperation.getInput();
						QName inputMsgLabel = input.getMessageName();
						String inputMsgLabelName = null;
						if ( inputMsgLabel != null )
							inputMsgLabelName = inputMsgLabel.getLocalPart();
						
						writeInterfaceMessageReferenceToTriples(interfaceName, interfaceOperationName, inputMsgLabelName, pattern, _wsdlURI);
						
						org.ow2.easywsdl.schema.api.Element inputElem = input.getElement();
						if ( inputElem != null ) {
							writeElementDeclaration(inputElem.getQName(), interfaceName, interfaceOperationName,inputMsgLabelName, _wsdlURI);
							
							Object intype = inputElem.getType();
							if ( intype instanceof org.ow2.easywsdl.schema.impl.ComplexTypeImpl ) {
								org.ow2.easywsdl.schema.impl.ComplexTypeImpl complexType = (org.ow2.easywsdl.schema.impl.ComplexTypeImpl) intype;
								List<org.ow2.easywsdl.schema.api.Element> elements = complexType.getSequence().getElements();
								for ( org.ow2.easywsdl.schema.api.Element elem : elements ) {
									writeSubelementDeclaration(inputElem.getQName(), elem.getQName(), _wsdlURI);
								}
							}
						}
						
						//Output
						Output output = interfaceOperation.getOutput();
						QName outputMsgLabel = output.getMessageName();
						String outputMsgLabelName = null;
						if ( outputMsgLabel != null )
							outputMsgLabelName = outputMsgLabel.getLocalPart();
						
						writeInterfaceMessageReferenceToTriples(interfaceName, interfaceOperationName, outputMsgLabelName, pattern, _wsdlURI);
						
						org.ow2.easywsdl.schema.api.Element outputElem = output.getElement();
						if ( outputElem != null ) {
							writeElementDeclaration(outputElem.getQName(), interfaceName, interfaceOperationName,outputMsgLabelName, _wsdlURI);
							
							Object outtype = outputElem.getType();
							if ( outtype instanceof org.ow2.easywsdl.schema.impl.ComplexTypeImpl ) {
								org.ow2.easywsdl.schema.impl.ComplexTypeImpl complexType = (org.ow2.easywsdl.schema.impl.ComplexTypeImpl) outtype;
								List<org.ow2.easywsdl.schema.api.Element> elements = complexType.getSequence().getElements();
								for ( org.ow2.easywsdl.schema.api.Element elem : elements ) {
									writeSubelementDeclaration(outputElem.getQName(), elem.getQName(), _wsdlURI);
								}
							}
						}
					}
				}
			}			
		} catch (URISyntaxException e) {
			throw new RegistrationException("The URI syntax is not well formed.", e.getCause());							
		} catch (SAWSDLException e) {
			throw new RegistrationException("Errors durring parsing the service.", e.getCause());		
		} catch (WSDL4ComplexWsdlException e) {
			throw new RegistrationException("Errors durring parsing the service.", e.getCause());
		} catch (RepositoryException e) {
			throw new RegistrationException("Errors durring saving of triples into repository.", e.getCause());
		}
		
		if ( SERVICE_NS != null && SERVICE_NAME != null ){
			//reposHandler.commit();
        	return SERVICE_NS + SERVICE_NAME;
		}
		return null;
	}
	
	private static void writeSubelementDeclaration(QName elementQName, QName subelementQName, String _wsdlURI) throws RepositoryException, SAWSDLException {
		logger.info("SUBELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(subelementQName.getLocalPart())  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("QName"));
		logger.info("SUBELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(subelementQName.getLocalPart())  + " , " + 
				QueryHelper.getWSDLURI("localName") + " , " + subelementQName.getLocalPart() );
		logger.info("SUBELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(subelementQName.getLocalPart())  + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + subelementQName.getNamespaceURI());
		
		logger.info("SUBELEMENT DECL FOR  ELEMENT DECL INFO: Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart()) + " , " + 
				QueryHelper.getWSDLURI("elementDeclaration") + " , " + getElementDeclarationNode(subelementQName.getLocalPart()));
		
		List<URI> modelReferences = elementsMap.get(subelementQName).getModelReference();
		for ( URI modelReferece : modelReferences) {
			logger.info("SUBELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(subelementQName.getLocalPart())  + " , " + 
					QueryHelper.getSAWSDLURI("modelReference") + " , " + modelReferece);
			
			//Writing persistent
			reposHandler.addResourceTriple(getElementDeclarationNode(subelementQName.getLocalPart()), QueryHelper.getSAWSDLURI("modelReference"), modelReferece.toString(), _wsdlURI);
		}
		
		//Writing persistent
		reposHandler.addResourceTriple(getElementDeclarationNode(subelementQName.getLocalPart()), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("QName"), _wsdlURI);
		reposHandler.addLiteralTriple(getElementDeclarationNode(subelementQName.getLocalPart()), QueryHelper.getWSDLURI("localName"), subelementQName.getLocalPart(), _wsdlURI);
		reposHandler.addResourceTriple(getElementDeclarationNode(subelementQName.getLocalPart()), QueryHelper.getWSDLURI("namespace"), subelementQName.getNamespaceURI(), _wsdlURI);
		
		reposHandler.addResourceTriple(getElementDeclarationNode(elementQName.getLocalPart()), QueryHelper.getWSDLURI("elementDeclaration"), getElementDeclarationNode(subelementQName.getLocalPart()), _wsdlURI);
	}
	
	private static void writeElementDeclaration(QName elementQName, String interfaceName, String operationName, String messageLabel, String _wsdlURI) throws RepositoryException, SAWSDLException {
		logger.info("ELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart())  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("QName"));
		logger.info("ELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart())  + " , " + 
				QueryHelper.getWSDLURI("localName") + " , " + elementQName.getLocalPart() );
		logger.info("ELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart())  + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + elementQName.getNamespaceURI());
		
		logger.info("ELEMENT DECL FOR INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getWSDLURI("elementDeclaration") + " , " + getElementDeclarationNode(elementQName.getLocalPart()));
		
		
		List<URI> loweringURIs = elementsMap.get(elementQName).getLoweringSchemaMapping();
		for ( URI loweringURI : loweringURIs ) {
 			logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel)  + " , " + 
				QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , " + loweringURI);
 			
 			//Writing persistent
 			reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getSAWSDLURI("loweringSchemaMapping"), loweringURI.toString(), _wsdlURI);
 		}
		
		List<URI> liftingURIs = elementsMap.get(elementQName).getLoweringSchemaMapping();
		for ( URI liftingURI : liftingURIs ) {
			logger.info("OUTPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel)  + " , " + 
					QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , " + liftingURI);
		
			//Writing persistent
			reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getSAWSDLURI("liftingSchemaMapping"), liftingURI.toString(), _wsdlURI);
		}
		
		List<URI> modelReferences = elementsMap.get(elementQName).getModelReference();
		for ( URI modelReferece : modelReferences) {
			logger.info("ELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart())  + " , " + 
					QueryHelper.getSAWSDLURI("modelReference") + " , " + modelReferece);
			
			//Writing persistent
			reposHandler.addResourceTriple(getElementDeclarationNode(elementQName.getLocalPart()), QueryHelper.getSAWSDLURI("modelReference"), modelReferece.toString(), _wsdlURI);
		}
		
		//Writing persistent
		reposHandler.addResourceTriple(getElementDeclarationNode(elementQName.getLocalPart()), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("QName"), _wsdlURI);
		reposHandler.addLiteralTriple(getElementDeclarationNode(elementQName.getLocalPart()), QueryHelper.getWSDLURI("localName"), elementQName.getLocalPart(), _wsdlURI);
		reposHandler.addResourceTriple(getElementDeclarationNode(elementQName.getLocalPart()), QueryHelper.getWSDLURI("namespace"), elementQName.getNamespaceURI(), _wsdlURI);
		
		reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel), QueryHelper.getWSDLURI("elementDeclaration"), getElementDeclarationNode(elementQName.getLocalPart()), _wsdlURI);
	}
	
	private static void writeServiceToTriples(List<URI> serviceCategories, String namespaceURI, String _wsdlURI) throws RepositoryException {
 		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getMSMEXTURI("Service"));
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()  + " , " + 
				QueryHelper.getRDFSURI("isDefinedBy") + " , " + _wsdlURI);
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()  + " , " + 
				QueryHelper.getMSMEXTURI("wsdlDescription") + " , " + getDescriptionNode());
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()  + " , " + 
				QueryHelper.getRDFSURI("label") + " , " + SERVICE_NAME);
		logger.info("SERVICE INFO : Triple + Context: " + getServiceID()  + " , " + 
				QueryHelper.getDCURI("creator") + " , " + "STI Innsbruck");
		
		for ( URI category : serviceCategories ) {
			logger.info("SERVICE INFO : Triple + Context: " + getServiceID()  + " , " + 
					QueryHelper.getSAWSDLURI("modelReference") + " , " + category);
			
			//Writing persistent
			reposHandler.addResourceTriple(getServiceID() , QueryHelper.getSAWSDLURI("modelReference"), category.toString(), _wsdlURI);
		}
		
		logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode()  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getMSMEXTURI("Service"));
		logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode()  + " , " + 
				QueryHelper.getRDFSURI("label") + " , " + SERVICE_NAME);
		 
		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Description"));
		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
				QueryHelper.getWSDLURI("service") + " , " + getServiceNode());
		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + namespaceURI);

    	//Writing persistent
		reposHandler.addResourceTriple(getServiceID() , QueryHelper.getRDFURI("type"),  QueryHelper.getMSMEXTURI("Service"), _wsdlURI);
		reposHandler.addResourceTriple(getServiceID() , QueryHelper.getRDFSURI("isDefinedBy"),  _wsdlURI, _wsdlURI);
		reposHandler.addResourceTriple(getServiceID() , QueryHelper.getMSMEXTURI("wsdlDescription"),  getDescriptionNode(), _wsdlURI);
		reposHandler.addLiteralTriple(getServiceID() , QueryHelper.getRDFSURI("label"), SERVICE_NAME, _wsdlURI);
		reposHandler.addLiteralTriple(getServiceID() , QueryHelper.getDCURI("creator"), "STI Innsbruck", _wsdlURI);
	
		reposHandler.addResourceTriple(getServiceNode() , QueryHelper.getRDFURI("type"),  QueryHelper.getMSMEXTURI("Service"), _wsdlURI);
		reposHandler.addLiteralTriple(getServiceNode() , QueryHelper.getRDFSURI("label"),  SERVICE_NAME, _wsdlURI);
		
		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Description"), _wsdlURI);
		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("service"),  getServiceNode(), _wsdlURI);
		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("namespace"),  namespaceURI, _wsdlURI);
 	}
	
	private static void writeEndpointsToTriples(String endpointName, String endpointAddress, String _wsdlURI) throws RepositoryException {
 		logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(endpointName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Endpoint"));
		logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(endpointName) + " , " 
				+ QueryHelper.getRDFSURI("label") + " , " + endpointName);
		logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(endpointName) + " , " 
				+ QueryHelper.getWSDLURI("address") + " , " + endpointAddress);
		logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(endpointName) + " , " 
				+ QueryHelper.getWSDLURI("useBinding") + " , " + getBindingNode(endpointName));
		
		logger.info("SERVICE ENDPOINTS : Triple + Context: " + getServiceNode() + " , " 
				+ QueryHelper.getWSDLURI("endpoint") + " , " + getEndpointNode(endpointName));
		
		logger.info("DESCRIPTION ENDPOINTS : Triple + Context: " + getDescriptionNode() + " , " 
				+ QueryHelper.getWSDLURI("endpoint") + " , " + getEndpointNode(endpointName));
		
		//Writing persistent
		reposHandler.addResourceTriple(getEndpointNode(endpointName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Endpoint"), _wsdlURI);
		reposHandler.addLiteralTriple(getEndpointNode(endpointName) , QueryHelper.getRDFSURI("label"),  endpointName, _wsdlURI);
		reposHandler.addResourceTriple(getEndpointNode(endpointName) , QueryHelper.getWSDLURI("address"),  endpointAddress, _wsdlURI);
		reposHandler.addResourceTriple(getEndpointNode(endpointName) , QueryHelper.getWSDLURI("useBinding"),  getEndpointNode(endpointName), _wsdlURI);
		
		reposHandler.addResourceTriple(getServiceNode() , QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(endpointName), _wsdlURI);
		
		reposHandler.addResourceTriple(getDescriptionNode() ,QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(endpointName), _wsdlURI);
 	}
 	
 	private static void writeInterfaceMessageReferenceToTriples(String interfaceName, String operationName, String messageLabel, String pattern, String _wsdlURI) throws RepositoryException {
 		logger.info("INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InputMessage"));
 		logger.info("INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
 				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceMessageReference"));
 		logger.info("INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
 				QueryHelper.getWSDLURI("messageLabel") + " , " + pattern + "#" + messageLabel);
 		
 		
 		logger.info("INTERFACE MSG REF INFO FOR INPUT : Triple + Context: " + getInterfaceOperationNode(interfaceName, operationName) + " , " + 
				QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel));
 		
 		//Writing persistent
 		reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("InputMessage"), _wsdlURI);
		reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceMessageReference"), _wsdlURI);
		reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getWSDLURI("messageLabel"),  pattern + "#" + messageLabel , _wsdlURI);
	
		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, operationName) , QueryHelper.getWSDLURI("interfaceMessageReference"), getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel), _wsdlURI);
	}
 	
 	private static void writeBindingsToTriples(String bindingName, String bindingType, String _wsdlURI) throws RepositoryException, URISyntaxException {
 		logger.info("BINDINGS : Triple + Context: " + getBindingNode(bindingName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Binding"));
		logger.info("BINDINGS : Triple + Context: " + getBindingNode(bindingName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + bindingType);
		logger.info("BINDINGS : Triple + Context: " + getBindingNode(bindingName) + " , " 
				+ QueryHelper.getRDFSURI("label") + " , " + bindingName);
		logger.info("BINDINGS : Triple + Context: " + getBindingNode(bindingName) + " , " 
				+ QueryHelper.getWSDLURI("binds") + " , " + getInterfaceNode(bindingName));
		logger.info("BINDINGS : Triple + Context: " + getBindingNode(bindingName) + " , " 
				+ QueryHelper.getWSDLURI("bindingFault") + " , " + "");
		
		logger.info("BINDINGS FOR DESCRIPTION: Triple + Context: " + getDescriptionNode() + " , " 
				+ QueryHelper.getWSDLURI("binding") + " , " + getBindingNode(bindingName));

		//Writing persistent
		reposHandler.addResourceTriple(getBindingNode(bindingName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Binding"), _wsdlURI);
//		reposHandler.addResourceTriple(getBindingNode(bindingName), QueryHelper.getRDFURI("type"), bindingType, _wsdlURI);
		reposHandler.addLiteralTriple(getBindingNode(bindingName), QueryHelper.getRDFSURI("label"), bindingName, _wsdlURI);
		reposHandler.addResourceTriple(getBindingNode(bindingName), QueryHelper.getWSDLURI("binds"), getInterfaceNode(bindingName), _wsdlURI);
		reposHandler.addResourceTriple(getBindingNode(bindingName), QueryHelper.getWSDLURI("bindingFault"), SERVICE_NS, _wsdlURI);
		
		reposHandler.addResourceTriple(getDescriptionNode(), QueryHelper.getWSDLURI("binding"), getBindingNode(bindingName), _wsdlURI);
 	}
 	
 	private static void writeBindingOperationsToTriples(String bindingName, String bindingOperationName, String soapActionURI, String _wsdlURI) throws RepositoryException {
 		logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(bindingOperationName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("BindingOperation"));
		
		
		logger.info("BINDINGS OPERATION FOR BINDING PARENT: Triple + Context: " + getBindingNode(bindingOperationName) + " , " 
				+ QueryHelper.getWSDLURI("bindingOperation") + " , " + getBindingOperationNode(bindingOperationName));
		
		//Writing persistent
		reposHandler.addResourceTriple(getBindingOperationNode(bindingOperationName), QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("BindingOperation"), _wsdlURI);
		
		/**
		 * XXX: Fix this. SOAPAction could be also only a string or empty
		 */
		if ( soapActionURI != null ) 
			try {
				logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(bindingOperationName) + " , " 
						+ QueryHelper.getWSOAPURI("action") + " , " + soapActionURI);
				
				//Writing persistent
				reposHandler.addResourceTriple(getBindingOperationNode(bindingOperationName), QueryHelper.getWSOAPURI("action"), soapActionURI, _wsdlURI);
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		reposHandler.addResourceTriple(getBindingNode(bindingName), QueryHelper.getWSDLURI("bindingOperation"), getBindingOperationNode(bindingOperationName), _wsdlURI);
 	}
 	
 	private static void writeInterfaceToTriples(String interfaceName, String _wsdlURI) throws RepositoryException {
 		logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Interface"));	
		logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
				+ QueryHelper.getRDFSURI("label") + " , " + interfaceName);
		
		logger.info("INTERFACE FOR DESCRIPTION : Triple + Context: " + getDescriptionNode() + " , " 
				+ QueryHelper.getWSDLURI("interface") + " , " + getInterfaceNode(interfaceName));
		
		//Writing persistent
		reposHandler.addResourceTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Interface"), _wsdlURI);
		reposHandler.addLiteralTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFSURI("label"), interfaceName, _wsdlURI);
		
		reposHandler.addResourceTriple(getDescriptionNode(), QueryHelper.getWSDLURI("interface"), getInterfaceNode(interfaceName), _wsdlURI);
 	}
 	
 	private static void writeInterfaceOperationsToTriples(String interfaceName, String interfaceOperationName, String _wsdlURI) throws RepositoryException {
 		logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceOperation"));
		logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getRDFSURI("label") + " , " + interfaceOperationName);
		
		logger.info("INTERFACE OPERATION FOR INTERFACE: Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
				+ QueryHelper.getWSDLURI("interfaceOperation") + " , " + getInterfaceOperationNode(interfaceName, interfaceOperationName));
		
		//Writing persistent
		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceOperation"), _wsdlURI);
		reposHandler.addLiteralTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName), QueryHelper.getRDFSURI("label"), interfaceOperationName, _wsdlURI);
		
		reposHandler.addResourceTriple(getInterfaceNode(interfaceName), QueryHelper.getWSDLURI("interfaceOperation"), getInterfaceOperationNode(interfaceName, interfaceOperationName), _wsdlURI);
 	}
	
	private static String getElementDeclarationNode(String elementName) {
		return  SERVICE_NS + "wsdl.elementDeclaration(" + elementName + ")";
	}
	
	private static String getInterfaceMessageReferenceNode(String interfaceName, String operationName, String messageLabel ) {
		return SERVICE_NS + "wsdl.interfaceMessageReference(" + interfaceName + "/" + operationName + "/" + messageLabel + ")"; 
	}
	
	private static String getInterfaceNode(String interfaceName) {
		return SERVICE_NS +"wsdl.interface(" + interfaceName + ")"; 
	}
	
	private static String getInterfaceOperationNode(String interfaceName, String interfaceOperationName) {
		return SERVICE_NS +"wsdl.interfaceOperation(" + interfaceName + "/" + interfaceOperationName + ")"; 
	}
	
	private static String getBindingNode(String bindingName ) {
		return SERVICE_NS +"wsdl.binding(" + SERVICE_NAME + "/" + bindingName + ")"; 
	}
	
	private static String getBindingOperationNode(String bindingOperationName ) {
		return SERVICE_NS +"wsdl.bindingOperation(" + SERVICE_NAME + "/" + bindingOperationName + ")"; 
	}
	
	private static String getEndpointNode(String bindingName ) {
		return SERVICE_NS +"wsdl.endpoint(" + SERVICE_NAME + "/" + bindingName + ")"; 
	}
	
	private static String getServiceNode() {
		return SERVICE_NS +"wsdl.service(" + SERVICE_NAME + ")"; 
	}
	
	private static String getServiceID() {
		return SERVICE_NS + SERVICE_NAME;
	}
	
	private static String getDescriptionNode() {
		return SERVICE_NS + "wsdl.description()";
	}
}