package at.sti2.msee.model.transformer.hrests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.UUID;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.xml.sax.InputSource;

import at.sti2.msee.model.transformer.Service2RDFTransformer;
import at.sti2.msee.model.transformer.Service2RDFTransformerException;

public class Hrests2RDFTransformerImpl extends Service2RDFTransformer {

	@Override
	public String toMSM(URL descriptionURL)
			throws Service2RDFTransformerException {
		try {
			String serviceDescription = IOUtils.toString(descriptionURL
					.openStream());
			String baseURI = "http://msee.sti2.at/services/"
					+ UUID.randomUUID() + "#";
			InputStream msmServiceDescriptionStream = transform2MSM(baseURI,
					serviceDescription);
			return IOUtils.toString(msmServiceDescriptionStream);
		} catch (IOException e) {
			throw new Service2RDFTransformerException(e.getCause());
		}
	}

	private InputStream transform2MSM(String baseURI, String serviceDescription) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source dataFile = new StreamSource(new StringReader(serviceDescription));
		StreamSource stylesource = new StreamSource(this.getClass()
				.getResourceAsStream("/hrests.xslt"));

		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer(stylesource);
			transformer.transform(dataFile, new StreamResult(outputStream));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		InputStream rdfxml = new ByteArrayInputStream(
				outputStream.toByteArray());
		
		Model tempModel = RDF2Go.getModelFactory().createModel();
		tempModel.open();
		try {
			tempModel.readFrom(rdfxml, this.getDefaultSyntax(), baseURI);
		} catch (ModelRuntimeException | IOException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(tempModel.serialize(this.getDefaultSyntax()).getBytes());
	}

}
