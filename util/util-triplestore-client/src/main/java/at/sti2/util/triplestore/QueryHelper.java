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
package at.sti2.util.triplestore;

/**
 * @author Alex Oberhauser
 * @author Corneliu-Valentin Stanciu
 * @author Benjamin Hiltpolt
 */
public class QueryHelper {

	public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String DC_NS = "http://purl.org/dc/elements/1.1/";
	public static final String WSMF_NS = "http://www.sti2.at/wsmf/ns#";
	public static final String MSM_NS = "http://cms-wg.sti2.org/ns/minimal-service-model#";
	public static final String SAWSDL_NS = "http://www.w3.org/ns/sawsdl#";
	public static final String WSDL_NS = "http://www.w3.org/ns/wsdl-rdf#";
	public static final String WL_NS = "http://www.wsmo.org/ns/wsmo-lite#";
	public static final String MSMEXT_NS = "http://sesa.sti2.at/ns/minimal-service-model-ext#";
	public static final String WSOAP_NS = "http://www.w3.org/ns/wsdl/soap#";
	public static final String XMLXSD_NS = "http://www.w3.org/2001/XMLSchema#";
	
	public static String getMSMEXTURI(String _property) {
		return MSMEXT_NS + _property;
	}
	
	public static String getXMLXSDURI(String _property) {
		return XMLXSD_NS + _property;
	}
	
	public static String getWSMFURI(String _property) {
		return WSMF_NS + _property;
	}
	
	public static String getRDFURI(String _property) {
		return RDF_NS + _property;
	}
	
	public static String getRDFSURI(String _property) {
		return RDFS_NS + _property;
	}
	
	public static String getDCURI(String _property) {
		return DC_NS + _property;
	}
	
	public static String getMSMURI(String _property) {
		return MSM_NS + _property;
	}
	
	public static String getSAWSDLURI(String _property) {
		return SAWSDL_NS + _property;
	}
	
	public static String getWSDLURI(String _property) {
		return WSDL_NS + _property;
	}
	
	public static String getWLURI(String _property) {
		return WL_NS + _property;
	}
	
	public static String getWSOAPURI(String _property) {
		return WSOAP_NS + _property;
	}
	
	/**
	 * @return Default Namespaces as SPARQL Prefix
	 */
	public static String getNamespacePrefix() {
		StringBuffer namespaces = new StringBuffer();
		namespaces.append("PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n");
        namespaces.append("PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n");
        namespaces.append("PREFIX owl:<http://www.w3.org/2002/07/owl#>\n");
        namespaces.append("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
        namespaces.append("PREFIX foaf:<http://xmlns.com/foaf/0.1/>\n");
        namespaces.append("PREFIX opo:<http://online-presence.net/opo/ns#>\n");
        namespaces.append("PREFIX geo:<http://www.w3.org/2003/01/geo/wgs84_pos#>\n");
        namespaces.append("PREFIX dc:<" + DC_NS + ">\n");
        namespaces.append("PREFIX dct:<http://purl.org/dc/terms/>\n");
        namespaces.append("PREFIX msm:<http://cms-wg.sti2.org/ns/minimal-service-model#>\n");
        namespaces.append("PREFIX sawsdl:<http://www.w3.org/ns/sawsdl#>\n");
        namespaces.append("PREFIX wsdl:<http://www.w3.org/ns/wsdl-rdf#>\n");
        namespaces.append("PREFIX wl:<http://www.wsmo.org/ns/wsmo-lite#>\n");
        namespaces.append("PREFIX wsmf:<" + WSMF_NS + ">\n");
        namespaces.append("PREFIX wsoap:<" + WSOAP_NS + ">\n");
        namespaces.append("PREFIX msm_ext: <" + MSMEXT_NS + ">\n");
		return namespaces.toString();
	}
}
