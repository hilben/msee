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

import at.sti2.wsmf.api.data.qos.IQoSParamValue;
import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdValue;
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
public interface IManagementWebService {

	/*
	 * Common Operations
	 */
	public String[] listInstanceIDs() throws Exception;
	
	/*
	 * Invocation State API
	 */
	public WSInvocationState getInvocationState(String activityStartedEvent) throws Exception;
	
	/*
	 * QoS API
	 */
	/**
	 * @param key {@link QoSParamKey}
	 * @return {@link IQoSParamValue}
	 */
	public IQoSParamValue getQoSParam(URL endpoint, QoSParamKey key) throws Exception;
	
	public void changeQoSThresholdValue(URL endpoint, QoSThresholdValue value) throws Exception;
	public QoSThresholdValue getQoSThresholdValue(URL endpoint, QoSThresholdKey key) throws Exception;


}
