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
package uk.ac.open.kmi.iserve.importer.sawsdl.schema;

import java.util.ArrayList;
import java.util.List;

import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.URI;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.open.kmi.iserve.commons.vocabulary.MSM;
import uk.ac.open.kmi.iserve.importer.sawsdl.util.ModelReferenceExtractor;

public class ComplexType extends Type {

	private List<Element> children;

	public ComplexType(Node node, String baseUriString) {
		super(node, baseUriString);
		children = new ArrayList<Element>();
		NodeList nodeList = node.getChildNodes();
		for ( int i = 0; i < nodeList.getLength(); i++ ) {
			if ( nodeList.item(i) != null ) {// && nodeList.item(i).getLocalName() != null ) {
				String attrName = null;
				if ( nodeList.item(i).getLocalName() != null && nodeList.item(i).getLocalName() != "" ) {
					attrName = nodeList.item(i).getLocalName();
				} else if ( nodeList.item(i).getNodeName() != null && nodeList.item(i).getNodeName() != "" ) {
					attrName = nodeList.item(i).getNodeName();
				}
				if ( attrName.contains("sequence") ) {
					NodeList elList = nodeList.item(i).getChildNodes();
					if ( elList != null && elList.getLength() > 0 ) {
						for ( int j = 0; j < elList.getLength(); j++ ) {
							 
							if ( elList.item(j) != null ) { // && elList.item(j).getLocalName() != null ) {
								String elName = null;
								if ( elList.item(j).getLocalName() != null && elList.item(j).getLocalName() != "" ) {
									elName = elList.item(j).getLocalName();
								} else if ( elList.item(j).getNodeName() != null && elList.item(j).getNodeName() != "" ) {
									elName = elList.item(j).getNodeName();
								}
								if ( elName.contains("element") ) {
									children.add(new Element(elList.item(j), baseUriString));
								}
							}
						}
					}
				}
			}
		}
	}

	public List<Element> getSequence() {
		return children;
	}

	public void toRdf(Model tempModel, URI partUri) {
		ModelReferenceExtractor.processModelRefString(getModelReference(), tempModel, partUri);
		ModelReferenceExtractor.processLiLoString(getLoweringSchemaMapping(), tempModel, partUri, MSM.loweringSchemaMapping);
		ModelReferenceExtractor.processLiLoString(getLiftingSchemaMapping(), tempModel, partUri, MSM.liftingSchemaMapping);

		// process children
		for ( Element child : children ) {
			tempModel.addStatement(partUri, MSM.hasPart, tempModel.createURI(child.getUriString()));
			tempModel.addStatement(partUri, MSM.hasPartTransitive, tempModel.createURI(child.getUriString()));
		}
	}

}
