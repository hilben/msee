/**
 * QueryHelper.java - at.sti2.util.triplestore
 */
package at.sti2.util.triplestore;

/**
 * @author Alex Oberhauser
 *
 */
public class QueryHelper {

	public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String DC_NS = "http://purl.org/dc/elements/1.1/";
	public static final String WSMF_NS = "http://www.sti2.at/wsmf/ns#";
	
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
        namespaces.append("PREFIX dc:<http://purl.org/dc/elements/1.1/>\n");
        namespaces.append("PREFIX dct:<http://purl.org/dc/terms/>\n");
		return namespaces.toString();
	}
}
