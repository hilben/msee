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
 * WSDL-RDF (WSDL) Vocabulary
 * 
 * @author Christian Mayr
 */
public class WSDL {

	// Name spaces
	public static final String NS_URI = "http://www.w3.org/2005/10/wsdl-rdf#";
	public static final String NS_PREFIX = "wsdl";

	// Classes
	public static final String SERVICE = NS_URI + "Service";
	public static final URI Service = new URIImpl(SERVICE);
	public static final String ENDPOINT = NS_URI + "Endpoint";
	public static final URI Endpoint = new URIImpl(ENDPOINT);

	// Properties
	public static final String eNDPOINT = NS_URI + "endpoint";
	public static final URI endpoint = new URIImpl(eNDPOINT);
	public static final String aDDRESS = NS_URI + "address";
	public static final URI address = new URIImpl(aDDRESS);
	public static final String nAMESPACE = NS_URI + "namespace";
	public static final URI namespace = new URIImpl(nAMESPACE);

}
