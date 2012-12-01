/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package at.sti2.wsmf.ws.json.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.data.qos.QoSParamAtTime;
import at.sti2.wsmf.ws.json.chartcore.ChartColumnDataTableEntry;
import at.sti2.wsmf.ws.json.chartcore.GoogleChartJSONDataTableCreator;

// The Java class will be hosted at the URI path "/monitoringjson"

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 *         TODO: clean up, refactoring...
 */
@Path("/monitoringjson")
public class ChartResource {

	@GET
	@Produces({ "application/javascript" })
	public String getSomeStuff(@QueryParam("endpoint") List<String> endpoints,
			@QueryParam("qosParamKey") List<String> qosParamKeys) {

		return "ajaxCallSucceed(" + asJson(endpoints, qosParamKeys).toString()
				+ ")";
	}

	/**
	 * @return
	 * 
	 */
	private JSONObject asJson(List<String> endpoints, List<String> qosParamKeys) {

		PersistentHandler ph = null;
		try {
			ph = PersistentHandler.getInstance();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		GoogleChartJSONDataTableCreator j = new GoogleChartJSONDataTableCreator();

		for (String endpoint : endpoints) {
			System.out.println("Add endpoint to json: " + endpoint);
			j.addColum(new ChartColumnDataTableEntry("endpoint", endpoint,
					"datetime"));
		}
		for (String qosParamKey : qosParamKeys) {
			j.addColum(new ChartColumnDataTableEntry(qosParamKey, qosParamKey,
					"number"));
		}

		// Iterate over all endpoints
		for (String endpoint : endpoints) {

			int countQoSParams = 1;

			// For all qosParamKeys receive them
			for (String qosParamKey : qosParamKeys) {

				List<QoSParamAtTime> data = new ArrayList<QoSParamAtTime>();

				// Receive data of this QoSParam
				try {
					data = ph
							.getQoSTimeframe(endpoint, qosParamKey, null, null);
				} catch (Exception e) {
					e.printStackTrace();// TODO: exception handling
				}

				for (QoSParamAtTime p : data) {

					String row[] = new String[qosParamKeys.size() + 1];

//					row[0] = "\"" + p.getTimeForGoogleCharts() + "\"";
					row[0] = "\"" + p.getTime() + "\"";
					for (int i = 1; i < row.length; i++) {
						row[i] = "null";
					}
					row[countQoSParams] = p.getQosParamValue();
					j.addRow(row);
				}

				countQoSParams++;
			}
		}

		System.out.println(j.toJSON());

		JSONObject jsonobj = null;
		try {
			jsonobj = new JSONObject(j.toJSON());
		} catch (JSONException e) {
			// Fatal Error
			e.printStackTrace();
		}

		return jsonobj;

	}

	public static void main(String args[]) throws Exception {

		long time = System.currentTimeMillis();

		ArrayList<String> endpoints = new ArrayList<String>();
		ArrayList<String> qosParamKeys = new ArrayList<String>();
		endpoints
				.add("http://localhost:9292/at.sti2.ngsee.testwebservices/services/reversestring");
		qosParamKeys.add("ResponseTime");
//		qosParamKeys.add("PayloadsizeResponse");
//		qosParamKeys.add("PayloadsizeRequest");
		// qosParamKeys.add("AvailableTime");
		System.out.println(new ChartResource().asJson(endpoints, qosParamKeys));
		System.out.println("Done in " + (System.currentTimeMillis() - time));
	}
}