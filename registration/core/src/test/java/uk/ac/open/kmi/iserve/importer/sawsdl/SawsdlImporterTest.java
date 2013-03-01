package uk.ac.open.kmi.iserve.importer.sawsdl;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.wsdl.WSDLException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import uk.ac.open.kmi.iserve.commons.io.IOUtil;
import uk.ac.open.kmi.iserve.importer.ImporterConfig;
import uk.ac.open.kmi.iserve.importer.ImporterException;

public class SawsdlImporterTest {

	private static final String ISERVE_URL = "http://localhost:9080/";
	private static final String DOC_FOLDER_PATH = "/Users/cp3982/Workspace/TempServicesFolder/";
	private static final String SESAME_URL = "http://localhost:8080/openrdf-sesame";
	private static final String REPOSITORY_NAME = "serv_repo_owlim";

	private SawsdlImporter importer;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws RepositoryException, WSDLException, ParserConfigurationException, URISyntaxException, IOException, ImporterException {
		ImporterConfig config = new ImporterConfig(ISERVE_URL, DOC_FOLDER_PATH, SESAME_URL, REPOSITORY_NAME);
		importer = new SawsdlImporter(config);
		
		URL serviceDescriptionURL = this.getClass().getResource("/webservices/ReservationService.wsdl");
		assertNotNull(serviceDescriptionURL);
		
		File file = new File(serviceDescriptionURL.toURI());
		String contents = IOUtil.readString(file);
		
		InputStream serviceUri = importer.importServiceToString(file.getName(), contents, "http://test.com/test.html");
				
		System.out.println(org.apache.commons.io.IOUtils.toString(serviceUri));		                           
                
	}
}
