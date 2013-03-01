package org.inb.bsc.wsdl20;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.URI;

import org.inb.bsc.wsdl20.rdf.Wsdl2RdfFactory;
import org.inb.bsc.wsdl20.rdf.Wsdl2RdfOntology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Wsdl2RdfTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWsdl2Rdf() {
		
		Wsdl2RdfFactory factory = Wsdl2RdfFactory.newInstance();
		InputStream wsdlInputStream = this.getClass().getResourceAsStream("/webservices/ReservationService.wsdl"); 
		assertNotNull(wsdlInputStream);
		
		
		Wsdl2RdfOntology ontology = factory.createWsdl2RdfOntology(URI.create("http://test"));					
		assertNotNull(ontology);
		
	}
}
