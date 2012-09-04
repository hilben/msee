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

package eu.soa4all.ranking.qos;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

//import at.sti2.wsmf.core.*;


public class QoSRanking  {

	protected static Logger logger = Logger.getLogger(QoSRanking.class);


	public static final String URL[] = {
		"http://sesa.sti2.at:8080/invoker-dummy-webservice/services/valenciatPortWebService",
		"http://localhost:9292/at.sti2.ngsee.testwebservices/services/randomnumber",
		"http://localhost:9292/at.sti2.ngsee.testwebservices/services/reversestring",
		"http://localhost:9292/at.sti2.ngsee.testwebservices/services/stringuppercase",
		"http://localhost:9292/at.sti2.ngsee.testwebservices/services/randomstring",
		"http://localhost:9292/at.sti2.ngsee.testwebservices/services/stringmulti"};
	
	
	public static void testQoSRanking() {
		assert(true);
	}
	
	public static void rankMonitoredEndpoints(List<MonitoredEndpoint> endpoints) {
		
	}
	
	/**
	 * @deprecated
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String args[]) throws Exception {
		
		
		//initialize the data 
		
		//OLD TEST DATA
		
		int endpoints_num = 10;
		
		ArrayList<MonitoredEndpoint> endpoints = new ArrayList<MonitoredEndpoint>();
		for (int i = 0; i < endpoints_num; i++) {
			endpoints.add(MonitoredEndpoint.getRandomMonitoredEndpoint("http://www.test.com/service_"+i));
		}
		
		for (MonitoredEndpoint e : endpoints) {
			System.out.println(e);
		}
		
//		
//		PersistentHandler phandler = null;
//		try {
//			phandler = PersistentHandler.getInstance();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			fail(e.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail(e.toString());
//		}
//		
//		
//		ArrayList<MonitoredEndpoint> endpoints = new ArrayList<MonitoredEndpoint>();
//		for (int i = 0; i < URL.length; i++) {
//			endpoints.add(new MonitoredEndpoint(URL[i], (phandler.getQoSParam(new URL(URL[i]),
//						QoSParamKey.ResponseTimeAverage), payloadSizeAverage, requestsTotal));
//		}
//		
		
		//Set up the ranked properties and fill the corresponding tables
		MonitoringRankingPropertySet rankingprops = new MonitoringRankingPropertySet();
		
		//Set up preferences
		rankingprops.addPropertyAndImportance("responsetime", -6.0f);
		rankingprops.addPropertyAndImportance("requests", 2.0f);
		rankingprops.addPropertyAndImportance("payload", 5.0f);
		
		ArrayList<PropertiesRankingValuesTable> endpointProperties = new ArrayList<PropertiesRankingValuesTable>();
		
		//Add ordering value to tables 
		for (MonitoredEndpoint e: endpoints) {
			PropertiesRankingValuesTable valueTable  = new PropertiesRankingValuesTable(e.getEndpointName(), rankingprops);
			valueTable.setRankingValueForProperty("responsetime", e.getResponseTimeAverage());
			valueTable.setRankingValueForProperty("requests", e.getRequestsTotal());
			valueTable.setRankingValueForProperty("payload", e.getPayloadSizeAverage());
			
			endpointProperties.add(valueTable);
	
			System.out.println(valueTable);
		}

		//Do the ordering using a scoring matrix
		for (String s : rankingprops.getPropertySet()) {
			//Find max
			float max = Float.NEGATIVE_INFINITY;
			for (PropertiesRankingValuesTable table : endpointProperties) {
				if (table.getRankingValueForProperty(s)>max) {
					max = table.getRankingValueForProperty(s);
					System.out.println("max for " + s + " in " + table.getName() 
							+ " is " + max);
				}
			}
			
			//Normalize all the value by dividing through the max value
			// and multiple by preference
			for (PropertiesRankingValuesTable table : endpointProperties) {
				float normalized = table.getRankingValueForProperty(s)/max;
				normalized *= rankingprops.getPropertyImportance(s);
				table.setRankingValueForProperty(s, normalized);
			}
		}
		
		
		Collections.sort(endpointProperties);
		
		
		//Show results
		System.out.println("RESULTS: ");
		
		int rank=1;
		for (PropertiesRankingValuesTable t: endpointProperties) {
			System.out.println("RANK: " + rank + "\n"+t);
			rank++;
		}
				
	
		
	}
			
}
