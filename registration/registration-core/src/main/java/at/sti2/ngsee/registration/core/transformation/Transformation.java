package at.sti2.ngsee.registration.core.transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;
import org.ow2.easywsdl.extensions.sawsdl.SAWSDLFactory;
import org.ow2.easywsdl.extensions.sawsdl.api.Binding;
import org.ow2.easywsdl.extensions.sawsdl.api.BindingOperation;
import org.ow2.easywsdl.extensions.sawsdl.api.Description;
import org.ow2.easywsdl.extensions.sawsdl.api.Endpoint;
import org.ow2.easywsdl.extensions.sawsdl.api.InterfaceType;
import org.ow2.easywsdl.extensions.sawsdl.api.Operation;
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLReader;
import org.ow2.easywsdl.extensions.sawsdl.api.Service;
import org.ow2.easywsdl.schema.api.XmlException;

import at.sti2.ngsee.registration.core.common.Config;
import at.sti2.util.triplestore.QueryHelper;
import at.sti2.util.triplestore.RepositoryHandler;

public class Transformation {
	
	protected static Logger logger = Logger.getLogger(Transformation.class);
	private static String SERVICE_NS;
	
	public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException {
//		transformWSDL("file:///home/koni/globalweather.asmx");
		transformWSDL("http://www.webservicex.com/globalweather.asmx?WSDL");
//		transformWSDL("http://sigimera.networld.to:8080/services/sigimeraID?wsdl");
//		transformWSDL("http://iserve.kmi.open.ac.uk/page/documents/6a2eca65-64cb-4a5d-aa7d-93161f9c3bb9/new2.sawsdl");
	}

	public static void transformWSDL(String _wsdlURI) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException {
		
		HashMap<String, Service> endpointsMap = new HashMap<String, Service>();
		
		Config cfg = new Config();
		RepositoryHandler reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		
		// Read a SAWSDL description
		SAWSDLReader reader = SAWSDLFactory.newInstance().newSAWSDLReader();
		Description desc = reader.read(new URL(_wsdlURI));
		
		
		//Writing general information about services in triples
		for( Service service : desc.getServices() ) {
			
			String serviceName = service.getQName().getLocalPart();
			SERVICE_NS = service.getQName().getNamespaceURI() + "#";
			
//			logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
//					QueryHelper.getRDFURI("type") + " , " + QueryHelper.getMSMURI("Service"));
//			logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
//					QueryHelper.getRDFSURI("isDefinedBy") + " , " + _wsdlURI);
//			logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
//					QueryHelper.getRDFSURI("seeAlso") + " , " + getDescriptionNode());
//			logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
//					QueryHelper.getRDFSURI("label") + " , " + serviceName);
//			logger.info("SERVICE INFO : Triple + Context: " + SERVICE_NS + serviceName  + " , " + 
//					QueryHelper.getDCURI("creator") + " , " + "STI Innsbruck");
//			
//			logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode(serviceName)  + " , " + 
//					QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Service"));
//			logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode(serviceName)  + " , " + 
//					QueryHelper.getRDFSURI("label") + " , " + serviceName);
//			 
//			logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
//					QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Description"));
//			logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
//					QueryHelper.getWSDLURI("service") + " , " + getServiceNode(serviceName));
//			logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
//					QueryHelper.getWSDLURI("namespace") + " , " + service.getQName().getNamespaceURI());
			
			//Writing persistent
			reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getRDFURI("type"),  QueryHelper.getMSMURI("Service"), _wsdlURI);
			reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getRDFSURI("isDefinedBy"),  _wsdlURI, _wsdlURI);
			reposHandler.addResourceTriple(SERVICE_NS + serviceName , QueryHelper.getRDFSURI("seeAlso"),  getDescriptionNode(), _wsdlURI);
			reposHandler.addLiteralTriple(SERVICE_NS + serviceName , QueryHelper.getRDFSURI("label"),  serviceName, _wsdlURI);
			reposHandler.addLiteralTriple(SERVICE_NS + serviceName , QueryHelper.getDCURI("creator"),  "STI Innsbruck", _wsdlURI);

			reposHandler.addResourceTriple(getServiceNode(serviceName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Service"), _wsdlURI);
			reposHandler.addLiteralTriple(getServiceNode(serviceName) , QueryHelper.getRDFSURI("label"),  serviceName, _wsdlURI);
			
			reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Description"), _wsdlURI);
			reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("service"),  getServiceNode(serviceName), _wsdlURI);
			reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("namespace"),  service.getQName().getNamespaceURI(), _wsdlURI);
			
			//Writing end-points triples
			for(Endpoint endpoint : service.getEndpoints()) {
				String endpointName = endpoint.getName();
				String endpointAddress = endpoint.getAddress();
//				
//				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
//						+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Endpoint"));
//				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
//						+ QueryHelper.getRDFSURI("label") + " , " + endpointName);
//				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
//						+ QueryHelper.getWSDLURI("address") + " , " + endpointAddress);
//				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
//						+ QueryHelper.getWSDLURI("useBinding") + " , " + getBindingNode(serviceName, endpointName));
//				
//				logger.info("SERVICE ENDPOINTS : Triple + Context: " + getServiceNode(serviceName) + " , " 
//						+ QueryHelper.getWSDLURI("endpoint") + " , " + getEndpointNode(serviceName, endpointName));
//				
//				logger.info("DESCRIPTION ENDPOINTS : Triple + Context: " + getDescriptionNode() + " , " 
//						+ QueryHelper.getWSDLURI("endpoint") + " , " + getEndpointNode(serviceName, endpointName));
				
				//Writing persistent
				reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Endpoint"), _wsdlURI);
				reposHandler.addLiteralTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getRDFSURI("label"),  endpointName, _wsdlURI);
				reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getWSDLURI("address"),  endpointAddress, _wsdlURI);
				reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getWSDLURI("useBinding"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
				
				reposHandler.addResourceTriple(getServiceNode(serviceName) , QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
				
				reposHandler.addResourceTriple(getDescriptionNode() ,QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
				
				endpointsMap.put(endpointName, service);
			}
		}
		
		//Writing bindings triples
		for ( Binding binding : desc.getBindings() ) {
			String bindingName = binding.getQName().getLocalPart();
			Service service = endpointsMap.get(bindingName);
			String serviceName = service.getQName().getLocalPart(); 

//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Binding"));
//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getRDFURI("type") + " , " + binding.getTypeOfBinding());
//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getRDFSURI("label") + " , " + bindingName);
//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getWSDLURI("binds") + " , " + getInterfaceNode(bindingName));
//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getWSDLURI("bindingFault") + " , " + "");
//			
//			logger.info("BINDINGS FOR DESCRIPTION: Triple + Context: " + getDescriptionNode() + " , " 
//					+ QueryHelper.getWSDLURI("binding") + " , " + getBindingNode(serviceName, bindingName));
//			
//			//Writing persistent
			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Binding"), _wsdlURI);
			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFURI("type"), binding.getTypeOfBinding().toString(), _wsdlURI);
			reposHandler.addLiteralTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFSURI("label"), bindingName, _wsdlURI);
			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("binds"), getInterfaceNode(bindingName), _wsdlURI);
//			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("bindingFault"), SERVICE_NS, _wsdlURI);
			
			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("binding"), getBindingNode(serviceName, bindingName), _wsdlURI);
			
			for ( BindingOperation bindingOperation : binding.getBindingOperations() ) {
				String bindingOperationName = bindingOperation.getQName().getLocalPart();
				
				//TODO: Check only for SOAP Binding 
				
//				logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(serviceName, bindingOperationName) + " , " 
//						+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("BindingOperation"));
//				logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(serviceName, bindingOperationName) + " , " 
//						+ QueryHelper.getWSDLURI("action") + " , " + SERVICE_NS + bindingOperationName);
//				
//				logger.info("BINDINGS OPERATION FOR SERVICE PARENT: Triple + Context: " + getBindingNode(serviceName, bindingOperationName) + " , " 
//						+ QueryHelper.getWSDLURI("bindingOperation") + " , " + getBindingOperationNode(serviceName, bindingOperationName));
				
				//Writing persistent
				reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingOperationName), QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("BindingOperation"), _wsdlURI);
				reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingOperationName), QueryHelper.getWSDLURI("action"), SERVICE_NS + bindingOperationName, _wsdlURI);
				
				reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingName), QueryHelper.getWSDLURI("bindingOperation"), getBindingOperationNode(serviceName, bindingOperationName), _wsdlURI);
			}
		}
	
		//Parse Interface
		for(InterfaceType wsdlInterface : desc.getInterfaces()){
			String interfaceName = wsdlInterface.getQName().getLocalPart();
			
			//TODO: Check only for SOAP Binding 
			
//			logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
//					+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Interface"));	
//			logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
//					+ QueryHelper.getRDFURI("label") + " , " + interfaceName);
//			
//			logger.info("INTERFACE FOR DESCRIPTION : Triple + Context: " + getDescriptionNode() + " , " 
//					+ QueryHelper.getWSDLURI("interface") + " , " + getInterfaceNode(interfaceName));
			
			//Writing persistent
			reposHandler.addResourceTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Interface"), _wsdlURI);
			reposHandler.addLiteralTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("label"), interfaceName, _wsdlURI);
			
			reposHandler.addLiteralTriple(getDescriptionNode(), QueryHelper.getWSDLURI("interface"), getInterfaceNode(interfaceName), _wsdlURI);
			
			
			for(Operation interfaceOperation : wsdlInterface.getOperations()){
				String interfaceOperationName = interfaceOperation.getQName().getLocalPart();
				
//				logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
//						+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceOperation"));
//				logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(interfaceName, interfaceOperationName) + " , " 
//						+ QueryHelper.getRDFSURI("label") + " , " + interfaceOperationName);
//				
//				logger.info("INTERFACE OPERATION FOR PARENT: Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
//						+ QueryHelper.getWSDLURI("interfaceOperation") + " , " + getInterfaceOperationNode(interfaceName, interfaceOperationName));
				
				//Writing persistent
				reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceOperation"), _wsdlURI);
				reposHandler.addLiteralTriple(getInterfaceOperationNode(interfaceName, interfaceOperationName), QueryHelper.getRDFURI("label"), interfaceOperationName, _wsdlURI);
				
				reposHandler.addLiteralTriple(getInterfaceNode(interfaceName), QueryHelper.getWSDLURI("interfaceOperation"), getInterfaceOperationNode(interfaceName, interfaceOperationName), _wsdlURI);
				
				
//				Input input = operation.getInput();
//				System.out.println("\t\t\t[INPUT NAME]: "+input.getName());
//				
//				for ( URI model : input.getModelReference() ){
//					System.out.println("\t[MODEL REFERENCE]: " + model.getHost());
//				}
//				
//				for( Part part  : input.getParts()){
//					System.out.println("\t\t\t\t[PART NAME]: "+part.getPartQName());
//
//					Documentation element = part.getDocumentation();
//					if ( element != null )
//						System.out.println("    element name "+element.getContent());
//				}
			}
		}
		reposHandler.shutdown();
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
}
