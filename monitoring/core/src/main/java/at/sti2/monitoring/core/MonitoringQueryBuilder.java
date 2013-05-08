package at.sti2.monitoring.core;

import java.io.IOException;
import java.net.URL;

import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.vocabulary.XSD;

public class MonitoringQueryBuilder {

	private static void setNSPrefix(ParameterizedSparqlString queryString) {
		queryString
				.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		queryString.setNsPrefix("psys",
				"http://proton.semanticweb.org/protonsys#");
		queryString.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
		queryString.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");

		queryString.setNsPrefix("rdf",
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		queryString.setNsPrefix("pext",
				"http://proton.semanticweb.org/protonext#");
		queryString.setNsPrefix("msm",
				"http://cms-wg.sti2.org/ns/minimal-service-model#");
	}
	
	public final static String xsddatetime = "^^xsd:dateTime";

	public static String removeMonitoredWebserviceAndData(URL webService)
			throws IOException {
		String query = new String("CLEAR GRAPH ?webservice");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);

		return queryString.toString();
	}

	public static String insertMonitoredWebservice(URL webService,
			boolean isMonitored) throws IOException {
		String ns = MonitoringOntology.getMonitoringOntology().NS;

		String query = new String(
				"DELETE DATA\n"
						+ "{ GRAPH ?webservice { ?webservice  rdf:type  ?webservicetype  . \n"
						+ "?webservice ?isCurrentlyMonitored ?boolrev"
						+ " } }; \n"
						+ "INSERT DATA\n"
						+ "{ GRAPH ?webservice { ?webservice  rdf:type  ?webservicetype  . \n"
						+ "?webservice ?isCurrentlyMonitored ?bool" + " } }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);
		queryString.setIri("?webservicetype", ns
				+ MonitoringOntology.MonitoredWebservice);

		queryString.setIri("?isCurrentlyMonitored", ns
				+ MonitoringOntology.isCurrentlyMonitored);
		queryString.setLiteral("?bool", isMonitored);
		queryString.setLiteral("?boolrev", !isMonitored);

		return queryString.toString();
	}

	public static String getIsWebServiceMonitoredSPARQLQuery(URL webService)
			throws IOException {

		String ns = MonitoringOntology.getMonitoringOntology().NS;

		String query = new String("SELECT ?isMonitored \n"
				+ "WHERE {?webservice ?isCurrentlyMonitored ?isMonitored.\n"
				+ "?webservice rdf:type ?webservicetype }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);
		queryString.setIri("?webservicetype", ns
				+ MonitoringOntology.MonitoredWebservice);
		queryString.setIri("?isCurrentlyMonitored", ns
				+ MonitoringOntology.isCurrentlyMonitored);

		return queryString.toString();
	}

	public static String getAllTriplesOfContext(URL context) throws IOException {

		String query = new String("		SELECT ?x ?y ?z \n" + "FROM ?context \n"
				+ "WHERE {?x ?y ?z}");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?context", context);

		return queryString.toString();

	}

	public static String setInvocationInstanceState(URL webService,
			String invocationInstanceID, String invocationStateID,
			String statename, String time) throws IOException {
		String ns = MonitoringOntology.getMonitoringOntology().NS;

		String query = new String(
				"DELETE DATA\n"
						+ "{ GRAPH ?webservice { ?webservice  ?hasInvocation  ?invocationInstanceID  . \n"
						+ "?invocationInstanceID rdf:type ?invocationType ."
						+ "?invocationInstanceID ?hasstate ?invocationStateID ."
						+ "?invocationInstanceID ?hascurstate ?x ."
						+ " } }; \n"
						+ "INSERT DATA\n"
						+ "{ GRAPH ?webservice { ?webservice  ?hasInvocation  ?invocationInstanceID  . \n"
						+ "?invocationInstanceID rdf:type ?invocationType ."
						+ "?invocationInstanceID ?hasstate ?invocationStateID ."
						+ "?invocationInstanceID ?hascurstate ?invocationStateID ."
						+ "?invocationStateID ?hasstatename ?statename ."
						+ "?invocationStateID ?hastime ?time" + " } }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);
		queryString.setIri("?hasInvocation", ns
				+ MonitoringOntology.hasInvocationInstance);

		queryString.setIri("?invocationInstanceID", ns + invocationInstanceID);
		queryString.setIri("?invocationStateID", ns + invocationStateID);
		queryString.setIri("?invocationType", ns
				+ MonitoringOntology.InvocationInstance);

		queryString.setIri("?hasstate", ns
				+ MonitoringOntology.hasInvocationState);
		queryString.setIri("?hascurstate", ns
				+ MonitoringOntology.hasCurrentInvocationState);
		queryString.setIri("?hasstatename", ns
				+ MonitoringOntology.hasStateName);

		queryString.setLiteral("?statename", statename);

		queryString.setIri("?hastime", ns + MonitoringOntology.hasDateTime);

		queryString.setLiteral("?time", time+xsddatetime);


		return queryString.toString();
	}

	public static String getInvocationInstance(String UUID) throws IOException {
		String ns = MonitoringOntology.getMonitoringOntology().NS;

		String query = new String("SELECT ?statename ?time ?webservice \n"
				+ "WHERE {\n"
				+ "?webservice  ?hasInvocation  ?invocationInstanceID  . \n"
				+ "?invocationInstanceID  ?hascurstate ?invocationStateID .\n"
				+ "?invocationInstanceID rdf:type ?invocationType . \n"
				+ "?invocationInstanceID ?hasstate ?invocationStateID . \n"
				+ "?invocationStateID ?hasstatename ?statename .\n"
				+ "?invocationStateID ?hastime ?time" + " }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?hasInvocation", ns
				+ MonitoringOntology.hasInvocationInstance);

		queryString.setIri("?invocationInstanceID", ns + UUID);
		queryString.setIri("?invocationType", ns
				+ MonitoringOntology.InvocationInstance);

		queryString.setIri("?hasstate", ns
				+ MonitoringOntology.hasInvocationState);
		queryString.setIri("?hascurstate", ns
				+ MonitoringOntology.hasCurrentInvocationState);
		queryString.setIri("?hasstatename", ns
				+ MonitoringOntology.hasStateName);

		queryString.setIri("?hastime", ns + MonitoringOntology.hasDateTime);

		return queryString.toString();
	}

	public static String addQoSParam(URL url, String UUID, QoSParameter qosparam)
			throws IOException {
		String ns = MonitoringOntology.getMonitoringOntology().NS;

		String query = new String("\nWITH ?webservice\n"
				+ "DELETE {?webservice  ?hascurqos  ?curparamid }    \n"
				+ "WHERE { ?curparamid ?hastype ?qostype}; \n"
				+ "INSERT DATA\n" + "{ " + "GRAPH ?webservice {"
				+ "?webservice  ?hascurqos  ?qosparamid  . \n"
				+ "?webservice ?hasqos ?qosparamid  . \n"
				+ "?qosparamid ?hasvalue ?qosvalue  . \n"
				+ "?qosparamid ?hastime ?qostime. \n"
				+ "?qosparamid ?hastype ?qostype" + " } };");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", url);
		queryString.setIri("?qosparamid", ns + UUID);
		queryString.setLiteral("?qosvalue", qosparam.getValue());
		queryString.setLiteral("?qostime", qosparam.getTime()+xsddatetime);
		queryString.setIri("?qostype", ns + qosparam.getType().toString());

		queryString.setIri("?hascurqos", ns
				+ MonitoringOntology.hasCurrentQoSParameter);
		queryString.setIri("?hasqos", ns + MonitoringOntology.hasQoSParameter);
		queryString.setIri("?hastype", ns + MonitoringOntology.hasQoSType);
		queryString.setIri("?hasvalue", ns + MonitoringOntology.hasQoSValue);
		queryString.setIri("?hastime", ns + MonitoringOntology.hasDateTime);

		return queryString.toString();
	}

	public static String getCurrentQoSParameter(URL url, QoSType qosparamtype)
			throws IOException {
		String ns = MonitoringOntology.getMonitoringOntology().NS;

		String query = new String("\n"
				+ "SELECT ?value ?time\n FROM ?webservice\n" + "WHERE {"
				+ "?webservice  ?hascurqos  ?qosparamid  . \n"
				+ "?qosparamid ?hastype ?qostype  . \n"
				+ "?qosparamid ?hasvalue ?value  . \n"
				+ "?qosparamid ?hastime ?time. \n }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", url);

		queryString.setIri("?qostype", ns + qosparamtype.toString());

		queryString.setIri("?hascurqos", ns
				+ MonitoringOntology.hasCurrentQoSParameter);

		queryString.setIri("?hastype", ns + MonitoringOntology.hasQoSType);
		queryString.setIri("?hasvalue", ns + MonitoringOntology.hasQoSValue);
		queryString.setIri("?hastime", ns + MonitoringOntology.hasDateTime);

		return queryString.toString();
	}

	public static String updateAvailabilityState(URL webService, String UUID,
			String time, String statename) throws IOException {
		String ns = MonitoringOntology.getMonitoringOntology().NS;

		String query = new String(
				"DELETE DATA\n"
						+ "{ GRAPH ?webservice { ?webservice  ?hasCurAvailState  ?curstate  . \n"
						+ " } }; \n"
						+ "INSERT DATA\n"
						+ "{ GRAPH ?webservice { ?webservice  ?hasCurAvailState  ?availStateID  . \n"
						+ "?webservice  ?hasAvailState  ?availStateID  . \n"
						+ "?availStateID rdf:type ?availStateType .\n"
						+ "?availStateID ?hastime ?time .\n"
						+ "?availStateID ?hasstatename ?statename .\n } }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);
		queryString.setIri("?hasCurAvailState", ns
				+ MonitoringOntology.hasCurrentAvailabilityState);
		queryString.setIri("?hasAvailState", ns
				+ MonitoringOntology.hasAvailabilityState);
		queryString.setIri("?hasstatename", ns
				+ MonitoringOntology.hasStateName);

		queryString.setIri("?availStateID", ns + UUID);
		queryString.setIri("?availStateType", ns
				+ MonitoringOntology.AvailabilityState);

		queryString.setLiteral("?statename", statename);
		queryString.setLiteral("?time", time+xsddatetime);
		queryString.setIri("?hastime", ns + MonitoringOntology.hasDateTime);

		return queryString.toString();
	}

	public static String getCurrentAvailabilityState(URL webService)
			throws IOException {
		String ns = MonitoringOntology.getMonitoringOntology().NS;

		String query = new String("\n"
				+ "SELECT ?state ?time\n FROM ?webservice\n" + "WHERE {"
				+ "?webservice  ?hasCurAvailState  ?availID  . \n"
				+ "?availID ?hasstatename ?state  . \n"
				+ "?availID ?hastime ?time. \n }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);
		queryString.setIri("?hasCurAvailState", ns
				+ MonitoringOntology.hasCurrentAvailabilityState);
		queryString.setIri("?hasstatename", ns
				+ MonitoringOntology.hasStateName);

		queryString.setIri("?hastime", ns + MonitoringOntology.hasDateTime);

		return queryString.toString();
	}

}
