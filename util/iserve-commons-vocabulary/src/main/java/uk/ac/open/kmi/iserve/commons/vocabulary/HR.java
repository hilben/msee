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
package uk.ac.open.kmi.iserve.commons.vocabulary;

import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;

/**
 * Hrests (WSDL) Vocabulary
 * 
 * @author Christian Mayr
 */
public class HR {

	// Name spaces
	public static final String NS_URI = "http://www.wsmo.org/ns/hrests#";
	public static final String NS_PREFIX = "hr";

	// Classes


	// Properties
	public static final String hASADDRESS = NS_URI + "hasAddress";
	public static final URI hasAddress = new URIImpl(hASADDRESS);
	public static final String hASMETHOD = NS_URI + "hasMethod";
	public static final URI hasMethod = new URIImpl(hASMETHOD);

}
