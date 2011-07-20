package at.sti2.ngsee.registration.core.transformation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.wsdl.extensions.http.HTTPAddressImpl;
import com.ibm.wsdl.extensions.http.HTTPBindingImpl;
import com.ibm.wsdl.extensions.http.HTTPOperationImpl;
import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;
import com.ibm.wsdl.extensions.soap.SOAPBindingImpl;
import com.ibm.wsdl.extensions.soap.SOAPOperationImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12AddressImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12BindingImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12OperationImpl;

import at.sti2.ngsee.registration.core.common.Config;
import at.sti2.util.triplestore.QueryHelper;
import at.sti2.util.triplestore.RepositoryHandler;

import edu.uga.cs.lsdis.sawsdl.*;
import edu.uga.cs.lsdis.sawsdl.extensions.schema.Schema;
import edu.uga.cs.lsdis.sawsdl.impl.util.SchemaUtils;

/**
 * 
 * @author Corneliu Stanciu
 *
 */
public class Transformation2 {
	
	private static Logger logger = Logger.getLogger(Transformation2.class);
	private static String SERVICE_NS;
	private static String SERVICE_NAME;
	private static RepositoryHandler reposHandler;
	
 	public static String transformWSDL(String _wsdlURI) throws WSDLException, FileNotFoundException, IOException, RepositoryException, URISyntaxException{
 		System.setProperty("javax.wsdl.factory.WSDLFactory", "edu.uga.cs.lsdis.sawsdl.impl.factory.WSDLFactoryImpl");
 		
 		WSDLReader wsdlReader = WSDLFactory.newInstance().newWSDLReader();
        Definition definition = (Definition) wsdlReader.readWSDL(_wsdlURI);
        
        Config cfg = new Config();
		reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		
		Types types = definition.getTypes();
		List<Schema> schemaList = SchemaUtils.getSchemas(types);
		Schema schema = schemaList.get(0);
		
		HashMap<String, String> llMap = new HashMap<String, String>();
		NodeList elements = schema.getElement().getChildNodes();
		for ( int count=0; count < elements.getLength(); count++ ) {
			Node item = elements.item(count);
			if ( item.getNodeType() == Node.ELEMENT_NODE ) {
				Node liftingSchemaNode = item.getAttributes().getNamedItem("sawsdl:liftingSchemaMapping");
				Node loweringSchemaNode = item.getAttributes().getNamedItem("sawsdl:loweringSchemaMapping");
				if ( liftingSchemaNode != null ) 
					llMap.put(item.getAttributes().getNamedItem("name").getNodeValue(), liftingSchemaNode.getNodeValue());
//					System.out.println("NODE:" + item.getAttributes().getNamedItem("name") + "--" + liftingSchemaNode.getNodeValue());
				if ( loweringSchemaNode != null )
					llMap.put(item.getAttributes().getNamedItem("name").getNodeValue(), loweringSchemaNode.getNodeValue());
//					System.out.println("NODE:" + item.getAttributes().getNamedItem("name") + "--" + loweringSchemaNode.getNodeValue());
			}
		}
		
		
        //Service
        Map<?, ?> services = definition.getServices();
        for ( Object serviceVal : services.values() ) {
        	Service service = (Service) serviceVal;
        	SERVICE_NAME = service.getQName().getLocalPart();
        	String namespaceURI = service.getQName().getNamespaceURI();
        	SERVICE_NS = service.getQName().getNamespaceURI() + "#";
        	
        	QName serviceCategories = (QName) service.getExtensionAttribute(new QName("http://www.w3.org/ns/sawsdl", "modelReference"));
        	Vector<String> categories = extractModelReference(serviceCategories);
        	
        	writeServiceToTriples(categories, namespaceURI, _wsdlURI);
        	
        	//Port
        	Map<?, ?> ports = service.getPorts();
        	for ( Object portKey : ports.values() ) {
        		Port port = (Port) portKey;
        		
        		// Endpoint
        		String endpointName = port.getName();
        		String endpointAddress = null;
        		
        		List<?> portElements = port.getExtensibilityElements();
        		for ( Object elementObj : portElements ) {
        			if ( elementObj instanceof HTTPAddressImpl ){
        				endpointAddress = ((HTTPAddressImpl) elementObj).getLocationURI();
        				break;
        			}
        			if ( elementObj instanceof SOAPAddressImpl ){
        				endpointAddress = ((SOAPAddressImpl) elementObj).getLocationURI();
        				break;
        			}
        			if ( elementObj instanceof SOAP12AddressImpl ){
        				endpointAddress = ((SOAP12AddressImpl) elementObj).getLocationURI();
        				break;
        			}	
        		}
	
        		if ( endpointAddress != null)
        			writeEndpointsToTriples(endpointName, endpointAddress, _wsdlURI);
        		
        		//Binding
				Binding binding = port.getBinding();
				String bindingName = binding.getQName().getLocalPart();
				String bindingType = null;
				
				List<?> bindingElements = binding.getExtensibilityElements();
				for ( Object elementObj : bindingElements ) {
					if ( elementObj instanceof HTTPBindingImpl ){
//						bindingType = ((HTTPBindingImpl) elementObj).getVerb();
        				break;
        			}
					if ( elementObj instanceof SOAP12BindingImpl ){
						bindingType = ((SOAP12BindingImpl) elementObj).getTransportURI();
        				break;
        			}
					if ( elementObj instanceof SOAPBindingImpl ){
						bindingType = ((SOAPBindingImpl) elementObj).getTransportURI();
        				break;
        			}
				}
				
				if ( bindingType != null ) 
					writeBindingsToTriples(bindingName, bindingType, _wsdlURI);
				
				//Interface (Port type)
				PortType portType = binding.getPortType();
				String interfaceName = portType.getQName().getLocalPart();
				
				if ( interfaceName != null )
					writeInterfaceToTriples(interfaceName, _wsdlURI);
				
				//Interface Operation ( Port type Operation)
        		List<?> operations = portType.getOperations();
        		for ( Object operationObj : operations ) {
        			Operation operation = (Operation) operationObj;
        			String interfaceOperationName = operation.getName();
        			
        			String inputName = null;
        			String outputName = null;
        			
        			//Operation Input and Output
        			Input input = operation.getInput();
        			Output output = operation.getOutput();
        			
        			//TODO: Extract Faults and write into triples
//        			Map<?, ?> faultsMap = operation.getFaults();

        			//Input
        			if ( input != null ) {
        				Message inputMessage = input.getMessage();
        				if ( inputMessage != null ) {
        					inputName = inputMessage.getQName().getLocalPart();
        					
        					Map<?,?> inputParts = inputMessage.getParts();
                			for ( Object partObj : inputParts.values() ) {
                				Part inputPart = (Part) partObj;
                				QName elemName = inputPart.getElementName();
                				
                				if ( elemName != null )
                					writeInputMessagePartToTriples(inputName, inputPart.getName(), namespaceURI, llMap.get(elemName.getLocalPart()) , _wsdlURI);
                			}
        					writeInputMessageToTriples(interfaceName, interfaceOperationName, inputName, _wsdlURI);
        				}
        			}

        			//Output
        			if ( output != null ){
        				Message outputMessage = output.getMessage();
        				if ( outputMessage != null) {
        					outputName = outputMessage.getQName().getLocalPart();
        					
        					Map<?,?> outputParts = outputMessage.getParts();
                			for ( Object partObj : outputParts.values() ) {
                				Part outputPart = (Part) partObj;
                				QName elemName = outputPart.getElementName();
                				
                				if ( elemName != null )
                					writeOutputMessagePartToTriples(outputName, outputPart.getName(), namespaceURI, llMap.get(elemName.getLocalPart()), _wsdlURI);
                			}
        					writeOutputMessageToTriples(interfaceName, interfaceOperationName, outputName, _wsdlURI);
        				}
        			}
        			
        			if ( inputName != null && outputName != null ) 
        				writeInterfaceOperationsToTriples(interfaceName, interfaceOperationName, _wsdlURI);
        		}
				
				//Binding Operations
        		List<?> bindingsOperations = binding.getBindingOperations();
        		for ( Object operationObj : bindingsOperations ) {
        			BindingOperation bindingOperation = (BindingOperation) operationObj;
        			String bindingOperationName = bindingOperation.getName();
        			String soapActionURI = null;
        			
        			List<?> bindingOperationElements = bindingOperation.getExtensibilityElements();
            		for ( Object elementObj : bindingOperationElements ) {
            			if ( elementObj instanceof HTTPOperationImpl ){
            				//TODO: HTTP Operations - Feathers 
            				break;
            			}
            			if ( elementObj instanceof SOAPOperationImpl){
            				soapActionURI = ((SOAPOperationImpl) elementObj).getSoapActionURI();
            				break;
            			}
            			if ( elementObj instanceof SOAP12OperationImpl){
            				soapActionURI = ((SOAP12OperationImpl) elementObj).getSoapActionURI();
            				break;
            			}
            		}        			

            		if ( soapActionURI != null )
            			writeBindingOperationsToTriples(bindingName, bindingOperationName, soapActionURI, _wsdlURI);
        		}
        	}
        }
        
		if ( SERVICE_NS != null && SERVICE_NAME != null ){
			reposHandler.commit();
        	return SERVICE_NS + SERVICE_NAME;
		}
        return null;
	}
 	
 	private static void writeServiceToTriples(Vector<String> categories, String namespaceURI, String _wsdlURI) throws RepositoryException {
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
		
		for ( String category : categories ) {
			logger.info("SERVICE INFO : Triple + Context: " + getServiceID()  + " , " + 
					QueryHelper.getSAWSDLURI("modelReference") + " , " + category);
			
			reposHandler.addResourceTriple(getServiceID() , QueryHelper.getSAWSDLURI("modelReference"), category, _wsdlURI);
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
 	
 	private static void writeInputMessagePartToTriples(String inputName, String inputPartName, String namespaceURI, String loweringURI, String _wsdlURI) throws RepositoryException {
 		logger.info("INPUT PART INFO : Triple + Context: " + getInputMessagePartNode(inputName, inputPartName) + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("QName"));
 		logger.info("INPUT PART INFO : Triple + Context: " + getInputMessagePartNode(inputName, inputPartName) + " , " + 
				QueryHelper.getWSDLURI("localName") + " , " + inputPartName);
 		logger.info("INPUT PART INFO : Triple + Context: " + getInputMessagePartNode(inputName, inputPartName) + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + namespaceURI);
 		
 		logger.info("INPUT PART FOR INPUT : Triple + Context: " + getInputMessageNode(inputName) + " , " + 
				QueryHelper.getWSDLURI("elementDeclaration") + " , " + getInputMessagePartNode(inputName, inputPartName));
 		logger.info("INPUT PART FOR INPUT: Triple + Context: " + getInputMessageNode(inputName)  + " , " + 
				QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , " + loweringURI);
 		
 		reposHandler.addResourceTriple(getInputMessagePartNode(inputName, inputPartName) , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("QName"), _wsdlURI);
		reposHandler.addLiteralTriple(getInputMessagePartNode(inputName, inputPartName) , QueryHelper.getWSDLURI("localName"), inputPartName, _wsdlURI);
		reposHandler.addResourceTriple(getInputMessagePartNode(inputName, inputPartName) , QueryHelper.getWSDLURI("namespace"), namespaceURI, _wsdlURI);
		
		reposHandler.addResourceTriple(getInputMessageNode(inputName) , QueryHelper.getWSDLURI("elementDeclaration"), getInputMessagePartNode(inputName, inputPartName), _wsdlURI);
		if ( loweringURI != null )
			reposHandler.addResourceTriple(getInputMessageNode(inputName) , QueryHelper.getSAWSDLURI("loweringSchemaMapping"), loweringURI, _wsdlURI);
	}
 	
 	private static void writeOutputMessagePartToTriples(String outputName, String outputPartName, String namespaceURI, String liftingURI, String _wsdlURI) throws RepositoryException, URISyntaxException {
 		logger.info("OUTPUT PART INFO : Triple + Context: " + getOutputMessagePartNode(outputName, outputPartName) + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("QName"));
 		logger.info("OUTPUT PART INFO : Triple + Context: " + getOutputMessagePartNode(outputName, outputPartName) + " , " + 
				QueryHelper.getWSDLURI("localName") + " , " + outputPartName);
 		logger.info("OUTPUT PART INFO : Triple + Context: " + getOutputMessagePartNode(outputName, outputPartName) + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + namespaceURI);
 		
 		logger.info("OUTPUT PART FOR OUTPUT : Triple + Context: " + getOutputMessageNode(outputName) + " , " + 
				QueryHelper.getWSDLURI("elementDeclaration") + " , " + getOutputMessagePartNode(outputName, outputPartName));
 		logger.info("OUTPUT PART FOR OUTPUT: Triple + Context: " + getOutputMessageNode(outputName)  + " , " + 
				QueryHelper.getSAWSDLURI("liftingSchemaMapping") + " , " + liftingURI);
 		
 		reposHandler.addResourceTriple(getOutputMessagePartNode(outputName, outputPartName) , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("QName"), _wsdlURI);
		reposHandler.addLiteralTriple(getOutputMessagePartNode(outputName, outputPartName) , QueryHelper.getWSDLURI("localName"), outputPartName, _wsdlURI);
		reposHandler.addResourceTriple(getOutputMessagePartNode(outputName, outputPartName) , QueryHelper.getWSDLURI("namespace"), namespaceURI, _wsdlURI);
		
		reposHandler.addResourceTriple(getOutputMessageNode(outputName) , QueryHelper.getWSDLURI("elementDeclaration"), getOutputMessagePartNode(outputName, outputPartName), _wsdlURI);
		if ( liftingURI != null )
			reposHandler.addResourceTriple(getOutputMessageNode(outputName) , QueryHelper.getSAWSDLURI("liftingSchemaMapping"), liftingURI, _wsdlURI);
	}

	private static void writeInputMessageToTriples(String interfaceName, String interfaceOperationName, String inputName, String _wsdlURI) throws RepositoryException {
 		logger.info("INPUT INFO : Triple + Context: " + getInputMessageNode(inputName) + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InputMessage"));
 		logger.info("INPUT INFO : Triple + Context: " + getInputMessageNode(inputName)  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceMessageReference"));
 		
 		logger.info("INPUT FOR INTERFACE: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getInputMessageNode(inputName));
 		
 		reposHandler.addResourceTriple(getInputMessageNode(inputName) , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InputMessage"), _wsdlURI);
 		reposHandler.addResourceTriple(getInputMessageNode(inputName) , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceMessageReference") , _wsdlURI);
		
		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName) , QueryHelper.getWSDLURI("interfaceMessageReference"), getInputMessageNode(inputName), _wsdlURI);
	}
 	
 	private static void writeOutputMessageToTriples(String interfaceName, String interfaceOperationName, String outputName, String _wsdlURI) throws RepositoryException {
 		logger.info("OUTPUT INFO : Triple + Context: " + getOutputMessageNode(outputName)  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("OutputMessage"));
 		
 		logger.info("OUTPUT FOR INTERFACE: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getOutputMessageNode(outputName));
 		
 		reposHandler.addResourceTriple(getOutputMessageNode(outputName) , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("OutputMessage"), _wsdlURI);
 		
		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName) , QueryHelper.getWSDLURI("interfaceMessageReference"), getOutputMessageNode(outputName), _wsdlURI);
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
		logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(bindingOperationName) + " , " 
				+ QueryHelper.getWSOAPURI("action") + " , " + soapActionURI);
		
		logger.info("BINDINGS OPERATION FOR BINDING PARENT: Triple + Context: " + getBindingNode(bindingOperationName) + " , " 
				+ QueryHelper.getWSDLURI("bindingOperation") + " , " + getBindingOperationNode(bindingOperationName));
		
		//Writing persistent
		reposHandler.addResourceTriple(getBindingOperationNode(bindingOperationName), QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("BindingOperation"), _wsdlURI);
		reposHandler.addResourceTriple(getBindingOperationNode(bindingOperationName), QueryHelper.getWSOAPURI("action"), soapActionURI, _wsdlURI);
				
		reposHandler.addResourceTriple(getBindingNode(bindingName), QueryHelper.getWSDLURI("bindingOperation"), getBindingOperationNode(bindingOperationName), _wsdlURI);
 	}
 	
 	private static void writeInterfaceToTriples(String interfaceName, String _wsdlURI) throws RepositoryException {
 		logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Interface"));	
		logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
				+ QueryHelper.getRDFURI("label") + " , " + interfaceName);
		
		logger.info("INTERFACE FOR DESCRIPTION : Triple + Context: " + getDescriptionNode() + " , " 
				+ QueryHelper.getWSDLURI("interface") + " , " + getInterfaceNode(interfaceName));
		
		//Writing persistent
		reposHandler.addResourceTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Interface"), _wsdlURI);
		reposHandler.addLiteralTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("label"), interfaceName, _wsdlURI);
		
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
	
	private static String getDescriptionNode() {
		return SERVICE_NS + "wsdl.description()"; 
	}
	
	private static String getInputMessageNode(String messageName) {
		return SERVICE_NS + "wsdl.interfaceMessageReference(" + messageName + ")"; 
	}
	
	private static String getOutputMessageNode(String messageName) {
		return SERVICE_NS + "wsdl.interfaceMessageReference(" + messageName + ")"; 
	}
	
	private static String getInputMessagePartNode(String messageName, String partName) {
		return SERVICE_NS + "wsdl.interfaceMessageReference(" + messageName + "/" + partName + ")"; 
	}
	
	private static String getOutputMessagePartNode(String messageName, String partName) {
		return SERVICE_NS + "wsdl.interfaceMessageReference(" + messageName + "/" + partName + ")"; 
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
	
	private static Vector<String> extractModelReference(QName serviceCategories) {
		Vector<String> categories = new Vector<String>();
		String list = serviceCategories.getLocalPart().replaceAll("  ", " ").replaceAll("http:", "").replaceAll("//", "");
		String[] serviceCategoriesList = list.split(" ");
		for ( String category : serviceCategoriesList ) {
			if ( !category.isEmpty() ) {
				categories.add("http://" + category);
//				System.err.println(category);
			}
		}
		return categories;
	}
	
	public static void main(String[] args) throws WSDLException, FileNotFoundException, IOException, RepositoryException, URISyntaxException {
		String serviceID = transformWSDL("http://sesa.sti2.at/services/globalweather.sawsdl");
		if ( serviceID != null ) {
			System.out.println(serviceID);
			reposHandler.commit();
		}
	}
}
