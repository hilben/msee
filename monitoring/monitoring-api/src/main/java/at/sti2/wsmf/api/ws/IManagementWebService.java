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
package at.sti2.wsmf.api.ws;

import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.jws.WebParam;

import at.sti2.wsmf.api.data.qos.IQoSParamValue;
import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdValue;
import at.sti2.wsmf.api.data.qos.ranking.QoSRankingPreferencesTemplate;
import at.sti2.wsmf.api.data.state.WSInvocationState;

/**
 * Monitoring Management Interface that provides the following functionaliy:
 * 
 * <ul>
 * 		<li>Invocation State API: Returns the current state of the invokation process.</li>
 * 		<li>QoS API: Read out Quality of Service values.</li>
 * 		<li>Adaption API: Change QoS Threshold values</li>
 * </ul>
 * 
 * @author Alex Oberhauser
 */
/**
 * @author Benjamin Hiltpolt
 * 
 */
public interface IManagementWebService {

	/**
	 * @param endpoint
	 * @param key
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception
	 */
	public Float[] getQoSParametersInTimeFrame(@WebParam(name="endpoint") URL endpoint,@WebParam(name="QoSParamKey") QoSParamKey key,
			@WebParam(name="datefrom") Date from, @WebParam(name="dateto") Date to) throws Exception;

	/*
	 * Common Operations
	 */
	public String[] listInstanceIDs() throws Exception;

	/*
	 * Invocation State API
	 */
	public WSInvocationState getInvocationState(String activityStartedEvent)
			throws Exception;

	/*
	 * QoS API
	 */
	/**
	 * @param key
	 *            {@link QoSParamKey}
	 * @return {@link IQoSParamValue}
	 */
	public IQoSParamValue getQoSParam(URL endpoint, QoSParamKey key)
			throws Exception;

	public void changeQoSThresholdValue(URL endpoint, QoSThresholdValue value)
			throws Exception;

	public QoSThresholdValue getQoSThresholdValue(URL endpoint,
			QoSThresholdKey key) throws Exception;

	/**
	 * 
	 * Returns all endpoints currently monitored by the monitoring framework
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] listEndpoints() throws Exception;

	/**
	 * @param qosRankingTemplate
	 * @param endpoints
	 * @return
	 * @throws Exception
	 */
	public List<String> getQoSRankedEndpoints(QoSParamKey[] key,
			Float[] preferenceValue, String[] endpoints) throws Exception;

	/**
	 * 
	 * TODO: Dummy function / conceptional work missing
	 * 
	 * Returns all categories
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getCategories() throws Exception;

	/**
	 * TODO: Dummy function / conceptional work missing
	 * 
	 * Returns all endpoints part of a certain category
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getEndpointsForCategory(String category) throws Exception;
	
	
	/**
	 * 
	 * TODO: Dummy function
	 * 
	 * Returns a String list storing all subcategories and endpoints of a category.
	 * Keep in mind that it will only return the endpoints of a category not the endpoints of the categorys subcategories.
	 * 
	 * 
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public List<String> getSubcategoriesAndServices(String category) throws Exception;
}
