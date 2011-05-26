package at.sti2.ngsee.registration.core.transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

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
	private static final String SESA_NS = "http://www.sti2.at/sesa/service/";
	private static final String SESA_INSTANCES_NS = "http://www.sti2.at/instances";
	private static String serviceName = null;
	
	public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException {
//		transformWSDL("file:///home/koni/globalweather.asmx");
		transformWSDL("http://www.webservicex.com/globalweather.asmx?WSDL");
//		transformWSDL("http://sigimera.networld.to:8080/services/sigimeraID?wsdl");
//		transformWSDL("http://iserve.kmi.open.ac.uk/page/documents/6a2eca65-64cb-4a5d-aa7d-93161f9c3bb9/new2.sawsdl");
	}

	public static void transformWSDL(String _wsdlURI) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException {
		
		Config cfg = new Config();
		RepositoryHandler reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		
		// Read a SAWSDL description
		SAWSDLReader reader = SAWSDLFactory.newInstance().newSAWSDLReader();
		Description desc = reader.read(new URL(_wsdlURI));
		
		
		//Writing general information about services in triples
		//TODO: DO NOT OVERWRITE THE SERVICE NAME 
		for( Service service : desc.getServices() ) {
			serviceName = service.getQName().getLocalPart();
			
			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
					QueryHelper.getRDFURI("type") + " , " + QueryHelper.getMSMURI("Service") + " + " +  _wsdlURI);
			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
					QueryHelper.getRDFSURI("isDefinedBy") + " , " + _wsdlURI);
			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
					QueryHelper.getRDFSURI("seeAlso") + " , " + getDescriptionNode()  + " , " + _wsdlURI);
			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
					QueryHelper.getRDFSURI("label") + " , " + serviceName  + " , " + _wsdlURI);
			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
					QueryHelper.getDCURI("creator") + " , " + "STI Innsbruck"  + " , " + _wsdlURI);
			
			logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode(serviceName)  + " , " + 
					QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Service")  + " , " + _wsdlURI);
			logger.info("WSDL SERVICE INFO : Triple + Context: " + getServiceNode(serviceName)  + " , " + 
					QueryHelper.getRDFSURI("label") + " , " + serviceName  + " , " + _wsdlURI);
			 
			logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
					QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Description")  + " , " + _wsdlURI);
			logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
					QueryHelper.getWSDLURI("service") + " , " + getServiceNode(serviceName)  + " , " + _wsdlURI);
			logger.info("DESCRIPTION SERVICE INFO: Triple + Context: " + getDescriptionNode()  + " , " + 
					QueryHelper.getWSDLURI("namespace") + " , " + service.getQName().getNamespaceURI()  + " , " + _wsdlURI);
			
			//Writing persistent
			reposHandler.addResourceTriple(SESA_NS + serviceName , QueryHelper.getRDFURI("type"),  QueryHelper.getMSMURI("Service"), _wsdlURI);
			reposHandler.addResourceTriple(SESA_NS + serviceName , QueryHelper.getRDFSURI("isDefinedBy"),  _wsdlURI, _wsdlURI);
			reposHandler.addResourceTriple(SESA_NS + serviceName , QueryHelper.getRDFSURI("seeAlso"),  getDescriptionNode(), _wsdlURI);
			reposHandler.addLiteralTriple(SESA_NS + serviceName , QueryHelper.getRDFSURI("label"),  serviceName, _wsdlURI);
			reposHandler.addLiteralTriple(SESA_NS + serviceName , QueryHelper.getDCURI("creator"),  "STI Innsbruck", _wsdlURI);

			reposHandler.addResourceTriple(getServiceNode(serviceName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Service"), _wsdlURI);
			reposHandler.addLiteralTriple(getServiceNode(serviceName) , QueryHelper.getRDFSURI("label"),  serviceName, _wsdlURI);
			
			reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Description"), _wsdlURI);
			reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("service"),  getServiceNode(serviceName), _wsdlURI);
			reposHandler.addResourceTriple(getDescriptionNode() , QueryHelper.getWSDLURI("namespace"),  service.getQName().getNamespaceURI(), _wsdlURI);
			
			

			//Writing modelReference triples
//			for ( URI model : service.getModelReference() ) {
//				reposHandler.addResourceTriple(SESA_NS + service.getQName().getLocalPart() , QueryHelper.getRDFURI("type"), QueryHelper.getMSMURI("Service"), _wsdlUrl);
				
//				logger.info("MODEL REFERENCE : Triple + Context: " + SESA_NS + service.getQName().getLocalPart()  
//						+ " , " + QueryHelper.getMSMURI("type") + " , " + QueryHelper.getMSMURI("Service") + " + " +  _wsdlURI);
//			}
			
			//Writing end-points triples
			for(Endpoint endpoint : service.getEndpoints()) {
				String endpointName = endpoint.getName();
				String endpointAddress = endpoint.getAddress();
				
				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
						+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Endpoint") + " + " +  _wsdlURI);
				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
						+ QueryHelper.getRDFSURI("label") + " , " + endpointName + " + " +  _wsdlURI);
				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
						+ QueryHelper.getWSDLURI("address") + " , " + endpointAddress + " + " +  _wsdlURI);
				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
						+ QueryHelper.getWSDLURI("useBinding") + " , " + getEndpointNode(serviceName, endpointName) + " + " +  _wsdlURI);
				
				logger.info("SERVICE ENDPOINTS : Triple + Context: " + getServiceNode(serviceName) + " , " 
						+ QueryHelper.getWSDLURI("endpoint") + " , " + getEndpointNode(serviceName, endpointName) + " + " +  _wsdlURI);
				
				logger.info("DESCRIPTION ENDPOINTS : Triple + Context: " + getDescriptionNode() + " , " 
						+ QueryHelper.getWSDLURI("endpoint") + " , " + getEndpointNode(serviceName, endpointName) + " + " +  _wsdlURI);
				
				//Writing persistent
				reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getRDFURI("type"),  QueryHelper.getWSDLURI("Endpoint"), _wsdlURI);
				reposHandler.addLiteralTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getRDFSURI("label"),  endpointName, _wsdlURI);
				reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getWSDLURI("address"),  endpointAddress, _wsdlURI);
				reposHandler.addResourceTriple(getEndpointNode(serviceName, endpointName) , QueryHelper.getWSDLURI("useBinding"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
				
				reposHandler.addResourceTriple(getServiceNode(serviceName) , QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
				
				reposHandler.addResourceTriple(getDescriptionNode() ,QueryHelper.getWSDLURI("endpoint"),  getEndpointNode(serviceName, endpointName), _wsdlURI);
			}
			logger.info("==================================");
		}
		
		//Writing bindings triples
		for ( Binding binding : desc.getBindings() ) {
			String bindingName = binding.getQName().getLocalPart();

			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
					+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Binding") + " + " +  _wsdlURI);
			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
					+ QueryHelper.getRDFURI("type") + " , " + binding.getTypeOfBinding() + " + " +  _wsdlURI);
			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
					+ QueryHelper.getRDFSURI("label") + " , " + bindingName + " + " +  _wsdlURI);
			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
					+ QueryHelper.getWSDLURI("binds") + " , " + getInterfaceNode(bindingName) + " + " +  _wsdlURI);
			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
					+ QueryHelper.getWSDLURI("bindingFault") + " , " + "" + " + " +  _wsdlURI);
			
			//Writing persistent
			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Binding"), _wsdlURI);
			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFURI("type"), binding.getTypeOfBinding().toString(), _wsdlURI);
			reposHandler.addLiteralTriple(getBindingNode(serviceName, bindingName), QueryHelper.getRDFSURI("label"), bindingName, _wsdlURI);
			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("binds"), getInterfaceNode(bindingName), _wsdlURI);
			reposHandler.addResourceTriple(getBindingNode(serviceName, bindingName), QueryHelper.getWSDLURI("bindingFault"), SESA_INSTANCES_NS, _wsdlURI);
			
			for ( BindingOperation bindingOperation : binding.getBindingOperations() ) {
				String bindingOperationName = bindingOperation.getQName().getLocalPart();
				
				logger.info("BINDINGS OPERATION: Triple + Context: " + getBindingOperationNode(serviceName, bindingName) + " , " 
						+ QueryHelper.getWSDLURI("bindingOperation") + " , " + bindingOperationName + " + " +  _wsdlURI);
				
				//Writing persistent
				reposHandler.addResourceTriple(getBindingOperationNode(serviceName, bindingName), QueryHelper.getWSDLURI("bindingOperation"), getBindingOperationNode(serviceName, bindingOperationName), _wsdlURI);
			}
			logger.info("==================================");	
		}
	
		//Parse Interface
		for(InterfaceType wsdlInterface : desc.getInterfaces()){
			String interfaceName = wsdlInterface.getQName().getLocalPart();
			
			logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
					+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Interface") + " + " +  _wsdlURI);	
			logger.info("INTERFACE : Triple + Context: " + getInterfaceNode(interfaceName) + " , " 
					+ QueryHelper.getRDFURI("label") + " , " + interfaceName + " + " +  _wsdlURI);
			
			//Writing persistent
			reposHandler.addResourceTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("Interface"), _wsdlURI);
			reposHandler.addLiteralTriple(getInterfaceNode(interfaceName), QueryHelper.getRDFURI("label"), interfaceName, _wsdlURI);
			
			for(Operation interfaceOperation : wsdlInterface.getOperations()){
				String interfaceOperationName = interfaceOperation.getQName().getLocalPart();
				
				logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(serviceName, interfaceOperationName) + " , " 
						+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceOperation") + " + " +  _wsdlURI);
				logger.info("INTERFACE OPERATION: Triple + Context: " + getInterfaceOperationNode(serviceName, interfaceOperationName) + " , " 
						+ QueryHelper.getRDFSURI("label") + " , " + interfaceOperationName + " + " +  _wsdlURI);
				
				//Writing persistent
				reposHandler.addResourceTriple(getInterfaceOperationNode(serviceName, interfaceOperationName), QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceOperation"), _wsdlURI);
				reposHandler.addLiteralTriple(getInterfaceOperationNode(serviceName, interfaceOperationName), QueryHelper.getRDFURI("label"), interfaceOperationName, _wsdlURI);
				
				
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
			logger.info("==================================");	
			
//			for ( URI model : wsdlInterface.getModelReference() ){
//				System.out.println("\t[MODEL REFERENCE]: " + model.getPort());
//			}
		}
		
//		AttrExtensions attributes = desc.getAttrExtensions();
//		if ( attributes != null ) {
//			AttrExtensions extensions = attributes.getAttrExtensions();
//			if ( extensions != null ) {
//				System.out.println("\t[MODEL REFERENCE]: " + extensions.getModelReference());
//			}
//		}
	}
	
	private static String getDescriptionNode() {
		return SESA_INSTANCES_NS + "#wsdl.description()"; 
	}
	
	private static String getInterfaceNode(String interfaceName) {
		return SESA_INSTANCES_NS +"#wsdl.interface(" + interfaceName + ")"; 
	}
	
	private static String getInterfaceOperationNode(String serviceName, String interfaceOperationName) {
		return SESA_INSTANCES_NS +"#wsdl.interfaceOperation(" + serviceName + "/" + interfaceOperationName + ")"; 
	}
	
	private static String getBindingNode(String serviceName, String bindingName ) {
		return SESA_INSTANCES_NS +"#wsdl.binding(" + serviceName + "/" + bindingName + ")"; 
	}
	
	private static String getBindingOperationNode(String serviceName, String bindingOperationName ) {
		return SESA_INSTANCES_NS +"#wsdl.binding(" + serviceName + "/" + bindingOperationName + ")"; 
	}
	
	private static String getEndpointNode(String serviceName, String bindingName ) {
		return SESA_INSTANCES_NS +"#wsdl.endpoint(" + serviceName + "/" + bindingName + ")"; 
	}
	
	private static String getServiceNode(String serviceName) {
		return SESA_INSTANCES_NS +"#wsdl.service(" + serviceName + ")"; 
	}
}
