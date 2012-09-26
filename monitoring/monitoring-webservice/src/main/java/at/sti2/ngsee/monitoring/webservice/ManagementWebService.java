/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.ngsee.monitoring.webservice;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdValue;
import at.sti2.wsmf.api.data.qos.ranking.QoSRankingPreferencesTemplate;
import at.sti2.wsmf.api.data.state.WSInvocationState;
import at.sti2.wsmf.api.ws.IManagementWebService;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.data.qos.QoSParamValue;
import at.sti2.wsmf.core.ranking.QoSParamsEndpointRankingTable;
import at.sti2.wsmf.core.ranking.QoSRankingEngine;

/**
 * @author Alex Oberhauser
 * 
 * @author Benjamin Hiltpolt
 */
@WebService
public class ManagementWebService implements IManagementWebService {

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#listInstanceIDs()
	 */
	@WebMethod
	@Override
	public String[] listInstanceIDs() throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		Vector<String> resultVector = persHandler.getInstanceIDs();
		return resultVector.toArray(new String[resultVector.size()]);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getInvocationState(java.lang.String)
	 */
	@WebMethod
	@Override
	public WSInvocationState getInvocationState(
			@WebParam(name = "activityStartedEvent") String _activityStartedEvent)
			throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		return persHandler.getInvocationState(_activityStartedEvent);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getQoSParam(java.lang.String)
	 */
	@WebMethod
	@Override
	public QoSParamValue getQoSParam(
			@WebParam(name = "endpoint") URL _endpoint,
			@WebParam(name = "key") QoSParamKey _key) throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		return persHandler.getQoSParam(_endpoint, _key);
	}
	
	
	public String getQoSParam2(
			@WebParam(name = "endpoint") URL _endpoint,
			@WebParam(name = "key") QoSParamKey _key) throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		return persHandler.getQoSParam(_endpoint, _key).toString();
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#changeQoSThresholdValue(java.net.URL,
	 *      at.sti2.wsmf.api.data.qos.IQoSThresholdValue)
	 */
	@WebMethod
	@Override
	public void changeQoSThresholdValue(
			@WebParam(name = "endpoint") URL _endpoint,
			@WebParam(name = "value") QoSThresholdValue _value)
			throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		persHandler.changeQoSThresholdValue(_endpoint, _value);
	}

	/**
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getQoSThresholdValue(java.net.URL,
	 *      at.sti2.wsmf.api.data.qos.QoSParamKey)
	 */
	@WebMethod
	@Override
	public QoSThresholdValue getQoSThresholdValue(
			@WebParam(name = "endpoint") URL _endpoint,
			@WebParam(name = "key") QoSThresholdKey _key) throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();
		return persHandler.getThresholdValue(_endpoint, _key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#listEndpoints()
	 */
	@Override
	public String[] listEndpoints() throws Exception {
		PersistentHandler persHandler = PersistentHandler.getInstance();

		List<String> eps = persHandler.getEndpoints();

		String[] ret = new String[eps.size()];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = eps.get(i);
		}

		return ret;
	}


	/* (non-Javadoc)
	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getQoSRankedEndpoint(QoSRankingPreferencesTemplate, String[])
	 */
	// TODO: put in other class 
	// TODO: logging
	// TODO: testing
	public List<String> getQoSRankedEndpoints(QoSParamKey[] keys,Float[] preferenceValues, String[] endpoints) throws Exception{
		return QoSRankingEngine.getQoSRankedEndpoints(keys, preferenceValues, endpoints);

	}
	

	public static void main(String args[]) throws Exception {
		// Set up the ranked QoSParams and fill the corresponding tables
		QoSRankingPreferencesTemplate qosRankingTemplate = new QoSRankingPreferencesTemplate();

		
		//Create random preferences for testing purposes
		for (QoSParamKey q : QoSParamKey.values()) {
			qosRankingTemplate.addPropertyAndImportance(q,	(float)(Math.random()+0.1));
		}
		
//		System.out.println(new ManagementWebService().getQoSRankedEndpoints(qosRankingTemplate,new ManagementWebService().listEndpoints()));
	}




}
