package uk.ac.open.kmi.iserve.importer.sawsdl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.wsdl.WSDLException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import uk.ac.open.kmi.iserve.commons.io.IOUtil;
import uk.ac.open.kmi.iserve.importer.ImporterConfig;
import uk.ac.open.kmi.iserve.importer.ImporterException;

public class SawsdlImporterTest {

	private SawsdlImporter importer;

	@Test
	public void testIServeSAWSDLImporter() throws RepositoryException, WSDLException, ParserConfigurationException, URISyntaxException, IOException, ImporterException {
		URL serviceDescriptionURL = this.getClass().getResource("/webservices/sawsdl/ReservationService.sawsdl");
		assertNotNull(serviceDescriptionURL);
		
		File file = new File(serviceDescriptionURL.toURI());
		String contents = IOUtil.readString(file);
		
		ImporterConfig config = new ImporterConfig("dummyURL","dummyFolderPath","dummySesame","dummyRepository");
		importer = new SawsdlImporter(config);
		InputStream serviceDescription = importer.importServiceToString(file.getName(), contents, "http://test.com/test.html");
		
		String descriptionString = org.apache.commons.io.IOUtils.toString(serviceDescription);
		assertTrue(descriptionString.contains("<rdf:Description rdf:about=\"http://sawsdl-transformer.baseuri/8965949584020236497#wsdl.service(reservationService)\">"));
                
	}
}
