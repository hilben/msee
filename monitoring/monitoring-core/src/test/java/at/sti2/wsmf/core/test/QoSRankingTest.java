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

package at.sti2.wsmf.core.test;

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

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.ranking.QoSParamsEndpointRankingTable;
import at.sti2.wsmf.core.ranking.QoSRankingEngine;
import at.sti2.wsmf.core.ranking.QoSRankingPreferencesTemplate;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class QoSRankingTest extends TestCase {

	protected static Logger logger = Logger.getLogger(QoSRankingTest.class);

	// public static String URL[] = {
	// "http://sesa.sti2.at:8080/invoker-dummy-webservice/services/valenciatPortWebService",
	// "http://localhost:9292/at.sti2.ngsee.testwebservices/services/randomnumber",
	// "http://localhost:9292/at.sti2.ngsee.testwebservices/services/reversestring",
	// "http://localhost:9292/at.sti2.ngsee.testwebservices/services/stringuppercase",
	// "http://localhost:9292/at.sti2.ngsee.testwebservices/services/randomstring",
	// "http://localhost:9292/at.sti2.ngsee.testwebservices/services/stringmulti"
	// };

	public static void testQoSRanking() {
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

		
		for (QoSParamKey q : QoSParamKey.values()) {
			qosRankingTemplate.addPropertyAndImportance(q,	(float)(Math.random()+0.1));
		}
		
		// Set up preferences for the QoSParams
//		qosRankingTemplate.addPropertyAndImportance(
//				QoSParamKey.ResponseTimeAverage, -6.0f);
//		qosRankingTemplate.addPropertyAndImportance(QoSParamKey.RequestTotal,
//				2.0f);
//		qosRankingTemplate.addPropertyAndImportance(
//				QoSParamKey.PayloadSizeAverage, 5.0f);

		// Create a list with QosOrderingValueTables for all the endpoints
		ArrayList<QoSParamsEndpointRankingTable> endpointQoSParamsRankingTable = new ArrayList<QoSParamsEndpointRankingTable>();

		// Obtain all endpoints monitored by the monitoring frame work
		List<String> endpoints = new Vector<String>();
		try {
			endpoints = persitentHandler.getEndpoints();
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
