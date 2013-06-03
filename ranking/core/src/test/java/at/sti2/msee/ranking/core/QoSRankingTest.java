/*
 * Copyright (c) 2012, University of Innsbruck, Austria.
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

package at.sti2.msee.ranking.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.core.datagenerator.MonitoringDataGenerator;
import at.sti2.msee.monitoring.core.datagenerator.MonitoringDataGeneratorParameters;
import at.sti2.msee.ranking.api.QoSRankingPreferencesTemplate;
import at.sti2.msee.ranking.api.exception.RankingException;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class QoSRankingTest extends TestCase {

	protected static Logger logger = Logger.getLogger(QoSRankingTest.class);

	// TODO: hardcoded endpoints
	public static String URL[] = { "http://www.example.com/testdataws1",
			"http://www.example.com/testdataws2" };

	@Test
	public void testQoSRanking() {

		logger.info("Testing the QoS Ranking ");
		java.net.URL ws1 = null;
		java.net.URL ws2 = null;
		
		try {
			 ws1 = new java.net.URL(URL[0]);
			ws2 = new java.net.URL(URL[1]);
		} catch (MalformedURLException e1) {
			fail();
		}

		logger.info("Generating test data");
		MonitoringDataGeneratorParameters params1 = new MonitoringDataGeneratorParameters(
				ws1, 3, 50, 1200, 50, 1200, 100, 1500, 0.05, new Date(), 10000);
		MonitoringDataGeneratorParameters params2 = new MonitoringDataGeneratorParameters(
				ws2, 3, 50, 1200, 50, 1200, 100, 1500, 0.05, new Date(), 10000);
		MonitoringDataGenerator g1 = new MonitoringDataGenerator(params1);
		MonitoringDataGenerator g2 = new MonitoringDataGenerator(params2);
		try {
			g1.createDataTest();
			g2.createDataTest();
		} catch (RepositoryException | MalformedQueryException
				| UpdateExecutionException | IOException | MonitoringException
				| ParseException e1) {
			fail();
		}
		
		// Set up the ranked QoSParams and fill the corresponding tables
		QoSRankingPreferencesTemplate qosRankingTemplate = new QoSRankingPreferencesTemplate();

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

		// Create a list with QosOrderingValueTables for all the endpoints
		ArrayList<QoSParamsEndpointRankingTable> endpointQoSParamsRankingTable = new ArrayList<QoSParamsEndpointRankingTable>();

		// Download all the QoS Params of the template
		for (String endpoint : URL) {

			QoSParamsEndpointRankingTable table = new QoSParamsEndpointRankingTable(
					endpoint, qosRankingTemplate);

			// The table retrieves its QoSParamKeyValues for its endpoint
			try {
				table.retrieveQoSParamValues();
			} catch (RankingException e) {
				logger.error(e);
				fail();
			}

			logger.info(table);

			endpointQoSParamsRankingTable.add(table);

		}

		// Use the ranking engine to get a ordered list
		QoSRankingEngine.rankQoSParamsTables(endpointQoSParamsRankingTable,
				qosRankingTemplate);

		// Show results
		logger.info("RESULTS: ");

		int rank = 1;
		for (QoSParamsEndpointRankingTable t : endpointQoSParamsRankingTable) {
			logger.info("RANK: " + rank + "\n" + t);
			rank++;

		}

	}
}
