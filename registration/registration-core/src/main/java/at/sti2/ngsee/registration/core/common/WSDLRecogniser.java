package at.sti2.ngsee.registration.core.common;

import org.dom4j.Document;
import org.dom4j.Node;

public class WSDLRecogniser {
	private Document doc = null;
	
	public WSDLRecogniser(Document _doc) {
		this.doc = _doc;
	}

	public boolean isWSDL11() {
		Node portType = XMLParsing.getNode(this.doc, "wsdl:portType");
		if ( portType != null )
			return true;
		return false;
	}
	
	public boolean isWSDL20() {
		Node portType = XMLParsing.getNode(this.doc, "interface");
		if ( portType != null )
			return true;
		return false;
	}
}
