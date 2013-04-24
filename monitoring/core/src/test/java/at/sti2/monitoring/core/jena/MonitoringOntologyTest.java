package at.sti2.monitoring.core.jena;

import java.io.IOException;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;

import at.sti2.monitoring.core.MonitoringOntology;
import at.sti2.monitoring.core.MonitoringRepositoryHandler;
import at.sti2.monitoring.core.common.MonitoringConfig;
import at.sti2.msee.triplestore.ServiceRepository;

public class MonitoringOntologyTest extends TestCase {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	private String storeLocation = "src/test/resources/monitoringtest.rdf";
	private MonitoringOntology ontology;

	private String ns = "";

	@Before
	public void setUp() throws IOException {
		this.ontology = MonitoringOntology.getMonitoringOntology();
		this.ns = MonitoringConfig.getConfig().getInstancePrefix();
	}

	@Test
	public void testStoreOntologyToFile() throws IOException {
		this.ontology.storeToFile(storeLocation);
	}

	@Test
	public void testObtainWebservice() {
		OntModel m = this.ontology.getOntology();

		OntClass c = m.getOntClass(ns + MonitoringOntology.MonitoredWebservice);

		assertNotNull(c);

		Property p1 = m.getOntProperty(ns + MonitoringOntology.hasURL);
		Property fakeprop = m.getOntProperty(ns + MonitoringOntology.hasURL
				+ "fake");

		assertNull(fakeprop);
		assertNotNull(p1);

		Property p2 = m.getProperty(ns + MonitoringOntology.hasQoSType);
	}
	

	@Test
	public void testCreateWebservice() throws IOException {

		OntModel m = this.ontology.getOntology();
		DatatypeProperty data = m.getDatatypeProperty(ns + "hasURL");
		System.out.println(ns);
		OntClass c = m.getOntClass(ns + "MonitoredWebservice");

		for (int i = 0; i < 100; i++) {
			Individual curi = c.createIndividual(ns + UUID.randomUUID());
			curi.addProperty(data, "http://someadress.com/asdf");
		}

		this.ontology.storeToFile(storeLocation + "2");
	}

}
