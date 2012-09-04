package eu.soa4all.ranking.wsmolite;

import org.wsmo.common.exception.InvalidModelException;

import junit.framework.TestCase;
import eu.soa4all.ranking.wsmolite.WSMOLiteRDFReader;

public class WSMOLiteRDFReaderTest extends TestCase {
	
	WSMOLiteRDFReader rdfReader;
	@Override
	protected void setUp() throws Exception {
		rdfReader = new WSMOLiteRDFReader();
	}
	 
	public void testReader() {	
		ClassLoader l = this.getClass().getClassLoader();
		
		rdfReader.readFromFile(l.getResource("instances.rdf.n3").getFile());
		try {
			rdfReader.getInstances();
		} catch (InvalidModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
