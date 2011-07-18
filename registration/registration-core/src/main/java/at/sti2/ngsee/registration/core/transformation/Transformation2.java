package at.sti2.ngsee.registration.core.transformation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

public class Transformation2 {
	
	private static Logger logger = Logger.getLogger(Transformation2.class);
	private static String SERVICE_NS;
	private static RepositoryHandler reposHandler;
	
	public static void main(String[] args) throws WSDLException, FileNotFoundException, IOException, RepositoryException, URISyntaxException {
		transformWSDL("file:///home/koni/globalweather.asmx");
	}
	
 	private static void transformWSDL(String _wsdlURI) throws WSDLException, FileNotFoundException, IOException, RepositoryException, URISyntaxException{
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
				System.out.println("NODE:" + item.getAttributes().getNamedItem("name") + "--" + item.getAttributes().getNamedItem("sawsdl:liftingSchemaMapping"));
				System.out.println("NODE:" + item.getAttributes().getNamedItem("name") + "--" + item.getAttributes().getNamedItem("sawsdl:loweringSchemaMapping"));
			}
		}
		
		
        //Service
        Map<?, ?> services = definition.getServices();
        for ( Object serviceVal : services.values() ) {
        	Service service = (Service) serviceVal;
        	String serviceName = service.getQName().getLocalPart();
        	String namespaceURI = service.getQName().getNamespaceURI();
        	SERVICE_NS = service.getQName().getNamespaceURI() + "#";
        	
        	QName serviceCategories = (QName) service.getExtensionAttribute(new QName("http://www.w3.org/ns/sawsdl", "modelReference"));
        	Vector<String> categories = extractModelReference(serviceCategories);
        	
//        	writeServiceToTriples(serviceName, categories, namespaceURI, _wsdlURI);
        	
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
	
//        		if ( endpointAddress != null)
//        			writeEndpointsToTriples(serviceName, endpointName, endpointAddress, _wsdlURI);
        		
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
				
//				if ( bindingType != null ) 
//					writeBindingsToTriples(serviceName, bindingName, bindingType, _wsdlURI);
				
				//Interface (Port type)
				PortType portType = binding.getPortType();
				String interfaceName = portType.getQName().getLocalPart();
				
//				if ( interfaceName != null )
//					writeInterfaceToTriples(interfaceName, _wsdlURI);
				
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
        			Map<?, ?> faultsMap = operation.getFaults();

        			//Input
        			if ( input != null ) {
        				Message inputMessage = input.getMessage();
        				if ( inputMessage != null ) {
        					inputName = inputMessage.getQName().getLocalPart();
        					
        					Map<?,?> inputParts = inputMessage.getParts();
                			for ( Object partObj : inputParts.values() ) {
                				Part inputPart = (Part) partObj;
                				
//                				writeInputPartToTriples(inputName, inputPart.getName(), namespaceURI, _wsdlURI);
                			}
//        					writeInputToTriples(interfaceName, interfaceOperationName, inputName, new URI("http://example.com"), _wsdlURI);
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
//                				writeOutputPartToTriples(outputName, outputPart.getName(), namespaceURI, _wsdlURI);
                			}
//        					writeOutputToTriples(interfaceName, interfaceOperationName, outputName, new URI("http://example.com"), _wsdlURI);
        				}
        			}
        			
//        			if ( inputName != null && outputName != null ) 
//        				writeInterfaceOperationsToTriples(interfaceName, interfaceOperationName, _wsdlURI);
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

//            		if ( soapActionURI != null )
//            			writeBindingOperationsToTriples(serviceName,bindingName, bindingOperationName, soapActionURI, _wsdlURI);
        		}
        	}
        }
//        reposHandler.commit();
        System.out.println("********************   COMMIT   **************************");

         //get and print the messages
//        Map messages = definition.getMessages();
//        for (Object key:messages.keySet()){
//            Message semanticMessage = definition.getSemanticMessage((QName)key);
//
////          System.out.println("Message QName ->" + semanticMessage.getQName());
//
//            Map parts = semanticMessage.getParts();
//
//            for (Object partKey : parts.keySet()) {
//                Part semanticPart = semanticMessage.getSemanticPart((String) partKey);
//                System.out.println("part ->" + semanticPart.getExtensionAttributes());
//                System.out.println("part model references ->" + semanticPart.getModelReferences());
//            }
//        }
	}
 	
 	private static void writeServiceToTriples(String serviceName, Vector<String> categories, String namespaceURI, String _wsdlURI) throws RepositoryException {
 		logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getMSMURI("Service"));
		logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
				QueryHelper.getRDFSURI("isDefinedBy") + " , " + _wsdlURI);
		logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
				QueryHelper.getMSMEXTURI("wsdlDescription") + " , " + getDescriptionNode());
		logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
				QueryHelper.getRDFSURI("label") + " , " + serviceName);
		logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
				QueryHelper.getDCURI("creator") + " , " + "STI Innsbruck");
		
		for ( String category : categories ) {
			logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
					QueryHelper.getSAWSDLURI("modelReference") + " , " + category);
			
			reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getSAWSDLURI("modelReference"), category, _wsdlURI);
		}
		
		logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode(serviceName)  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Service"));
		logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode(serviceName)  + " , " + 
				QueryHelper.getRDFSURI("label") + " , " + serviceName);
		 
		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Description"));
		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
				QueryHelper.getWSDLURI("service") + " , " + getServiceNode(serviceName));
		logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + namespaceURI);

    	//Writing persistent
		reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getRDFURI("type"),  QueryHelper.getMSMURI("Service"), _wsdlURI);
		reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getRDFSURI("isDefinedBy"),  _wsdlURI, _wsdlURI);
		reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getMSMEXTURI("wsdlDescription"),  getDescriptionNode(), _wsdlURI);
		reposHandler.addLiteralTriple(SERVICE_NS + serviceName , QueryHelper.getRDFSURI("label"),  serviceName, _wsdlURI);
		reposHandler.addLiteralTriple(SERVICE_NS + serviceName , QueryHelper.getDCURI("creator"),  "STI Innsbruck", _wsdlURI);
	
		reposHandler.addResourceTriple(getServiceNode(serviceName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Service"), _wsdlURI);
		reposHandler.addLiteralTriple(getServiceNode(serviceName) , QueryHelper.getRDFSURI("label"),  serviceName, _wsdlURI);
		
		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Description"), _wsdlURI);
		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("service"),  getServiceNode(serviceName), _wsdlURI);
		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("namespace"),  namespaceURI, _wsdlURI);
 	}
 	
 	private static void writeInputPartToTriples(String inputName, String inputPartName, String namespaceURI, String _wsdlURI) throws RepositoryException {
 		logger.info("INPUT PART INFO : Triple + Context: " + SERVICE_NS + inputPartName + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("QName"));
 		logger.info("INPUT PART INFO : Triple + Context: " + SERVICE_NS + inputPartName + " , " + 
				QueryHelper.getWSDLURI("localName") + " , " + inputPartName);
 		logger.info("INPUT PART INFO : Triple + Context: " + SERVICE_NS + inputPartName + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + namespaceURI);
 		
 		logger.info("INPUT PART FOR INPUT : Triple + Context: " + SERVICE_NS + inputName + " , " + 
				QueryHelper.getWSDLURI("elementDeclaration") + " , " + SERVICE_NS + inputPartName);
 		
 		reposHandler.addResourceTriple(SERVICE_NS + inputPartName , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("QName"), _wsdlURI);
		reposHandler.addLiteralTriple(SERVICE_NS + inputPartName , QueryHelper.getWSDLURI("localName"), inputPartName, _wsdlURI);
		reposHandler.addResourceTriple(SERVICE_NS + inputPartName , QueryHelper.getWSDLURI("namespace"), namespaceURI, _wsdlURI);
		
		reposHandler.addResourceTriple(SERVICE_NS + inputName , QueryHelper.getWSDLURI("elementDeclaration"), SERVICE_NS + inputPartName, _wsdlURI);
	}
 	
 	private static void writeOutputPartToTriples(String outputName, String outputPartName, String namespaceURI, String _wsdlURI) throws RepositoryException {
 		logger.info("OUTPUT PART INFO : Triple + Context: " + SERVICE_NS + outputPartName + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("QName"));
 		logger.info("OUTPUT PART INFO : Triple + Context: " + SERVICE_NS + outputPartName + " , " + 
				QueryHelper.getWSDLURI("localName") + " , " + outputPartName);
 		logger.info("OUTPUT PART INFO : Triple + Context: " + SERVICE_NS + outputPartName + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + namespaceURI);
 		
 		logger.info("OUTPUT PART FOR OUTPUT : Triple + Context: " + SERVICE_NS + outputName + " , " + 
				QueryHelper.getWSDLURI("elementDeclaration") + " , " + SERVICE_NS + outputPartName);
 		
 		reposHandler.addResourceTriple(SERVICE_NS + outputPartName , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("QName"), _wsdlURI);
		reposHandler.addLiteralTriple(SERVICE_NS + outputPartName , QueryHelper.getWSDLURI("localName"), outputPartName, _wsdlURI);
		reposHandler.addResourceTriple(SERVICE_NS + outputPartName , QueryHelper.getWSDLURI("namespace"), namespaceURI, _wsdlURI);
		
		reposHandler.addResourceTriple(SERVICE_NS + outputName , QueryHelper.getWSDLURI("elementDeclaration"), SERVICE_NS + outputPartName, _wsdlURI);
	}

	private static void writeInputToTriples(String interfaceName, String interfaceOperationName, String inputName, URI loweringURI, String _wsdlURI) throws RepositoryException {
 		logger.info("INPUT INFO : Triple + Context: " + SERVICE_NS + inputName  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InputMessage"));
 		logger.info("INPUT INFO : Triple + Context: " + SERVICE_NS + inputName  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceMessageReference"));
 		logger.info("INPUT INFO : Triple + Context: " + SERVICE_NS + inputName  + " , " + 
				QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , " + loweringURI);
 		
 		logger.info("INPUT FOR INTERFACE: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + SERVICE_NS + inputName);
 		
 		reposHandler.addResourceTriple(SERVICE_NS + inputName , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InputMessage"), _wsdlURI);
 		reposHandler.addResourceTriple(SERVICE_NS + inputName , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceMessageReference") , _wsdlURI);
		reposHandler.addResourceTriple(SERVICE_NS + inputName , QueryHelper.getSAWSDLURI("loweringSchemaMapping"), loweringURI.toString(), _wsdlURI);
		
		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName) , QueryHelper.getWSDLURI("interfaceMessageReference"), SERVICE_NS + inputName, _wsdlURI);
	}
 	
 	private static void writeOutputToTriples(String interfaceName, String interfaceOperationName, String outputName, URI liftingURI, String _wsdlURI) throws RepositoryException {
 		logger.info("OUTPUT INFO : Triple + Context: " + SERVICE_NS + outputName  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("OutputMessage"));
 		logger.info("OUTPUT INFO : Triple + Context: " + SERVICE_NS + outputName  + " , " + 
				QueryHelper.getSAWSDLURI("liftingSchemaMapping") + " , " + liftingURI);
 		
 		logger.info("OUTPUT FOR INTERFACE: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + SERVICE_NS + outputName);
 		
 		reposHandler.addResourceTriple(SERVICE_NS + outputName , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("OutputMessage"), _wsdlURI);
		reposHandler.addResourceTriple(SERVICE_NS + outputName , QueryHelper.getSAWSDLURI("liftingSchemaMapping"), liftingURI.toString(), _wsdlURI);
		
		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName) , QueryHelper.getWSDLURI("interfaceMessageReference"), SERVICE_NS + outputName, _wsdlURI);
	}

	
 	
 	private static void writeEndpointsToTriples(String serviceName, String endpointName, String endpointAddress, String _wsdlURI) throws RepositoryException {
 		logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Endpoint"));
		logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
				+ QueryHelper.getRDFSURI("label") + " , " + endpointName);
		logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
				+ QueryHelper.getWSDLURI("address") + " , " + endpointAddress);
		logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
				+ QueryHelper.getWSDLURI("useBinding") + " , " + getBindingNode(serviceName, endpointName));
		
		logger.info("SERVICE ENDPOINTS : Triple + Context: " + getServiceNode(serviceName) + " , " 
				+ QueryHelper.getWSDLURI("endpoint") + " , " + getEndpointNode(serviceName, endpointName));
		
		logger.info("DESCRIPTION ENDPOINTS : Triple + Context: " + getDescriptionNode() + " , " 
				+ QueryHelper.getWSDLURI("endpoint") + " , " + getEndpointNode(serviceName, endpointName));
		
		//Writing persistent
		reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Endpoint"), _wsdlURI);
		reposHandler.addLiteralTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getRDFSURI("label"),  endpointName, _wsdlURI);
		reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getWSDLURI("address"),  endpointAddress, _wsdlURI);
		reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getWSDLURI("useBinding"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
		
		reposHandler.addResourceTriple(getServiceNode(serviceName) , QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
		
		reposHandler.addResourceTriple(getDescriptionNode() ,QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
 	}
 	
 	private static void writeBindingsToTriples(String serviceName, String bindingName, String bindingType, String _wsdlURI) throws RepositoryException, URISyntaxException {
 		logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Binding"));
		logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + bindingType);
		logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
				+ QueryHelper.getRDFSURI("label") + " , " + bindingName);
		logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
				+ QueryHelper.getWSDLURI("binds") + " , " + getInterfaceNode(bindingName));
		logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
				+ QueryHelper.getWSDLURI("bindingFault") + " , " + "");
		
		logger.info("BINDINGS FOR DESCRIPTION: Triple + Context: " + getDescriptionNode() + " , " 
				+ QueryHelper.getWSDLURI("binding") + " , " + getBindingNode(serviceName, bindingName));

		//Writing persistent
		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Binding"), _wsdlURI);
//		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFURI("type"), bindingType, _wsdlURI);
		reposHandler.addLiteralTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFSURI("label"), bindingName, _wsdlURI);
		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("binds"), getInterfaceNode(bindingName), _wsdlURI);
		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("bindingFault"), SERVICE_NS, _wsdlURI);
		
		reposHandler.addResourceTriple(getDescriptionNode(), QueryHelper.getWSDLURI("binding"), getBindingNode(serviceName, bindingName), _wsdlURI);
 	}
 	
 	private static void writeBindingOperationsToTriples(String serviceName, String bindingName, String bindingOperationName, String soapActionURI, String _wsdlURI) throws RepositoryException {
 		logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(serviceName, bindingOperationName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("BindingOperation"));
		logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(serviceName, bindingOperationName) + " , " 
				+ QueryHelper.getWSOAPURI("action") + " , " + soapActionURI);
		
		logger.info("BINDINGS OPERATION FOR BINDING PARENT: Triple + Context: " + getBindingNode(serviceName, bindingOperationName) + " , " 
				+ QueryHelper.getWSDLURI("bindingOperation") + " , " + getBindingOperationNode(serviceName, bindingOperationName));
		
		//Writing persistent
		reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingOperationName), QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("BindingOperation"), _wsdlURI);
		reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingOperationName), QueryHelper.getWSDLURI("action"), soapActionURI, _wsdlURI);
				
		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("bindingOperation"), getBindingOperationNode(serviceName, bindingOperationName), _wsdlURI);
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
	
	private static String getInterfaceNode(String interfaceName) {
		return SERVICE_NS +"wsdl.interface(" + interfaceName + ")"; 
	}
	
	private static String getInterfaceOperationNode(String serviceName, String interfaceOperationName) {
		return SERVICE_NS +"wsdl.interfaceOperation(" + serviceName + "/" + interfaceOperationName + ")"; 
	}
	
	private static String getBindingNode(String serviceName, String bindingName ) {
		return SERVICE_NS +"wsdl.binding(" + serviceName + "/" + bindingName + ")"; 
	}
	
	private static String getBindingOperationNode(String serviceName, String bindingOperationName ) {
		return SERVICE_NS +"wsdl.bindingOperation(" + serviceName + "/" + bindingOperationName + ")"; 
	}
	
	private static String getEndpointNode(String serviceName, String bindingName ) {
		return SERVICE_NS +"wsdl.endpoint(" + serviceName + "/" + bindingName + ")"; 
	}
	
	private static String getServiceNode(String serviceName) {
		return SERVICE_NS +"wsdl.service(" + serviceName + ")"; 
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
}
