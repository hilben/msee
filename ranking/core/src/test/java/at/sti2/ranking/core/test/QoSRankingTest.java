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

package at.sti2.ranking.core.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.ranking.core.ranking.QoSParamsEndpointRankingTable;
import at.sti2.ranking.core.ranking.QoSRankingEngine;
import at.sti2.ranking.api.data.qos.ranking.QoSRankingPreferencesTemplate;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class QoSRankingTest extends TestCase {

	protected static Logger logger = Logger.getLogger(QoSRankingTest.class);

	//TODO: hardcoded endpoints
	public static String URL[] = {
			"http://sesa.sti2.at:8080/monitoring-testwebservices/services/big/getBigAnswer ",
			"http://sesa.sti2.at:8080/monitoring-testwebservices/services/constant/getConstantAnswer ",
			"http://sesa.sti2.at:8080/monitoring-testwebservices/services/slow/getSlowAnswer" };

	//TODO: Rewrite this 
	public static void testQoSRanking() {
		
		logger.info("Testing the QoS Ranking ");
		
		// initialize the persistent handler
		PersistentHandler persitentHandler = null;
		try {
			persitentHandler = PersistentHandler.getInstance();
		} catch (FileNotFoundException e) {
			logger.error(e.getCause());
		} catch (IOException e) {
			logger.error(e.getCause());
		}

		// Set up the ranked QoSParams and fill the corresponding tables
		QoSRankingPreferencesTemplate qosRankingTemplate = new QoSRankingPreferencesTemplate();

		// Create random preferences for testing purposes
		for (QoSParamKey q : QoSParamKey.values()) {
			qosRankingTemplate.addPropertyAndImportance(q.name(),
					(float) (Math.random() + 0.1));
		}

		// // Set up preferences for the QoSParams
		// qosRankingTemplate.addPropertyAndImportance(QoSParamKey.MonitoredTime.name(),
		// 1.0f);
		// qosRankingTemplate.addPropertyAndImportance(QoSParamKey.AvailableTime.name(),
		// 2.0f);
		// qosRankingTemplate.addPropertyAndImportance(QoSParamKey.UnavailableTime.name(),
		// -5.0f);

		// Create a list with QosOrderingValueTables for all the endpoints
		ArrayList<QoSParamsEndpointRankingTable> endpointQoSParamsRankingTable = new ArrayList<QoSParamsEndpointRankingTable>();

		// Obtain all endpoints monitored by the monitoring frame work
		List<String> endpoints = new Vector<String>();
		try {
			endpoints = persitentHandler.getEndpoints();
			logger.info("Ranking the following endpoints: " + endpoints);
		} catch (QueryEvaluationException e2) {
			logger.error(e2.getCause());
		} catch (RepositoryException e2) {
			logger.error(e2.getCause());
		} catch (MalformedQueryException e2) {
			logger.error(e2.getCause());
		}

		// Download all the QoS Params of the template
		for (String endpoint : endpoints) {

			QoSParamsEndpointRankingTable table = new QoSParamsEndpointRankingTable(
					endpoint, qosRankingTemplate);

			// The table retrieves its QoSParamKeyValues for its endpoint
			table.retrieveQoSParamValues();

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
