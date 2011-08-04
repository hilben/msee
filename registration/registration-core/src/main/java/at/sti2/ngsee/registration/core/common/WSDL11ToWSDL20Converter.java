package at.sti2.ngsee.registration.core.common;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public abstract class WSDL11ToWSDL20Converter {
	
	private static String XSLT_CONVERTER = "file:///home/koni/development/sti/wsdl11to20.xsl";

	public static String convert(String _wsdlURI) throws TransformerException {
		
		File xmlFile = new File(_wsdlURI);
        File xsltFile = new File(XSLT_CONVERTER);
		
		Source xmlSource = new StreamSource(xmlFile);
        Source xsltSource = new StreamSource(xsltFile);
		
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans = transFact.newTransformer(xsltSource);
		trans.transform(xmlSource, new StreamResult(System.out));
		
		return null;
	}
	
	public static void main(String [] args) throws TransformerException {
		convert("file:///home/koni/globalweather.sawsdl");
	}
}
