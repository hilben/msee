package at.sti2.monitoring.core;

import java.io.IOException;
import java.net.URL;

import com.hp.hpl.jena.query.ParameterizedSparqlString;

public class MonitoringQueries {

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

	public static String removeMonitoredWebserviceAndData(URL webService)
			throws IOException {
		String query = new String("CLEAR GRAPH ?webservice");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		MonitoringQueries.setNSPrefix(queryString);

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

		MonitoringQueries.setNSPrefix(queryString);

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

		MonitoringQueries.setNSPrefix(queryString);

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

		MonitoringQueries.setNSPrefix(queryString);

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

		MonitoringQueries.setNSPrefix(queryString);

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

		queryString.setLiteral("?time", time);

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

		MonitoringQueries.setNSPrefix(queryString);

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

}
