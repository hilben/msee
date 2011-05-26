/**
 * IManagementWebService.java - at.sti2.wsmf.api.ws
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

	/*
	 * Adaption API
	 */
	public void addEndpoint(URL endpoint) throws Exception;
	public URL[] listEndpoints() throws Exception;
	public void removeEndpoint(URL endpoint) throws Exception;

}
