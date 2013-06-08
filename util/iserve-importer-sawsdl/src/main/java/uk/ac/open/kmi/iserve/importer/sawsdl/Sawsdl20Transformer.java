/*
   Copyright ${year}  Knowledge Media Institute - The Open University

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package uk.ac.open.kmi.iserve.importer.sawsdl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.WSDLSource;
import org.apache.woden.schema.Schema;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.xml.BindingOperationElement;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.EndpointElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceFaultReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;
import org.apache.woden.wsdl20.xml.ServiceElement;
import org.apache.woden.wsdl20.xml.TypesElement;
import org.apache.woden.xml.XMLAttr;
import org.apache.ws.commons.schema.XmlSchema;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.RDF;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.open.kmi.iserve.commons.vocabulary.HR;
import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;
import uk.ac.open.kmi.iserve.commons.vocabulary.WSDL;
import uk.ac.open.kmi.iserve.importer.sawsdl.schema.SchemaMap;
import uk.ac.open.kmi.iserve.importer.sawsdl.schema.SchemaParser;
import uk.ac.open.kmi.iserve.importer.sawsdl.util.ModelReferenceExtractor;

public class Sawsdl20Transformer {

	private String baseURI = "http://sawsdl-transformer.baseuri/8965949584020236497#";
	private SchemaParser schemaParser;
	private List<SchemaMap> schemaMaps;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;

	private Syntax defaultSyntax = Syntax.RdfXml;
	private String method = null;
	private String endpoint = null;

	public Sawsdl20Transformer() throws ParserConfigurationException {
		schemaParser = new SchemaParser();
		schemaMaps = new ArrayList<SchemaMap>();
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
	}

	public String transform(String serviceDescription) throws WSDLException, SAXException,
			IOException {
		WSDLFactory wsdlFactory = WSDLFactory.newInstance();
		WSDLReader reader = wsdlFactory.newWSDLReader();
		reader.setFeature(WSDLReader.FEATURE_VALIDATION, false);

		InputSource is = new InputSource(new StringReader(serviceDescription));
		Document dom = builder.parse(is);
		Element domElement = dom.getDocumentElement();

		WSDLSource wsdlSource = reader.createWSDLSource();
		wsdlSource.setSource(domElement);

		Description definition = reader.readWSDL(wsdlSource);
		DescriptionElement descElement = definition.toElement();

		// java.net.URI targetNamespace = descElement.getTargetNamespace();
		// this.setBaseURI(targetNamespace.toString());

		Model tempModel = RDF2Go.getModelFactory().createModel();
		tempModel.open();

		processTypes(descElement.getTypesElement(), tempModel, getBaseURI() + "types/");
		processServices(descElement.getServiceElements(), tempModel);

		this.setNamespaces(tempModel);

		String result = tempModel.serialize(this.defaultSyntax);
		// result = result.replaceAll(TEMP_BASE_URI, "#");
		tempModel.close();
		// System.out.println(result);
		schemaMaps = null;
		reader = null;
		schemaMaps = new ArrayList<SchemaMap>();
		return result;
	}

	private void setNamespaces(Model model) {
		model.setNamespace("msm", "http://cms-wg.sti2.org/ns/minimal-service-model#");
	}

	private void processTypes(TypesElement typesElement, Model tempModel, String baseUriString) {
		Schema[] schemas = typesElement.getInlinedSchemas();
		if (schemas != null) {
			for (Schema schema : schemas) {
				XmlSchema xmlSchema = schema.getSchemaDefinition();
				schemaMaps.add(schemaParser.parse(xmlSchema, baseUriString));
			}
		}
	}

	private void processServices(ServiceElement[] serviceElements, Model tempModel) {
		if (null == serviceElements || serviceElements.length <= 0) {
			return;
		}
		for (SchemaMap schemaMap : schemaMaps) {
			schemaMap.toRdf(tempModel);
		}
		for (ServiceElement serviceElement : serviceElements) {

			EndpointElement[] endpoints = serviceElement.getEndpointElements();
			String endpointName = endpoints[0].getAddress().toString();
			if (endpointName.contains("Soap12Endpoint") || endpointName.contains("Soap11Endpoint")) {
				endpoint = endpointName.substring(0, endpointName.lastIndexOf("."));
			} else {
				endpoint = endpointName;
			}
			String serviceNS = serviceElement.getName().getNamespaceURI();

			// rest in wsdl format - get binding
			try {
				BindingOperationElement bindingOperation = endpoints[0].getBindingElement()
						.getBindingOperationElements()[0];
				for (XMLAttr attr : bindingOperation.getExtensionAttributes()) {
					if (attr.getAttributeType().toString()
							.equals("{http://www.w3.org/ns/wsdl/http}method")) {
						method = (String) attr.getContent();
					}
				}

			} catch (Exception e) {
			}

			InterfaceElement intfElement = serviceElement.getInterfaceElement();
			if (intfElement != null && serviceElement != null && serviceElement.getName() != null) {
				URI serviceUri = tempModel.createURI(getBaseURI() + "wsdl.service("
						+ serviceElement.getName().getLocalPart() + ")");
				tempModel.addStatement(serviceUri, RDF.type, MSM.Service);

				// storing endpoint information
				tempModel.addStatement(serviceUri, RDF.type, WSDL.Service);
				tempModel.addStatement(serviceUri, WSDL.endpoint, new URIImpl(endpointName));
				tempModel.addStatement(new URIImpl(endpointName), RDF.type, WSDL.Endpoint);
				tempModel.addStatement(new URIImpl(endpointName), WSDL.address, endpoint);
				tempModel.addStatement(serviceUri, WSDL.namespace, serviceNS);

				ModelReferenceExtractor.extractModelReferences(intfElement, tempModel, serviceUri);

				processOperations(intfElement.getInterfaceOperationElements(), tempModel,
						serviceUri);
			}
		}
	}

	private void processOperations(InterfaceOperationElement[] operationElements, Model tempModel,
			URI serviceUri) {
		if (null == operationElements || operationElements.length <= 0) {
			return;
		}
		for (InterfaceOperationElement operationElement : operationElements) {
			URI operationUri = tempModel.createURI(serviceUri.toString() + "/"
					+ operationElement.getName().getLocalPart());
			tempModel.addStatement(operationUri, RDF.type, MSM.Operation);

			// REST
			if (method != null) {
				tempModel.addStatement(operationUri, HR.hasMethod, method);
				tempModel.addStatement(operationUri, HR.hasAddress, endpoint);
			}

			tempModel.addStatement(serviceUri, MSM.hasOperation, operationUri);
			ModelReferenceExtractor.extractModelReferences(operationElement, tempModel,
					operationUri);
			InterfaceMessageReferenceElement[] messages = operationElement
					.getInterfaceMessageReferenceElements();
			processMessages(messages, tempModel, operationUri);
			InterfaceFaultReferenceElement[] faults = operationElement
					.getInterfaceFaultReferenceElements();
			processFaults(faults, tempModel, operationUri);
		}
	}

	private void processFaults(InterfaceFaultReferenceElement[] faults, Model tempModel,
			URI operationUri) {
		if (null == faults || faults.length <= 0) {
			return;
		}
		for (InterfaceFaultReferenceElement fault : faults) {
			URI faultUri = null;
			if (fault.getDirection().equals(Direction.IN)) {
				faultUri = tempModel.createURI(operationUri.toString() + "/infault/"
						+ fault.getMessageLabel().toString());
				tempModel.addStatement(operationUri, MSM.hasInputFault, faultUri);
			} else if (fault.getDirection().equals(Direction.OUT)) {
				faultUri = tempModel.createURI(operationUri.toString() + "/outfault/"
						+ fault.getMessageLabel().toString());
				tempModel.addStatement(operationUri, MSM.hasOutputFault, faultUri);
			}
			tempModel.addStatement(faultUri, RDF.type, MSM.Message);
			if (fault.getRef() != null && fault.getRef().getLocalPart() != null) {
				URI faultPart = tempModel.createURI(getBaseURI() + "types/"
						+ fault.getRef().getLocalPart());
				tempModel.addStatement(faultPart, RDF.type, MSM.MessagePart);
				tempModel.addStatement(faultUri, MSM.hasPart, faultPart);
				tempModel.addStatement(faultUri, MSM.hasPartTransitive, faultPart);
			}
			ModelReferenceExtractor.extractModelReferences(fault, tempModel, faultUri);
			ModelReferenceExtractor.extractLiLoSchema(fault, tempModel, faultUri);
		}
	}

	private void processMessages(InterfaceMessageReferenceElement[] messages, Model tempModel,
			URI operationUri) {
		if (null == messages || messages.length <= 0) {
			return;
		}
		for (InterfaceMessageReferenceElement message : messages) {
			URI messageUri = null;
			if (message.getDirection().equals(Direction.IN)) {
				messageUri = tempModel.createURI(operationUri.toString() + "/input/"
						+ message.getMessageLabel().toString());
				tempModel.addStatement(operationUri, MSM.hasInput, messageUri);
			} else if (message.getDirection().equals(Direction.OUT)) {
				messageUri = tempModel.createURI(operationUri.toString() + "/output/"
						+ message.getMessageLabel().toString());
				tempModel.addStatement(operationUri, MSM.hasOutput, messageUri);
			}
			if (message.getElement() != null && message.getElement().getQName() != null
					&& message.getElement().getQName().getLocalPart() != null) {
				URI messagePart = tempModel.createURI(getBaseURI() + "types/"
						+ message.getElement().getQName().getLocalPart());
				tempModel.addStatement(messagePart, RDF.type, MSM.MessagePart);
				tempModel.addStatement(messageUri, MSM.hasPart, messagePart);
				tempModel.addStatement(messageUri, MSM.hasPartTransitive, messagePart);
			}
			tempModel.addStatement(messageUri, RDF.type, MSM.Message);
			ModelReferenceExtractor.extractModelReferences(message, tempModel, messageUri);
			ModelReferenceExtractor.extractLiLoSchema(message, tempModel, messageUri);
		}
	}

	public Syntax getDefaultSyntax() {
		return defaultSyntax;
	}

	public void setDefaultSyntax(Syntax defaultSyntax) {
		this.defaultSyntax = defaultSyntax;
	}

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

}
