package at.sti2.msee.ranking.repository;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.core.common.MonitoringConfig;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.vocabulary.XSD;

public class RankingOntology {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	public String NS = "";
	private OntModel m;
	private static RankingOntology rankingOntology = null;

	/*
	 * Owl Classes
	 */
	public static final String RankedWebservice = "RankedWebservice";
	/*
	 * Object properties
	 */
	public static final String hasRule = "hasRule";

	/*
	 * Datatype properties
	 */
//	public static final String hasDateTime = "hasDateTime";


	private RankingOntology(String namespace) {
		this.NS = namespace;
		m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		this.init();
	}

	public static RankingOntology getRankingOntology() throws IOException {
		if (rankingOntology == null) {
			rankingOntology = new RankingOntology(MonitoringConfig
					.getConfig().getInstancePrefix());
		}
		return rankingOntology;
	}

	public OntModel getOntology() {
		// Return a copy of the ontology
		OntModel ret = ModelFactory.createOntologyModel();
		ret.add(m);
		return ret;
	}

	// TODO: Extend to store in several formats?
	public void storeToFile(String file) throws IOException {
		RDFWriter writer = m.getWriter("RDF/XML");

		// writer.setErrorHandler(myErrorHandler);
		writer.setProperty("showXmlDeclaration", "true");
		writer.setProperty("tab", "8");
		writer.setProperty("relativeURIs", "same-document,relative");

		OutputStream out = new FileOutputStream(file);
		writer.write(m, out, NS);
		out.close();

		LOGGER.info("Stored the ranking ontology to: " + file);
	}

	public String getOntologyAsRDFXML() throws IOException {

		RDFWriter writer = m.getWriter("RDF/XML");

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writer.write(m, out, null);
		out.close();

		String rdf = out.toString();

		return rdf;
	}

	public String getOntologyModelAsRDFXML(OntModel model) throws IOException {
		RDFWriter writer = model.getWriter("RDF/XML");

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writer.write(model, out, null);
		out.close();

		String rdf = out.toString();

		return rdf;
	}

	/**
	 * Set up the ontology for the monitoring component
	 */
	private void init() {
		m.setNsPrefix("ns", NS);

		// Classes
		OntClass RankedWebservice = m.createClass(NS
				+ RankingOntology.RankedWebservice);
		
		DatatypeProperty hasRule = m.createDatatypeProperty(NS
				+ RankingOntology.hasRule);

		
		hasRule.addDomain(RankedWebservice);
		hasRule.addRange(XSD.xstring);

	}

}
