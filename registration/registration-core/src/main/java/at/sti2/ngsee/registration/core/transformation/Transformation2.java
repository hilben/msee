package at.sti2.ngsee.registration.core.transformation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import com.ibm.wsdl.extensions.http.HTTPAddressImpl;
import com.ibm.wsdl.extensions.http.HTTPBindingImpl;
import com.ibm.wsdl.extensions.http.HTTPOperationImpl;
import com.ibm.wsdl.extensions.mime.MIMEMimeXmlImpl;
import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;
import com.ibm.wsdl.extensions.soap.SOAPBindingImpl;
import com.ibm.wsdl.extensions.soap.SOAPBodyImpl;
import com.ibm.wsdl.extensions.soap.SOAPOperationImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12AddressImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12BindingImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12BodyImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12OperationImpl;

import at.sti2.util.triplestore.QueryHelper;

import edu.uga.cs.lsdis.sawsdl.Definition;

public class Transformation2 {
	
	private static Logger logger = Logger.getLogger(Transformation2.class);
	private static String SERVICE_NS;
	private final static String SOAP_NS = "http://schemas.xmlsoap.org/soap/http";
	
	public static void main(String[] args) throws WSDLException, RepositoryException, IOException {
		transformWSDL("file:///home/koni/globalweather.asmx");
	}
	
 	private static void transformWSDL(String _wsdlURI) throws WSDLException, RepositoryException, IOException {
 		System.setProperty("javax.wsdl.factory.WSDLFactory", "edu.uga.cs.lsdis.sawsdl.impl.factory.WSDLFactoryImpl");
 		
 		WSDLReader wsdlReader = WSDLFactory.newInstance().newWSDLReader();
        Definition definition = (Definition) wsdlReader.readWSDL(_wsdlURI);
        
//      Config cfg = new Config();
//		RepositoryHandler reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
        
        //Service
        Map<?, ?> services = definition.getServices();
        for ( Object serviceKey : services.values() ) {
        	Service service = (Service) serviceKey;
        	String serviceName = service.getQName().getLocalPart();
        	String namespaceURI = service.getQName().getNamespaceURI();
        	SERVICE_NS = service.getQName().getNamespaceURI() + "#";
        	
        	writeServiceToTriples(serviceName,namespaceURI ,_wsdlURI);
        	
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
//        		writeEndpointsToTriples(serviceName, endpointName, endpointAddress, _wsdlURI);
        		
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
				
				//Interface (Port type)
				PortType portType = binding.getPortType();
				String interfaceName = portType.getQName().getLocalPart();
				

				if ( bindingType != null ) {
					// Write the endpoint, binding and interface triples only if there exists a SOAP one. 
//					writeEndpointsToTriples(serviceName, endpointName, endpointAddress, _wsdlURI); 
//					writeBindingsToTriples(serviceName, bindingName, bindingType, _wsdlURI);
//					writeInterfaceToTriples(interfaceName, _wsdlURI);
				}
				
				
				//Interface Operation ( Port type Operation)
        		List<?> operations = portType.getOperations();
        		for ( Object operationObj : operations ) {
        			Operation operation = (Operation) operationObj;
        			String interfaceOperationName = operation.getName();
        			
        			//Operation Input 
        			Input input = operation.getInput();
        			Output output = operation.getOutput();
        			Map<?, ?> faultsMap = operation.getFaults();
        			        			
//        			writeInterfaceOperationsToTriples(interfaceName, interfaceOperationName, input, output, _wsdlURI);
        			
//        			System.err.println(input);
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
            				//TODO: Feathers - HTTP Operations
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
        			
        			writeBindingOperationsToTriples(serviceName, bindingOperationName, soapActionURI, _wsdlURI);
        			
        			//Operation Inputs, Outputs and Faults
        			Operation oper = bindingOperation.getOperation();
        			Input input = oper.getInput();
        			Output output = oper.getOutput();
        			        			
        			Map<?, ?> faults = oper.getFaults();
        			for ( Object faultObj : faults.values() ) {
        				Fault fault = (Fault) faultObj;
        			}
        			
        			Message inputMessage = input.getMessage();
        			Message outputMessage = output.getMessage();
        			
        			Map<?, ?> inputParts = inputMessage.getParts();
        			for (Object inPartObj : inputParts.values()) {
        				Part inPart = (Part) inPartObj;
        			}
        			
        			Map<?, ?> outputParts = outputMessage.getParts();
        			for (Object outPartObj : outputParts.values()) {
        				Part outPart = (Part) outPartObj;
        			}
        		}
        	}
        }
        
//        System.out.println("========================");
        
//        Map portTypes = definition.getPortTypes();
//        for ( Object portTypeKey : portTypes.values() ) {
//        	System.out.println(((PortType)portTypeKey).getQName());
//        }

         //get and print the messages
//        Map messages = definition.getMessages();
//        for (Object key:messages.keySet()){
//            Message semanticMessage = definition.getSemanticMessage((QName)key);
//
////            System.out.println("Message QName ->" + semanticMessage.getQName());
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
 	
 	private static void writeServiceToTriples(String serviceName, String namespaceURI, String _wsdlURI) {
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
//		reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getRDFURI("type"),  QueryHelper.getMSMURI("Service"), _wsdlURI);
//		reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getRDFSURI("isDefinedBy"),  _wsdlURI, _wsdlURI);
//		reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getMSMEXTURI("wsdlDescription"),  getDescriptionNode(), _wsdlURI);
//		reposHandler.addLiteralTriple(SERVICE_NS + serviceName , QueryHelper.getRDFSURI("label"),  serviceName, _wsdlURI);
//		reposHandler.addLiteralTriple(SERVICE_NS + serviceName , QueryHelper.getDCURI("creator"),  "STI Innsbruck", _wsdlURI);
//	
//		reposHandler.addResourceTriple(getServiceNode(serviceName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Service"), _wsdlURI);
//		reposHandler.addLiteralTriple(getServiceNode(serviceName) , QueryHelper.getRDFSURI("label"),  serviceName, _wsdlURI);
//		
//		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Description"), _wsdlURI);
//		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("service"),  getServiceNode(serviceName), _wsdlURI);
//		reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("namespace"),  service.getQName().getNamespaceURI(), _wsdlURI);
 	}
 	
 	private static void writeEndpointsToTriples(String serviceName, String endpointName, String endpointAddress, String _wsdlURI) {
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
//		reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Endpoint"), _wsdlURI);
//		reposHandler.addLiteralTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getRDFSURI("label"),  endpointName, _wsdlURI);
//		reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getWSDLURI("address"),  endpointAddress, _wsdlURI);
//		reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getWSDLURI("useBinding"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
//		
//		reposHandler.addResourceTriple(getServiceNode(serviceName) , QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
//		
//		reposHandler.addResourceTriple(getDescriptionNode() ,QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
 	}
 	
 	private static void writeBindingsToTriples(String serviceName, String bindingName, String bindingType, String _wsdlURI) {
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
//		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Binding"), _wsdlURI);
//		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFURI("type"), binding.getTypeOfBinding().toString(), _wsdlURI);
//		reposHandler.addLiteralTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFSURI("label"), bindingName, _wsdlURI);
//		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("binds"), getInterfaceNode(bindingName), _wsdlURI);
//		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("bindingFault"), SERVICE_NS, _wsdlURI);
		
//		reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("binding"), getBindingNode(serviceName, bindingName), _wsdlURI);
 	}
 	
 	private static void writeBindingOperationsToTriples(String serviceName, String bindingOperationName, String soapActionURI, String _wsdlURI) {
 		logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(serviceName, bindingOperationName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("BindingOperation"));
		logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(serviceName, bindingOperationName) + " , " 
				+ QueryHelper.getWSDLURI("action") + " , " + SERVICE_NS + bindingOperationName);
		logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(serviceName, bindingOperationName) + " , " 
				+ QueryHelper.getWSOAPURI("action") + " , " + soapActionURI);
		
		logger.info("BINDINGS OPERATION FOR SERVICE PARENT: Triple + Context: " + getBindingNode(serviceName, bindingOperationName) + " , " 
				+ QueryHelper.getWSDLURI("bindingOperation") + " , " + getBindingOperationNode(serviceName, bindingOperationName));
		
		//Writing persistent
//				reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingOperationName), QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("BindingOperation"), _wsdlURI);
//				reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingOperationName), QueryHelper.getWSDLURI("action"), SERVICE_NS + bindingOperationName, _wsdlURI);
//				
//				reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingName), QueryHelper.getWSDLURI("bindingOperation"), getBindingOperationNode(serviceName, bindingOperationName), _wsdlURI);
 	}
 	
 	private static void writeInterfaceToTriples(String interfaceName, String _wsdlURI) {
 		logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Interface"));	
		logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
				+ QueryHelper.getRDFURI("label") + " , " + interfaceName);
		
		logger.info("INTERFACE FOR DESCRIPTION : Triple + Context: " + getDescriptionNode() + " , " 
				+ QueryHelper.getWSDLURI("interface") + " , " + getInterfaceNode(interfaceName));
		
		//Writing persistent
//		reposHandler.addResourceTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Interface"), _wsdlURI);
//		reposHandler.addLiteralTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("label"), interfaceName, _wsdlURI);
//		
//		reposHandler.addLiteralTriple(getDescriptionNode(), QueryHelper.getWSDLURI("interface"), getInterfaceNode(interfaceName), _wsdlURI);
 	}
 	
 	private static void writeInterfaceOperationsToTriples(String interfaceName, String interfaceOperationName, Input input, Output output, String _wsdlURI) {
 		logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceOperation"));
		logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getRDFSURI("label") + " , " + interfaceOperationName);
		logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getInputNode(interfaceName, interfaceOperationName));
		logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
				+ QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getOutputNode(interfaceName, interfaceOperationName));
		
		logger.info("INTERFACE OPERATION FOR PARENT: Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
				+ QueryHelper.getWSDLURI("interfaceOperation") + " , " + getInterfaceOperationNode(interfaceName, interfaceOperationName));
		
		//Writing persistent
//		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceOperation"), _wsdlURI);
//		reposHandler.addLiteralTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName), QueryHelper.getRDFURI("label"), interfaceOperationName, _wsdlURI);
//		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName), QueryHelper.getWSDLURI("interfaceMessageReference"), getInputNode(interfaceName, interfaceOperationName), _wsdlURI);
//		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName), QueryHelper.getWSDLURI("interfaceMessageReference"), getOutputNode(interfaceName, interfaceOperationName), _wsdlURI);
//		
//		reposHandler.addLiteralTriple(getInterfaceNode(interfaceName), QueryHelper.getWSDLURI("interfaceOperation"), getInterfaceOperationNode(interfaceName, interfaceOperationName), _wsdlURI);
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
	
	private static String getInputNode(String interfaceName, String interfaceOperationName) {
		return SERVICE_NS +"wsdl.input(" + interfaceName + "/" + interfaceOperationName + ")"; 
	}
	
	private static String getOutputNode(String interfaceName, String interfaceOperationName) {
		return SERVICE_NS +"wsdl.output(" + interfaceName + "/" + interfaceOperationName + ")"; 
	}
	
	private static boolean isSoapProtocol(String transportProtocol) {
		if ( transportProtocol.equalsIgnoreCase(SOAP_NS) )
			return true;
		return false;
	}

}
