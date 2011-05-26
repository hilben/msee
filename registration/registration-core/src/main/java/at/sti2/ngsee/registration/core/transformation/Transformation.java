package at.sti2.ngsee.registration.core.transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
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
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLReader;
import org.ow2.easywsdl.extensions.sawsdl.api.Service;
import org.ow2.easywsdl.schema.api.XmlException;

import at.sti2.ngsee.registration.core.common.Config;
import at.sti2.util.triplestore.QueryHelper;
import at.sti2.util.triplestore.RepositoryHandler;

public class Transformation {
	
	protected static Logger logger = Logger.getLogger(Transformation.class);
	private static final String SESA_NS = "http://www.sti2.at/sesa/service/";
	private static String serviceName = null;
	
	public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException {
		transformWSDL("file:///home/koni/globalweather.asmx");
//		transformWSDL("http://www.webservicex.com/globalweather.asmx?WSDL");
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
			
//			reposHandler.addResourceTriple(SESA_NS + service.getQName().getLocalPart() , QueryHelper.getRDFURI("type"), QueryHelper.getMSMURI("Service"), _wsdlUrl);

//			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
//					QueryHelper.getRDFURI("type") + " , " + QueryHelper.getMSMURI("Service") + " + " +  _wsdlURI);
//			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
//					QueryHelper.getRDFSURI("isDefinedBy") + " , " + _wsdlURI);
//			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
//					QueryHelper.getRDFSURI("seeAlso") + " , " + getDescriptionNode()  + " , " + _wsdlURI);
//			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
//					QueryHelper.getRDFSURI("label") + " , " + service.getQName().getLocalPart()  + " , " + _wsdlURI);
//			logger.info("SERVICE INFO : Triple + Context: " + SESA_NS + serviceName  + " , " + 
//					QueryHelper.getDCURI("creator") + " , " + "STI Innsbruck"  + " , " + _wsdlURI);
			
			//Writing modelReference triples
			for ( URI model : service.getModelReference() ) {
//				reposHandler.addResourceTriple(SESA_NS + service.getQName().getLocalPart() , QueryHelper.getRDFURI("type"), QueryHelper.getMSMURI("Service"), _wsdlUrl);
				
				logger.info("MODEL REFERENCE : Triple + Context: " + SESA_NS + service.getQName().getLocalPart()  
						+ " , " + QueryHelper.getMSMURI("type") + " , " + QueryHelper.getMSMURI("Service") + " + " +  _wsdlURI);
			}
			
			//Writing end-points triples
			for(Endpoint endpoint : service.getEndpoints()) {
				String endpointName = endpoint.getName();
				String endpointAddress = endpoint.getAddress();
				
//				reposHandler.addResourceTriple(SESA_NS + service.getQName().getLocalPart() ,
//						QueryHelper.getRDFURI("type"), QueryHelper.getMSMURI("Service"), _wsdlURI);
				
//				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
//						+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Endpoint") + " + " +  _wsdlURI);
//				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
//						+ QueryHelper.getRDFSURI("label") + " , " + endpointName + " + " +  _wsdlURI);
//				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
//						+ QueryHelper.getWSDLURI("address") + " , " + endpointAddress + " + " +  _wsdlURI);
//				logger.info("ENDPOINTS : Triple + Context: " + getEndpointNode(serviceName, endpointName) + " , " 
//						+ QueryHelper.getWSDLURI("useBinding") + " , " + getEndpointNode(serviceName, endpointName) + " + " +  _wsdlURI);
			}
		}
		
		//Writing bindings triples
		for ( Binding binding : desc.getBindings() ) {
			String bindingName = binding.getQName().getLocalPart();
			
//			reposHandler.addResourceTriple(SESA_NS + service.getQName().getLocalPart() ,
//					QueryHelper.getRDFURI("type"), QueryHelper.getMSMURI("Service"), _wsdlURI);

//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("Binding") + " + " +  _wsdlURI);
//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getRDFURI("type") + " , " + binding.getTypeOfBinding() + " + " +  _wsdlURI);
//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getRDFSURI("label") + " , " + bindingName + " + " +  _wsdlURI);
//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getWSDLURI("binds") + " , " + getInterfaceNode(bindingName) + " + " +  _wsdlURI);
//			logger.info("BINDINGS : Triple + Context: " + getBindingNode(serviceName, bindingName) + " , " 
//					+ QueryHelper.getWSDLURI("bindingFault") + " , " + "" + " + " +  _wsdlURI);
//			logger.info("==================================");	
			
			System.out.println("\t[BINDING OPERATION]: " + binding.getTransportProtocol());
			
//			for ( BindingOperation bindingOperation : binding.getBindingOperations() ) {
//				System.out.println("\t[BINDING OPERATION]: " + bindingOperation.getSoapAction());
//			}
		}
//		
//		AttrExtensions attributes = desc.getAttrExtensions();
//		if ( attributes != null )
//			for ( URI model : attributes.getModelReference() ){
//				System.out.println("\t[MODEL REFERENCE]: " + model.getHost());
//			}
//		
//		
//		//Parse Interface
//		for(InterfaceType wsdlInterface : desc.getInterfaces()){
//			System.out.println("\t[INTERFACE]: "+wsdlInterface.getQName());
//		}
//			
//			
//			for ( URI model : wsdlInterface.getModelReference() ){
//				System.out.println("\t[MODEL REFERENCE]: " + model.getHost());
//			}
//			
//			for(Operation operation : wsdlInterface.getOperations()){
//				System.out.println("\t\t[OPERATION NAME]: " + operation.getQName());
//				
//				AttrExtensions attributesO = desc.getAttrExtensions();
//				if ( attributesO != null )
//					for ( URI model1 : attributesO.getModelReference() ){
//						System.out.println("\t[MODEL REFERENCE]: " + model1.getHost());
//					}
//				
//				for ( URI model : operation.getModelReference() ){
//					System.out.println("\t[MODEL REFERENCE]: " + model.getHost());
//				}
//				
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
//			}
//		}
	}
	
	private static String getDescriptionNode() {
		return "#wsdl.description("; 
	}
	
	private static String getInterfaceNode(String string) {
		return "#wsdl.interface(" + string + ")"; 
	}
	
	private static String getInterfaceOperationNode(String string) {
		return "#wsdl.interfaceOperation(" + string + ")"; 
	}
	
	private static String getBindingNode(String serviceName, String endpointName ) {
		return "#wsdl.binding(" + serviceName + "/" + endpointName + ")"; 
	}
	
	private static String getEndpointNode(String serviceName, String bindingName ) {
		return "#wsdl.endpoint(" + serviceName + "/" + bindingName + ")"; 
	}
}
