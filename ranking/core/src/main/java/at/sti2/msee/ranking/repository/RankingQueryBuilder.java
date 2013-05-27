package at.sti2.msee.ranking.repository;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;

import com.hp.hpl.jena.query.ParameterizedSparqlString;

/**
 * @author benni
 *
 */
public class RankingQueryBuilder {

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
	

	public static String removeRankedWebserviceAndData(URL webService)
			throws IOException {
		String query = new String("CLEAR GRAPH ?webservice");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		RankingQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);

		return queryString.toString();
	}

	public static String insertRankedWebserviceAndRulesQuery(URL webService,
			String rule) throws IOException {
		String ns = RankingOntology.getRankingOntology().NS;

		String query = new String(
				"DELETE DATA\n"
						+ "{ GRAPH ?webservice { ?webservice  rdf:type  ?webservicetype  . \n"
						+ "?webservice ?hasrule ?oldrule"
						+ " } }; \n"
						+ "INSERT DATA\n"
						+ "{ GRAPH ?webservice { ?webservice  rdf:type  ?webservicetype  . \n"
						+ "?webservice ?hasrule ?rule" + " } }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		RankingQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);
		queryString.setIri("?webservicetype", ns
				+ RankingOntology.RankedWebservice);

		queryString.setIri("?hasrule", ns
				+ RankingOntology.hasRule);
		queryString.setLiteral("?rule", rule);

		return queryString.toString();
	}

	
	//TODO:
	public static String getWebServiceRuleQuery(URL webService)
			throws IOException {

		String ns = RankingOntology.getRankingOntology().NS;

		String query = new String("SELECT ?rule \n"
				+ "WHERE {?webservice rdf:type ?webservicetype .\n"
				+ "?webservice ?hasrule ?rule }");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		RankingQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?webservice", webService);
		queryString.setIri("?webservicetype", ns
				+ RankingOntology.RankedWebservice);

		queryString.setIri("?hasrule", ns
				+ RankingOntology.hasRule);

		return queryString.toString();
	}

	public static String getAllTriplesOfContext(URL context) throws IOException {

		String query = new String("		SELECT ?x ?y ?z \n" + "FROM ?context \n"
				+ "WHERE {?x ?y ?z}");

		ParameterizedSparqlString queryString = new ParameterizedSparqlString(
				query);

		RankingQueryBuilder.setNSPrefix(queryString);

		queryString.setIri("?context", context);

		return queryString.toString();

	}

	

}
