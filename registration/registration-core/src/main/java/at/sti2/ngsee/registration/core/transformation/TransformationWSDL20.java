/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.ngsee.registration.core.transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.openrdf.repository.RepositoryException;
import org.ow2.easywsdl.extensions.sawsdl.SAWSDLFactory;
import org.ow2.easywsdl.extensions.sawsdl.api.Binding;
import org.ow2.easywsdl.extensions.sawsdl.api.BindingOperation;
import org.ow2.easywsdl.extensions.sawsdl.api.Description;
import org.ow2.easywsdl.extensions.sawsdl.api.Endpoint;
import org.ow2.easywsdl.extensions.sawsdl.api.Input;
import org.ow2.easywsdl.extensions.sawsdl.api.InterfaceType;
import org.ow2.easywsdl.extensions.sawsdl.api.Operation;
import org.ow2.easywsdl.extensions.sawsdl.api.Output;
import org.ow2.easywsdl.extensions.sawsdl.api.SAWSDLReader;
import org.ow2.easywsdl.extensions.sawsdl.api.Service;
import org.ow2.easywsdl.extensions.sawsdl.api.Types;
import org.ow2.easywsdl.extensions.sawsdl.api.schema.Element;
import org.ow2.easywsdl.extensions.sawsdl.api.schema.Schema;
import org.ow2.easywsdl.schema.api.XmlException;
import org.xml.sax.SAXException;

import at.sti2.ngsee.registration.core.common.Config;
import at.sti2.ngsee.registration.core.common.XMLParsing;
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
	private static String NAMESPACE_URI;
	private static RepositoryHandler reposHandler;
	
	public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException, ParserConfigurationException, SAXException, DocumentException  {
//		transformWSDL("http://sesa.sti2.at/services/globalweather.sawsdl");
		transformWSDL("file:///home/koni/development/sti/wsdl-2.0-testcase/00-all.wsdl");
//		transformWSDL("file:///home/koni/sawsdl_2.0.wsdl");
	}
	
	public static String transformWSDL(String _wsdlURI) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException, ParserConfigurationException, SAXException, DocumentException {
				
		Config cfg = new Config();
		reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		
		// Read a SAWSDL description
		SAWSDLReader reader = SAWSDLFactory.newInstance().newSAWSDLReader();
		Description desc = reader.read(new URL(_wsdlURI));

		SAXReader content = new SAXReader();
		Document doc = content.read(_wsdlURI);
		
		//Extracting and saving the lifting and lowering Schemas
		HashMap<String, List<URI>> llMap = new HashMap<String, List<URI>>();
		Types types = desc.getTypes();
		List<Schema> schemas = types.getSchemas();
		for ( Schema schema : schemas ) {
			List<Element> elements = schema.getElements();
			
			for ( Element elem : elements ) {
				String elemID = elem.getQName().getPrefix() + ":" + elem.getQName().getLocalPart();
				
				List<URI> liftingSchemas = elem.getLiftingSchemaMapping();
				List<URI> loweringSchemas = elem.getLoweringSchemaMapping();
				
				if ( !liftingSchemas.isEmpty() )
					llMap.put(elemID, liftingSchemas);
				if ( !loweringSchemas.isEmpty() ) 
					llMap.put(elemID, loweringSchemas);
			}
		}
		
		//Writing general information about services in triples
		for( Service service : desc.getServices() ) {
			
			SERVICE_NAME = service.getQName().getLocalPart();
			SERVICE_NS = service.getQName().getNamespaceURI() + "#";
			NAMESPACE_URI = service.getQName().getNamespaceURI();
			
			List<URI> serviceCategories = service.getModelReference();
			
			System.out.println(service.getModelReference());
			
			writeServiceToTriples(serviceCategories, NAMESPACE_URI, _wsdlURI);
			
			//Writing end-points triples
			for(Endpoint endpoint : service.getEndpoints()) {
				String endpointName = endpoint.getName();
				String endpointAddress = endpoint.getAddress();
												
				writeEndpointsToTriples(endpointName, endpointAddress, _wsdlURI);
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
		for( InterfaceType wsdlInterface : desc.getInterfaces() ) {
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
				
				writeInputInterfaceMessageReferenceToTriples(interfaceName, interfaceOperationName, inputMsgLabelName, pattern, llMap.get(getElementName(doc, interfaceOperationName, "input")), _wsdlURI);
				
				//Output
				Output output = interfaceOperation.getOutput();
				QName outputMsgLabel = output.getMessageName();
				String outputMsgLabelName = null;
				if ( outputMsgLabel != null )
					outputMsgLabelName = outputMsgLabel.getLocalPart();
				
				writeOutputInterfaceMessageReferenceToTriples(interfaceName, interfaceOperationName, outputMsgLabelName, pattern, llMap.get(getElementName(doc, interfaceOperationName, "output")), _wsdlURI);

			}
		}
		if ( SERVICE_NS != null && SERVICE_NAME != null ){
			reposHandler.commit();
        	return SERVICE_NS + SERVICE_NAME;
		}
        return null;
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
 	
 	private static void writeInputInterfaceMessageReferenceToTriples(String interfaceName, String operationName, String messageLabel, String pattern, List<URI> loweringURIs, String _wsdlURI) throws RepositoryException {
 		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInputMessagePartNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InputMessage"));
 		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInputMessagePartNode(interfaceName, operationName, messageLabel) + " , " + 
 				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceMessageReference"));
 		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInputMessagePartNode(interfaceName, operationName, messageLabel) + " , " + 
 				QueryHelper.getWSDLURI("elementDeclaration") + " , " + pattern + "#" + messageLabel);
 		
 		for ( URI loweringURI : loweringURIs )
 			logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInputMessagePartNode(interfaceName, operationName, messageLabel)  + " , " + 
				QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , " + loweringURI);
 		
 		logger.info("INPUT INTERFACE MSG REF INFO FOR INPUT : Triple + Context: " + getInterfaceOperationNode(interfaceName, operationName) + " , " + 
				QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getInputMessagePartNode(interfaceName, operationName, messageLabel));
 		
 		
 		reposHandler.addResourceTriple(getInputMessagePartNode(interfaceName, operationName, messageLabel) , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("InputMessage"), _wsdlURI);
		reposHandler.addResourceTriple(getInputMessagePartNode(interfaceName, operationName, messageLabel) , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceMessageReference"), _wsdlURI);
		reposHandler.addResourceTriple(getInputMessagePartNode(interfaceName, operationName, messageLabel) , QueryHelper.getWSDLURI("messageLabel"),  pattern + "#" + messageLabel , _wsdlURI);
	
		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, operationName) , QueryHelper.getWSDLURI("interfaceMessageReference"), getInputMessagePartNode(interfaceName, operationName, messageLabel), _wsdlURI);
		
		for ( URI loweringURI : loweringURIs )
			reposHandler.addResourceTriple(getInputMessagePartNode(interfaceName, operationName, messageLabel) , QueryHelper.getSAWSDLURI("loweringSchemaMapping"), loweringURI.toString(), _wsdlURI);
	}
 	
 	private static void writeOutputInterfaceMessageReferenceToTriples(String interfaceName, String operationName, String messageLabel, String pattern, List<URI> liftingURIs, String _wsdlURI) throws RepositoryException, URISyntaxException {
 		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getOutputMessagePartNode(interfaceName, operationName, messageLabel) + " , " + 
 				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InputMessage"));
		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getOutputMessagePartNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceMessageReference"));
		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getOutputMessagePartNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getWSDLURI("elementDeclaration") + " , " + pattern + "#" + messageLabel);
		
		for ( URI liftingURI : liftingURIs )
			logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getOutputMessagePartNode(interfaceName, operationName, messageLabel)  + " , " + 
					QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , " + liftingURI);
		
		logger.info("INPUT INTERFACE MSG REF INFO FOR INPUT : Triple + Context: " + getInterfaceOperationNode(interfaceName, operationName) + " , " + 
			QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getOutputMessagePartNode(interfaceName, operationName, messageLabel));
		
		
		reposHandler.addResourceTriple(getOutputMessagePartNode(interfaceName, operationName, messageLabel) , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("OutputMessage"), _wsdlURI);
		reposHandler.addResourceTriple(getOutputMessagePartNode(interfaceName, operationName, messageLabel) , QueryHelper.getWSDLURI("messageLabel"),  pattern + "#" + messageLabel , _wsdlURI);

		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, operationName) , QueryHelper.getWSDLURI("interfaceMessageReference"), getOutputMessagePartNode(interfaceName, operationName, messageLabel), _wsdlURI);
	
		for ( URI liftingURI : liftingURIs )
			reposHandler.addResourceTriple(getOutputMessagePartNode(interfaceName, operationName, messageLabel) , QueryHelper.getSAWSDLURI("loweringSchemaMapping"), liftingURI.toString(), _wsdlURI);
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
		/**
		 * XXX: Fix this. SOAPAction could be also only a string or empty
		 */
		if ( soapActionURI != null ) 
			try {
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
	
	private static String getInputMessagePartNode(String interfaceName, String operationName, String messageLabel ) {
		return SERVICE_NS + "wsdl.interfaceMessageReference(" + interfaceName + "/" + operationName + "/" + messageLabel + ")"; 
	}
	
	private static String getOutputMessagePartNode(String interfaceName, String operation, String message) {
		return SERVICE_NS + "wsdl.interfaceMessageReference(" + interfaceName + "/" + operation + "/" + message + ")"; 
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
	
	private static String getElementName(Document doc, String interfaceOperationName, String nodeName) {
		Node operationNode = XMLParsing.getNodeFromAttr(doc, interfaceOperationName);
		Node inputNode = XMLParsing.getSubnode(operationNode, nodeName);
		return XMLParsing.getNodeAttribute(inputNode, "element");
	}
}
