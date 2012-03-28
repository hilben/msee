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
import org.ow2.easywsdl.wsdl.api.Part;
import org.xml.sax.SAXException;

import at.sti2.ngsee.registration.core.common.Config;
import at.sti2.ngsee.registration.core.common.WSDLRecogniser;
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
	
	private static Logger logger = Logger.getLogger(TransformationWSDL20.class);
	private static String SERVICE_NS;
	private static String SERVICE_NAME;
	private static String NAMESPACE_URI;
	private static RepositoryHandler reposHandler;
	
	public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException, ParserConfigurationException, SAXException, DocumentException  {
//		transformWSDL("file:///home/koni/globalweather.sawsdl");
//		transformWSDL("file:///home/koni/sawsdl_2.0.wsdl");
		transformWSDL("file:///home/koni/development/sti/wsdl-2.0-testcase/00-all.wsdl");
	}
	
	private static void checkWSDLVersion(Document _doc) {
		WSDLRecogniser wsdlVersion = new WSDLRecogniser(_doc);
		if ( wsdlVersion.isWSDL11() ){
			System.err.println("WSDL 1.0");
		}
		else if ( wsdlVersion.isWSDL20() ){
			System.err.println("WSDL 2.0");
		}
	}
	
	public static String transformWSDL(String _wsdlURI) throws MalformedURLException, IOException, URISyntaxException, XmlException, RepositoryException, ParserConfigurationException, SAXException, DocumentException {
		
		Config cfg = new Config();
		reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		
		// Read a SAWSDL description
		SAWSDLReader reader = SAWSDLFactory.newInstance().newSAWSDLReader();
		Description desc = reader.read(new URL(_wsdlURI));
		
		SAXReader content = new SAXReader();
		Document doc = content.read(_wsdlURI);
		
//		System.err.println(desc.getVersion());
//		checkWSDLVersion(doc);
		
		//Extracting elements 
		HashMap<String, Element> elementsMap = new HashMap<String, Element>();
		Types types = desc.getTypes();
		List<Schema> schemas = types.getSchemas();
		for ( Schema schema : schemas ) {
			List<Element> elements = schema.getElements();
			
			for ( Element elem : elements ) {
				String elemID = elem.getQName().getPrefix() + ":" + elem.getQName().getLocalPart();
				elementsMap.put(elemID, elem);
				System.err.println("ELEM: " + elem.getQName());
			}
		}
		
		//Writing general information about services in triples
		for( Service service : desc.getServices() ) {
			
			SERVICE_NAME = service.getQName().getLocalPart();
			SERVICE_NS = service.getQName().getNamespaceURI() + "#";
			NAMESPACE_URI = service.getQName().getNamespaceURI();
			
			List<URI> serviceCategories = service.getModelReference();			
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
				
				List<Part> parts = input.getParts();
				System.err.println(input.getElement());
//				for ( Part part : parts )
//					System.err.println(part.getPartQName());

								
//				System.err.println(getElementNameFromWSDL11(doc, inputMsgLabelName, "wsdl:part"));
				
//				Element elemIn = elementsMap.get(getElementNameFromWSDL20(doc, interfaceName, interfaceOperationName, "input"));
//				List<URI> loweringSchemas = elemIn.getLoweringSchemaMapping();
//				List<URI> inputParamModelReference = elemIn.getModelReference();
//				
//				writeElementDeclaration(elemIn.getQName(), interfaceName, interfaceOperationName, inputMsgLabelName, inputParamModelReference, _wsdlURI);
//				writeInputInterfaceMessageReferenceToTriples(interfaceName, interfaceOperationName, inputMsgLabelName, pattern, loweringSchemas, _wsdlURI);
//				
//				//Output
//				Output output = interfaceOperation.getOutput();
//				QName outputMsgLabel = output.getMessageName();
//				String outputMsgLabelName = null;
//				if ( outputMsgLabel != null )
//					outputMsgLabelName = outputMsgLabel.getLocalPart();
//				
//				Element elemOut = elementsMap.get(getElementName(doc, interfaceOperationName, "output"));
//				List<URI> liftingSchemas = elemOut.getLiftingSchemaMapping();
//				List<URI> outputParamModelReference = elemOut.getModelReference();
//				
//				writeElementDeclaration(elemOut.getQName(), interfaceName, interfaceOperationName, outputMsgLabelName, outputParamModelReference, _wsdlURI);
//				writeOutputInterfaceMessageReferenceToTriples(interfaceName, interfaceOperationName, outputMsgLabelName, pattern, liftingSchemas, _wsdlURI);

			}
		}
		if ( SERVICE_NS != null && SERVICE_NAME != null ){
//			reposHandler.commit();
        	return SERVICE_NS + SERVICE_NAME;
		}
        return null;
	}
	
	private static void writeElementDeclaration(QName elementQName, String interfaceName, String operationName, String messageLabel, List<URI> modelReferences, String _wsdlURI) throws RepositoryException {
		logger.info("ELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart())  + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("QName"));
		logger.info("ELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart())  + " , " + 
				QueryHelper.getWSDLURI("localName") + " , " + elementQName.getLocalPart() );
		logger.info("ELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart())  + " , " + 
				QueryHelper.getWSDLURI("namespace") + " , " + elementQName.getNamespaceURI());
		
		logger.info("ELEMENT DECL FOR INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getWSDLURI("elementDeclaration") + " , " + getElementDeclarationNode(elementQName.getLocalPart()));
		
		for ( URI modelReferece : modelReferences) {
			logger.info("ELEMENT DECL INFO : Triple + Context: " + getElementDeclarationNode(elementQName.getLocalPart())  + " , " + 
					QueryHelper.getSAWSDLURI("modelReference") + " , " + modelReferece);
			
			reposHandler.addResourceTriple(getElementDeclarationNode(elementQName.getLocalPart()), QueryHelper.getSAWSDLURI("modelReference"), modelReferece.toString(), _wsdlURI);
		}
		
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
 		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InputMessage"));
 		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
 				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceMessageReference"));
 		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
 				QueryHelper.getWSDLURI("messageLabel") + " , " + pattern + "#" + messageLabel);
 		
 		for ( URI loweringURI : loweringURIs ) {
 			logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel)  + " , " + 
				QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , " + loweringURI);
 			
 			reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getSAWSDLURI("loweringSchemaMapping"), loweringURI.toString(), _wsdlURI);
 		}
 		
 		logger.info("INPUT INTERFACE MSG REF INFO FOR INPUT : Triple + Context: " + getInterfaceOperationNode(interfaceName, operationName) + " , " + 
				QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel));
 		
 		
 		reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("InputMessage"), _wsdlURI);
		reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getRDFURI("type"), QueryHelper.getWSDLURI("InterfaceMessageReference"), _wsdlURI);
		reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getWSDLURI("messageLabel"),  pattern + "#" + messageLabel , _wsdlURI);
	
		reposHandler.addResourceTriple(getInterfaceOperationNode(interfaceName, operationName) , QueryHelper.getWSDLURI("interfaceMessageReference"), getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel), _wsdlURI);
	}
 	
 	private static void writeOutputInterfaceMessageReferenceToTriples(String interfaceName, String operationName, String messageLabel, String pattern, List<URI> liftingURIs, String _wsdlURI) throws RepositoryException, URISyntaxException {
 		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
 				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InputMessage"));
		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getRDFURI("type") + " , " + QueryHelper.getWSDLURI("InterfaceMessageReference"));
		logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) + " , " + 
				QueryHelper.getWSDLURI("messageLabel") + " , " + pattern + "#" + messageLabel);
		
		
		for ( URI liftingURI : liftingURIs ) {
			logger.info("INPUT INTERFACE MSG REF INFO : Triple + Context: " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel)  + " , " + 
					QueryHelper.getSAWSDLURI("loweringSchemaMapping") + " , " + liftingURI);
		
			reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getSAWSDLURI("liftingSchemaMapping"), liftingURI.toString(), _wsdlURI);
		}
		
		logger.info("INPUT INTERFACE MSG REF INFO FOR INPUT : Triple + Context: " + getInterfaceOperationNode(interfaceName, operationName) + " , " + 
			QueryHelper.getWSDLURI("interfaceMessageReference") + " , " + getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel));
		
		
		reposHandler.addResourceTriple(getInterfaceMessageReferenceNode(interfaceName, operationName, messageLabel) , QueryHelper.getRDFURI("type"),   QueryHelper.getWSDLURI("OutputMessage"), _wsdlURI);
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
	
	private static String getElementNameFromWSDL20(Document doc, String interfaceName, String interfaceOperationName, String nodeName) {
		Node interfaceNode = XMLParsing.getNodeFromAttr(doc, interfaceName);
		System.err.println("INTERFACE NODE: " + interfaceNode.getPath() + " --- " + interfaceName + " --- " + interfaceOperationName);
		Node operationNode = XMLParsing.getSubnodeFromAttribute(interfaceNode, interfaceOperationName);
		System.err.println("OPERATION NODE: " + operationNode.getPath() + " --- " + interfaceOperationName);
		Node inputNode = XMLParsing.getSubnode(operationNode, nodeName);
		System.err.println("INPUT NODE: " + inputNode.getPath() + " --- " + nodeName);
		return XMLParsing.getNodeAttribute(inputNode, "element");
	}
	
	private static String getElementNameFromWSDL11(Document doc, String messageName, String nodeName) throws URISyntaxException {
		Node messageNode = XMLParsing.getNodeFromAttr(doc, messageName);
		System.err.println("INPUT NODE: " + messageNode.getName() + " --- " + messageName);

		
//		Node partNode = XMLParsing.getSubnode(messageNode, nodeName);
//		System.err.println("PART NODE: " + partNode.getPath() + " --- " + nodeName);
//		return XMLParsing.getNodeAttribute(partNode, "element");
		return null;
	}
}

//Object complexType = elem.getType();
//if ( complexType instanceof ComplexTypeImpl ){
//	System.err.println("COMPLEX TYPE " + ((ComplexTypeImpl) complexType).getSequence().getElements());
//	Sequence complexSequence = ((ComplexTypeImpl) complexType).getSequence();
//	if ( complexSequence != null ){
//		List<Element> complexSequenceElem = complexSequence.getElements();
//		for ( Element subelem : complexSequenceElem ) {
//			System.err.println(subelem.getType().getQName());
//		}
//	}
//}
//if ( complexType instanceof SimpleTypeImpl ){
////	System.err.println("SIMPLE TYPE " + ((SimpleTypeImpl) complexType).getQName());
//}