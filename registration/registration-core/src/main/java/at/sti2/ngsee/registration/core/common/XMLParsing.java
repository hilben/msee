package at.sti2.ngsee.registration.core.common;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

/**
 * This class contains helpful methods on parsing a XML file (using XPath). It cannot be instantiated.(All methods are static)
 * 
 * @author Corneliu Valentin Stanciu
 *
 */
public abstract class XMLParsing {
		
	/**
	 * Search a single node in a document by the given node name as string.
	 * 
	 * @param _doc The XML file in a Document format. 
	 * @param _nodename The name of the searched node as string.
	 * @return The searched node or null if the node name was not found or was mistyped.
	 */
	public static Node getNode(Document _doc, String _nodename) {
		try {
			Node node = _doc.selectSingleNode("//*[name()='" + _nodename + "']");
			return node;
		}catch( NullPointerException e){
			return null;
		}
	}
	
	public static Node getNodeFromAttr(Document _doc, String _attribute) {
		try {
			Node node = _doc.selectSingleNode("//*[@name='" + _attribute + "']");
			return node;
		}catch( NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Search all nodes in a document by the given node name.
	 * 
	 * @param _doc The XML file in a Document format.
	 * @param _node The name of the searched node as string.
	 * @return A list of all nodes or null if the node name was not found or was mistyped.
	 */
	@SuppressWarnings("unchecked")
	public static List<Node> getNodes(Document _doc, String _node) {
		try{
			List<Node> nodes = _doc.selectNodes("//" + _node );
			return nodes;
		}catch( NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Search a single subnode of a node by the given node name. 
	 * 
	 * @param _parent The parent node.
	 * @param _nodename The name of the parent node as string. 
	 * @return The searched subnode or null if the node name was not found or was mistyped.
	 */
	public static Node getSubnode(Node _parent, String _nodename) {
		try{
			Node node = _parent.selectSingleNode("./*[name()='" + _nodename + "']");
			return node;
		}catch( NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Search a single subnode of a node by the given attribute name. 
	 * 
	 * @param _parent The parent node.
	 * @param _nodename The name of the parent node as string. 
	 * @return The searched subnode or null if the node name was not found or was mistyped.
	 */
	public static Node getSubnodeFromAttribute(Node _parent, String _attributename) {
		try{
			Node node = _parent.selectSingleNode("./*[@name='" + _attributename + "']");
			return node;
		}catch( NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Search all subnodes of a node by the given node name.
	 * 
	 * @param _parent The parent node.
	 * @param _nodename The name of the parent node as string.
	 * @return A list of all subnodes or null if the the node was not found or was mistyped.
	 */
	@SuppressWarnings("unchecked")
	public static List<Node> getSubnodes(Node _parent, String _nodename) {
		try{
			List<Node> node = _parent.selectNodes("./*[name()='" + _nodename + "']");
			return node;
		}catch( NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Search attribute of a given node.
	 * 
	 * @param _node The node where to search for attributes. 
	 * @param _attributeName The name of the searched attribute. 
	 * @return The value of the given attribute name.
	 */
	public static String getNodeAttribute(Node _node, String _attributeName) {
		try{
			String attribute = _node.valueOf("./@" + _attributeName );
			return attribute;
		}catch( NullPointerException e){
			return null;
		}
	}
}
