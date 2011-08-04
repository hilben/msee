package at.sti2.ngsee.registration.core.transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

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
import org.ow2.easywsdl.extensions.sawsdl.api.Types;
import org.ow2.easywsdl.extensions.sawsdl.api.schema.Element;
import org.ow2.easywsdl.extensions.sawsdl.api.schema.Schema;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding.BindingConstants;

import at.sti2.ngsee.registration.core.common.Config;
import at.sti2.util.triplestore.QueryHelper;
import at.sti2.util.triplestore.RepositoryHandler;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Corneliu Stanciu<br>
 * @version     $Id$<br>
 * Date of creation:  13.04.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public class TransformationWSDL20 {
	
	private static Logger logger = Logger.getLogger(TransformationWSDL11.class);
	private static String SERVICE_NS;
	private static String SERVICE_NAME;
	private static RepositoryHandler reposHandler;
	private final static String SOAP_NS = "http://schemas.xmlsoap.org/soap/http";
	
	public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException  {
//		transformWSDL("http://www.w3.org/2002/ws/sawsdl/spec/wsdl/order#");
//		transformWSDL("http://www.w3.org/2002/ws/sawsdl/spec/wsdl/order11");
//		transformWSDL("http://www.webservicex.com/globalweather.asmx?WSDL");
//		transformWSDL("http://sigimera.networld.to:8080/services/sigimeraID?wsdl");
//		transformWSDL("http://iserve.kmi.open.ac.uk/page/documents/6a2eca65-64cb-4a5d-aa7d-93161f9c3bb9/new2.sawsdl");
//		transformWSDL("http://sesa.sti2.at/services/globalweather.sawsdl");
		transformWSDL("file:///home/koni/development/sti/wsdl-2.0-testcase/00-all.wsdl");
//		transformWSDL("file:///home/koni/sawsdl_2.0.wsdl");
//		transformWSDL("file:///tmp/xslt");
	}
	
	public static void transformWSDL(String _wsdlURI) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException {
				
		Config cfg = new Config();
		reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		
		// Read a SAWSDL description
		SAWSDLReader reader = SAWSDLFactory.newInstance().newSAWSDLReader();
		Description desc = reader.read(new URL(_wsdlURI));
		
		
		Types types = desc.getTypes();
		List<Schema> schemas = types.getSchemas();
		for ( Schema schema : schemas ) {
			List<Element> elements = schema.getElements();
			
			for ( Element elem : elements ) {
				System.out.println(elem.getLiftingSchemaMapping());
				System.out.println(elem.getLoweringSchemaMapping());
			}
		}
		
		//Writing general information about services in triples
		for( Service service : desc.getServices() ) {
			
			SERVICE_NAME = service.getQName().getLocalPart();
			SERVICE_NS = service.getQName().getNamespaceURI() + "#";
			String namespaceURI = service.getQName().getNamespaceURI();
			
			List<URI> serviceCategories = service.getModelReference();
			
//			System.out.println(service.getModelReference());
			
//			writeServiceToTriples(serviceCategories, namespaceURI, _wsdlURI);
			
			//Writing end-points triples
			for(Endpoint endpoint : service.getEndpoints()) {
				String endpointName = endpoint.getName();
				String endpointAddress = endpoint.getAddress();
												
//				writeEndpointsToTriples(endpointName, endpointAddress, _wsdlURI);
			}
		}
		
		//Writing bindings triples
		for ( Binding binding : desc.getBindings() ) {
			String bindingName = binding.getQName().getLocalPart();
			String bindingType = binding.getTypeOfBinding().value().toString();
				
				writeBindingsToTriples(bindingName, bindingType, _wsdlURI);
				
				for ( BindingOperation bindingOperation : binding.getBindingOperations() ) {
					String bindingOperationName = bindingOperation.getQName().getLocalPart(); 
					String soapAction = bindingOperation.getSoapAction();

					writeBindingOperationsToTriples(bindingName, bindingOperationName, soapAction, _wsdlURI);
				}
//			}
		}
	
		//Parse Interface
		for(InterfaceType wsdlInterface : desc.getInterfaces()){
			String interfaceName = wsdlInterface.getQName().getLocalPart();
			
			
			for( Operation interfaceOperation : wsdlInterface.getOperations() ){
				String interfaceOperationName = interfaceOperation.getQName().getLocalPart();
				
			}
		}
//		reposHandler.shutdown();
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
	
	private static boolean isSoapProtocol(String transportProtocol) {
		if ( transportProtocol.equalsIgnoreCase(SOAP_NS) )
			return true;
		return false;
	}
}
