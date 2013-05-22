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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.ranking.api.QoSRankingPreferencesTemplate;
import at.sti2.msee.ranking.api.exception.RankingException;
import at.sti2.msee.ranking.core.QoSParamsEndpointRankingTable;
import at.sti2.msee.ranking.core.QoSRankingEngine;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class QoSRankingTest extends TestCase {

	protected static Logger logger = Logger.getLogger(QoSRankingTest.class);

	//TODO: hardcoded endpoints
	public static String URL[] = {
			"http://www.example.com/testdataws1",
			"http://www.example.com/testdataws2"};

	
	
	@Test
	public void testQoSRanking() {
		
		logger.info("Testing the QoS Ranking ");
		
		// Set up the ranked QoSParams and fill the corresponding tables
		QoSRankingPreferencesTemplate qosRankingTemplate = new QoSRankingPreferencesTemplate();

		// Create random preferences for testing purposes
//		for (QoSType q : QoSType.values()) {
//			qosRankingTemplate.addPropertyAndImportance(q.name(),
//					(float) (Math.random() + 0.1));
//		}

		 // Set up preferences for the QoSParams
		 qosRankingTemplate.addPropertyAndImportance(QoSType.PayloadSizeRequestMaximum.name(),
		 1.0f);
		 qosRankingTemplate.addPropertyAndImportance(QoSType.PayloadSizeResponseMinimum.name(),
		 2.0f);
		 qosRankingTemplate.addPropertyAndImportance(QoSType.RequestSuccessful.name(),
		 -5.0f);

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
