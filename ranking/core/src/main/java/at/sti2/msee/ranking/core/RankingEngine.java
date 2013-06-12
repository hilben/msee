package at.sti2.msee.ranking.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.ontoware.rdf2go.model.Syntax;
import org.openrdf.repository.RepositoryException;
import org.wsmo.common.exception.InvalidModelException;

import at.sti2.msee.ranking.api.QoSRankingPreferencesTemplate;
import at.sti2.msee.ranking.api.exception.RankingException;
import at.sti2.msee.ranking.repository.RankingRepositoryHandler;
import eu.soa4all.ranking.rules.RulesRanking;
import eu.soa4all.ranking.wsmolite.RulesRankingTest;
import eu.soa4all.ranking.wsmolite.WSMOLiteRDFReader;
import eu.soa4all.validation.RPC.MSMService;
import eu.soa4all.validation.RPC.Service;
import eu.soa4all.validation.ServiceTemplate.Preference;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;

public class RankingEngine {
	RankingRepositoryHandler handler = null;

	public RankingEngine() throws RankingException {
		try {
			this.handler = RankingRepositoryHandler.getInstance();
		} catch (RepositoryException | IOException e) {
			throw new RankingException(e);
		}
	}

	/**
	 * @param args
	 * @throws RankingException
	 */
	public String[] getRankedServices(String[] services, String[] keys,
			String[] keyvalues) throws RankingException {
		return this.getRankedServices(services, keys, keyvalues, null, null);
	}

	public String[] getRankedServices(String[] services, String[] keys,
			String[] keyvalues, String template, String instances)
			throws RankingException {

		if (services.length < 2) {
			throw new RankingException(
					"There are not enough services for a ranking");
		}

		if (keys.length != keyvalues.length) {
			throw new RankingException(
					"Keys and values are not of the same length");
		}

		QoSRankingPreferencesTemplate qosRankingTemplate = new QoSRankingPreferencesTemplate();

		for (int i = 0; i < keys.length; i++) {
			qosRankingTemplate.addPropertyAndImportance(keys[i],
					Float.parseFloat(keyvalues[i]));
		}

		ServiceTemplate servicetemplate = new ServiceTemplate();
		RulesRanking engine = new RulesRanking();

		InputStream is = new ByteArrayInputStream(template.getBytes());

		servicetemplate.readFrom(is, Syntax.Ntriples);
		engine.setServiceTemplate(servicetemplate);

		Iterator<Preference> i = servicetemplate.getPreferences();

		WSMOLiteRDFReader rdfReader = new WSMOLiteRDFReader();
		rdfReader.readContent(instances);

		try {
			engine.setInstancesOntology(rdfReader.getInstances());
		} catch (InvalidModelException e) {
			throw new RankingException(e);
		}

		//
		// Loading services

		for (String s : services) {
			Service service = new MSMService();

			try {
				service.readFrom(this.getInputStreamForWS(new URL(s)),
						Syntax.Ntriples);
			} catch (IOException e) {
				throw new RankingException(e);
			}

			engine.addService(service, RulesRankingTest.class.getResource(s));
		}
		System.out.println(engine.rank());

		// Create a list with QosOrderingValueTables for all the endpoints
		ArrayList<QoSParamsEndpointRankingTable> endpointQoSParamsRankingTable = new ArrayList<QoSParamsEndpointRankingTable>();

		// Download all the QoS Params of the template
		for (String s : services) {

			String endpoint = s;

			QoSParamsEndpointRankingTable table = new QoSParamsEndpointRankingTable(
					endpoint, qosRankingTemplate);

			// The table retrieves its QoSParamKeyValues for its endpoint

			table.retrieveQoSParamValues();
			try {
				table.setNfpPropertyValues(engine.getWsNormalizedPropertyScore()
						.get(new URL(table.getName())));
			} catch (MalformedURLException e) {
				throw new RankingException("Malforemd URL",e);
			}

			endpointQoSParamsRankingTable.add(table);

			// Use the ranking engine to get a ordered list

		}

		System.out.println("RANKED COMPLETELY: "
				+ QoSRankingEngine.rankQoSParamsTables(
						endpointQoSParamsRankingTable, qosRankingTemplate));

		return null;
	}

	private InputStream getInputStreamForWS(URL url) throws RankingException,
			IOException {
		String str = this.handler.getRulesForWebservice(url);

		InputStream is = new ByteArrayInputStream(str.getBytes());

		return is;
	}
}
