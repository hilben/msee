/*
 * Copyright (c) 2010, University of Innsbruck, Austria.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package eu.soa4all.ranking.wsmolite;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ontoware.rdf2go.model.Syntax;
import org.wsmo.common.exception.InvalidModelException;

import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.core.datagenerator.MonitoringDataGenerator;
import at.sti2.msee.monitoring.core.datagenerator.MonitoringDataGeneratorParameters;
import at.sti2.msee.ranking.api.QoSRankingPreferencesTemplate;
import at.sti2.msee.ranking.api.exception.RankingException;
import at.sti2.msee.ranking.core.QoSParamsEndpointRankingTable;
import at.sti2.msee.ranking.core.QoSRankingEngine;
import at.sti2.msee.ranking.repository.RankingRepositoryHandler;
import eu.soa4all.ranking.rules.RulesRanking;
import eu.soa4all.validation.RPC.MSMService;
import eu.soa4all.validation.RPC.Service;
import eu.soa4all.validation.ServiceTemplate.Preference;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;
import eu.soa4all.validation.WSMOLite.Annotation;

/**
 * RulesRanking test class
 * 
 * computes the ranking list of services using the normal rule ranking approach
 * that consider the entire set of NFPs
 * 
 * @author Ioan Toma
 * 
 * 
 * 
 *         TODO: currently work in progress support QoS / Ranking parameters
 *         together
 * 
 * @author benni
 */

public class RulesRankingTest extends TestCase {

	protected static Logger logger = Logger.getLogger(RulesRankingTest.class);

	private RulesRanking engine = new RulesRanking();

	private QoSRankingPreferencesTemplate qosRankingTemplate = null;

	private String[] wsTestRules = { "/WSMullerFixed_new.rdf.n3",
			"/WSRacerFixed_new.rdf.n3", "/WSRunnerFixed_new.rdf.n3",
			"/WSWalkerFixed_new.rdf.n3", "/WSWeaselFixed_new.rdf.n3" };

	private RankingRepositoryHandler handler;

	@Before
	protected void setUp() throws Exception {

		super.setUp();

		engine = new RulesRanking();

		this.handler = RankingRepositoryHandler.getInstance();

		for (String s : wsTestRules) {
			handler.clearAllContentForWebservice(RulesRankingTest.class
					.getResource(s));
			handler.setRulesForWebservice(
					RulesRankingTest.class.getResource(s), readFile(s));

			MonitoringDataGeneratorParameters p = new MonitoringDataGeneratorParameters(
					RulesRankingTest.class.getResource(s), 3, 10, 15, 5, 15,
					20, 100, 0.05, new Date(), 1000);
			MonitoringDataGenerator gen = new MonitoringDataGenerator(p);
			gen.createDataTest();
		}

		// Set up the ranked QoSParams and fill the corresponding tables
		qosRankingTemplate = new QoSRankingPreferencesTemplate();

		// Create random preferences for testing purposes
		// for (QoSType q : QoSType.values()) {
		// qosRankingTemplate.addPropertyAndImportance(q.name(),
		// (float) (Math.random() + 0.1));
		// }

		// Set up preferences for the QoSParams
		qosRankingTemplate.addPropertyAndImportance(
				QoSType.PayloadSizeRequestAverage.name(), 1.0f);
		qosRankingTemplate.addPropertyAndImportance(
				QoSType.PayloadSizeResponseAverage.name(), 1.0f);
		qosRankingTemplate.addPropertyAndImportance(
				QoSType.ResponseTimeAverage.name(), -1.0f);

	}

	@Test
	public void testRankingWithStoredRules() throws Exception {

		long startTime = System.currentTimeMillis();

		ClassLoader l = this.getClass().getClassLoader();

		// Loading service template
		ServiceTemplate template = new ServiceTemplate();

		logger.info("Loading service template");
		template.readFrom(new FileInputStream(l
				.getResource("stShipping.rdf.n3").getFile()), Syntax.Ntriples);
		engine.setServiceTemplate(template);

		Iterator<Preference> i = template.getPreferences();
		while (i.hasNext()) {
			System.out.println("Pref: " + i.next().resource);
		}

		WSMOLiteRDFReader rdfReader = new WSMOLiteRDFReader();
		rdfReader.readFromFile(l.getResource("instances.rdf.n3").getFile());
		try {
			engine.setInstancesOntology(rdfReader.getInstances());
		} catch (InvalidModelException e) {
			e.printStackTrace();
			fail();
		}
		//
		// Loading services
		logger.info("Loading services");

		for (String s : this.wsTestRules) {
			Service service = new MSMService();

			service.readFrom(this.getInputStreamForWS(RulesRankingTest.class
					.getResource(s)), Syntax.Ntriples);

			engine.addService(service, RulesRankingTest.class.getResource(s));
		}
		System.out.println(engine.rank());

		long time = System.currentTimeMillis() - startTime;

		logger.info("...finish ranking in " + time + "    miliseconds");

		// Create a list with QosOrderingValueTables for all the endpoints
		ArrayList<QoSParamsEndpointRankingTable> endpointQoSParamsRankingTable = new ArrayList<QoSParamsEndpointRankingTable>();

		// Download all the QoS Params of the template
		for (String s : wsTestRules) {

			String endpoint = RulesRankingTest.class.getResource(s)
					.toExternalForm();

			QoSParamsEndpointRankingTable table = new QoSParamsEndpointRankingTable(
					endpoint, qosRankingTemplate);

			// The table retrieves its QoSParamKeyValues for its endpoint
			try {
				table.retrieveQoSParamValues();
				table.setNfpPropertyValues(engine
						.getWsNormalizedPropertyScore().get(
								new URL(table.getName())));
			} catch (RankingException e) {
				logger.error(e);
				fail();
			}

			logger.info(table);

			endpointQoSParamsRankingTable.add(table);

			// Use the ranking engine to get a ordered list

		}

		System.out.println("RANKED COMPLETELY: "
				+ QoSRankingEngine.rankQoSParamsTables(
						endpointQoSParamsRankingTable, qosRankingTemplate));
		// System.out.println("ASDFASDFASDF"
		// + engine.getWsNormalizedPropertyScore());
	}

	private InputStream getInputStreamForWS(URL url) throws RankingException,
			IOException {
		String str = this.handler.getRulesForWebservice(url);

		InputStream is = new ByteArrayInputStream(str.getBytes());

		return is;
	}

	private String readFile(String path) throws IOException {
		Scanner sc = new Scanner(
				RulesRankingTest.class.getResourceAsStream(path));
		String text = sc.useDelimiter("\\A").next();
		sc.close();
		return text;
	}
}