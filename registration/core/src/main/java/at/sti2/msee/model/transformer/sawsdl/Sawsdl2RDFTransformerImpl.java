package at.sti2.msee.model.transformer.sawsdl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import uk.ac.open.kmi.iserve.importer.sawsdl.Sawsdl11Transformer;
import uk.ac.open.kmi.iserve.importer.sawsdl.Sawsdl20Transformer;
import at.sti2.msee.model.transformer.Service2RDFTransformer;
import at.sti2.msee.model.transformer.Service2RDFTransformerException;

public class Sawsdl2RDFTransformerImpl extends Service2RDFTransformer {

	private static final int vUnknown = -1;
	private static final int v11 = 1;
	private static final int v20 = 2;

	@Override
	public String toMSM(URL descriptionURL) throws Service2RDFTransformerException {				
		try 
		{			
			String serviceDescription = IOUtils.toString(descriptionURL.openStream());
			String baseURI = "http://msee.sti2.at/services/" + UUID.randomUUID() + "#";
			InputStream msmServiceDescriptionStream = transform2MSM(baseURI, serviceDescription);
			
			return IOUtils.toString(msmServiceDescriptionStream);
		} 
		catch (IOException e) 
		{
			throw new Service2RDFTransformerException(e.getCause());
		}		
	}
	
	private InputStream transform2MSM(String baseURI, String serviceDescription) throws Service2RDFTransformerException {
		String resultString = null;
		try 
		{
			int wsdlVersion = getWSDLVersion(serviceDescription);
			if ( wsdlVersion == v11 ) 
			{
				Sawsdl11Transformer sawsdl11Transformer = new Sawsdl11Transformer();
				sawsdl11Transformer.setDefaultSyntax(this.getDefaultSyntax());
				sawsdl11Transformer.setBaseURI(baseURI);
				
				resultString = sawsdl11Transformer.transform(serviceDescription);
			} 
			else if ( wsdlVersion == v20 ) 
			{
				Sawsdl20Transformer sawsdl20Transformer = new Sawsdl20Transformer();
				sawsdl20Transformer.setDefaultSyntax(this.getDefaultSyntax());
				sawsdl20Transformer.setBaseURI(baseURI);
				resultString = sawsdl20Transformer.transform(serviceDescription);
			} 
			else 
			{
				throw new Service2RDFTransformerException("Unknown version of WSDL, can not find either http://schemas.xmlsoap.org/wsdl or http://www.w3.org/ns/wsdl ");
			}
			
			return new ByteArrayInputStream(resultString.getBytes());
		} 
		catch (Exception e) 
		{
			throw new Service2RDFTransformerException(e.getCause());
		} 
	}
	
	private int getWSDLVersion(String serviceDescription) throws IOException {		
		StringReader reader = new StringReader(serviceDescription);
		BufferedReader br = null;
		br = new BufferedReader(reader);
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.toLowerCase();
			if ( line.contains("www.w3.org/ns/wsdl") ) {
				br.close();
				return v20;
			} else if ( line.contains("schemas.xmlsoap.org/wsdl") ) {
				br.close();
				return v11;
			}
		}
		reader.close();
		if (br != null) {
			br.close();
		}
		return vUnknown;
	}	
}
