package at.sti2.monitoring.core;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sti2.monitoring.core.common.MonitoringConfig;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.vocabulary.XSD;

public class MonitoringOntology {

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	public String NS = "";
	private OntModel m;
	private static MonitoringOntology monitoringOntology = null;

	/*
	 * Owl Classes
	 */
	public static final String MonitoredWebservice = "MonitoredWebservice";
	public static final String InvocationState = "InvocationState";
	public static final String AvailabilityState = "AvailabilityState";
	public static final String QoSParameter = "QoSParameter";
	public static final String QoSParameterType ="QoSParameterType";

	/*
	 * Object properties
	 */
	public static final String hasInvocationState = "hasInvocationState";
	public static final String hasCurrentInvocationState = "hasCurrentInvocationState";
	
	public static final String hasQoSParameter = "hasQoSParameter";
	public static final String hasCurrentQoSParameter = "hasQoSParameter";
	
	public static final String hasAvailabilityState = "hasAvailabilityState";
	public static final String hasCurrentAvailabilityState = "hasCurrentAvailabilityState";

	/*
	 * Datatype properties
	 */
	public static final String hasDateTime = "hasDateTime";
	public static final String hasStateName = "hasStateName";
	public static final String hasURL = "hasURL";

	public static final String isCurrentlyMonitored = "isCurrentlyMonitored";
	public static final String hasQoSType = "hasQoSType";
	public static final String hasQoSUnit = "hasQoSUnit";

	private MonitoringOntology(String namespace) {
		this.NS = namespace;
		m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		this.init();
	}

	public static MonitoringOntology getMonitoringOntology() throws IOException {
		if (monitoringOntology == null) {
			monitoringOntology = new MonitoringOntology(MonitoringConfig
					.getConfig().getInstancePrefix());
		}
		return monitoringOntology;
	}

	public OntModel getOntology() {
		//Return a copy of the ontology
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

		LOGGER.info("Stored the monitoring ontology to: " + file);
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
		
		//Classes
		OntClass MonitoredWebservice = m.createClass(NS
				+ MonitoringOntology.MonitoredWebservice);
		OntClass InvocationState = m.createClass(NS
				+ MonitoringOntology.InvocationState);
		OntClass AvailabilityState = m.createClass(NS
				+ MonitoringOntology.AvailabilityState);
		OntClass QoSParameter = m.createClass(NS
				+ MonitoringOntology.QoSParameter);
		OntClass QoSParameterType = m.createClass(NS
				+ MonitoringOntology.QoSParameterType);
		
		ObjectProperty hasInvocationState = m.createObjectProperty(NS
				+ MonitoringOntology.hasInvocationState);
		ObjectProperty hasAvailabilityState = m.createObjectProperty(NS
				+ MonitoringOntology.hasAvailabilityState);
		ObjectProperty hasQoSParameter = m.createObjectProperty(NS
				+ MonitoringOntology.hasQoSParameter);

		ObjectProperty hasCurrentInvocationState = m.createObjectProperty(NS
				+ MonitoringOntology.hasCurrentInvocationState);
		ObjectProperty hasCurrentAvailabilityState = m.createObjectProperty(NS
				+ MonitoringOntology.hasCurrentAvailabilityState);
		ObjectProperty hasCurrentQoSParameter = m.createObjectProperty(NS
				+ MonitoringOntology.hasCurrentQoSParameter);

		hasInvocationState.addDomain(MonitoredWebservice);
		hasInvocationState.addRange(InvocationState);
		// hasInvocationState.addLabel("has Invocationstate", "en");

		hasAvailabilityState.addDomain(MonitoredWebservice);
		hasAvailabilityState.addRange(AvailabilityState);
		// hasAvailabilityState.addLabel("has AvailabilityState", "en");

		hasQoSParameter.addDomain(MonitoredWebservice);
		hasQoSParameter.addRange(QoSParameter);
		// hasQoSParameter.addLabel("has QoSParameter", "en");

		hasCurrentInvocationState.addDomain(MonitoredWebservice);
		hasCurrentInvocationState.addRange(InvocationState);
		// hasCurrentInvocationState.addLabel("has current Invocationstate",
		// "en");

		hasCurrentAvailabilityState.addDomain(MonitoredWebservice);
		hasCurrentAvailabilityState.addRange(AvailabilityState);
		// hasCurrentAvailabilityState.addLabel("has current AvailabilityState",
		// "en");

		hasCurrentQoSParameter.addDomain(MonitoredWebservice);
		hasCurrentQoSParameter.addRange(QoSParameter);
		// hasCurrentQoSParameter.addLabel("has current QoSParameter", "en");

		DatatypeProperty hasDateTime = m.createDatatypeProperty(NS
				+ MonitoringOntology.hasDateTime);
		hasDateTime.addRange(XSD.dateTime);

		DatatypeProperty hasStateName = m.createDatatypeProperty(NS
				+ MonitoringOntology.hasStateName);
		hasStateName.addRange(XSD.xstring);

		DatatypeProperty hasURL = m.createDatatypeProperty(NS + MonitoringOntology.hasURL);
		hasURL.addRange(XSD.QName);

		DatatypeProperty isCurrentlyMonitored = m.createDatatypeProperty(NS
				+ MonitoringOntology.isCurrentlyMonitored);
		isCurrentlyMonitored.addRange(XSD.xboolean);
		isCurrentlyMonitored.addDomain(MonitoredWebservice);

		DatatypeProperty hasQoSType = m.createDatatypeProperty(NS
				+ MonitoringOntology.hasQoSType);
		hasQoSType.addRange(XSD.xstring);
		hasQoSType.addDomain(QoSParameter);
		DatatypeProperty hasQoSUnit = m.createDatatypeProperty(NS
				+ MonitoringOntology.hasQoSUnit);
		hasQoSUnit.addRange(XSD.xstring);
		hasQoSUnit.addDomain(QoSParameterType);
	}

}
