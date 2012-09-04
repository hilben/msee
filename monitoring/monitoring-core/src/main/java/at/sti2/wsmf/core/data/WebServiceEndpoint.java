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
package at.sti2.wsmf.core.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.util.triplestore.QueryHelper;
import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;
import at.sti2.wsmf.api.data.state.WSAvailabilityState;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
import at.sti2.wsmf.core.common.DateHelper;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Alex Oberhauser
 */
public class WebServiceEndpoint {
	private static Logger log = Logger.getLogger(WebServiceEndpoint.class);
	
	private final URL endpoint;
	private WSAvailabilityState availabilitySate;
	private PersistentHandler persHandler;
	
	public WebServiceEndpoint(URL _endpoint, boolean _store) throws FileNotFoundException, IOException {
		this.endpoint = _endpoint;
		this.persHandler = PersistentHandler.getInstance();
		this.initAvailabilityState();
	}
	
	public WebServiceEndpoint(URL _endpoint) throws FileNotFoundException, IOException, RepositoryException {
		this.endpoint = _endpoint;
		this.persHandler = PersistentHandler.getInstance();
		String subject = this.endpoint.toExternalForm();
		WebServiceEndpointConfig cfg = WebServiceEndpointConfig.getConfig(subject);
		String webserviceURI = cfg.getInstancePrefix() + cfg.getWebServiceName();
		
		this.persHandler.updateResourceTriple(subject, QueryHelper.getRDFURI("type"), QueryHelper.getWSMFURI("Endpoint"), subject);
		this.persHandler.updateResourceTriple(subject, QueryHelper.getWSMFURI("isRelatedToWebService"),
				webserviceURI, subject); 
		
		this.persHandler.commit();//delete?
		
		this.initAvailabilityState();
	}
	
	private synchronized void initAvailabilityState() {
		try {;
			this.availabilitySate = this.persHandler.getWSAvailabilityState(this.endpoint.toExternalForm());
		} catch (QueryEvaluationException e) {
			log.error("Not endpoint status found! Setting to 'not checked'.");
			this.availabilitySate = WSAvailabilityState.WSNotChecked;
		} catch (RepositoryException e) {
			log.error("Not endpoint status found! Setting to 'not checked'.");
			this.availabilitySate = WSAvailabilityState.WSNotChecked;
		} catch (MalformedQueryException e) {
			log.error("Not endpoint status found! Setting to 'not checked'.");
			this.availabilitySate = WSAvailabilityState.WSNotChecked;
		}
	}
	
	private synchronized int getRequests(QoSParamKey _key) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		return new Integer(this.persHandler.getQoSValue(this.endpoint.toExternalForm(), _key));
	}
	
	private synchronized long getLongValue(QoSParamKey _key) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		return new Long(this.persHandler.getQoSValue(this.endpoint.toExternalForm(), _key));
	}
	
	public synchronized void changeAvailabilityStatus(WSAvailabilityState _state) throws RepositoryException {
		this.availabilitySate = _state;
		String subject = this.endpoint.toExternalForm();
		this.persHandler.updateResourceTriple(subject, QueryHelper.getWSMFURI("availabilityState"),
				QueryHelper.getWSMFURI(this.availabilitySate.name()) , subject);
	}
	
	private synchronized int _incrementRequests(QoSParamKey _key) throws QueryEvaluationException, RepositoryException, MalformedQueryException, IOException {
		int oldValue = this.getRequests(_key);
		int newValue = oldValue + 1;
		log.info("Increment '" + _key.name() + "' to " + newValue);
		QoSParamValue value = new QoSParamValue(_key, newValue + "", QoSUnit.Requests);
		this.changeQoSValue(value);
		return newValue;
	}
	
	public synchronized int incrementeTotalRequests() throws QueryEvaluationException, RepositoryException, MalformedQueryException, IOException {
		return this._incrementRequests(QoSParamKey.RequestTotal);
	}
	
	public synchronized int incrementeFailedRequests() throws QueryEvaluationException, RepositoryException, MalformedQueryException, IOException {
		return this._incrementRequests(QoSParamKey.RequestFailed);
	}
	
	public synchronized int incrementeSuccessfulRequests() throws QueryEvaluationException, RepositoryException, MalformedQueryException, IOException {
		return this._incrementRequests(QoSParamKey.RequestSuccessful);
	}
	
	public Vector<QoSParamValue> changeResponseTime(long _responseTimeMS) throws RepositoryException, IOException, QueryEvaluationException, MalformedQueryException {
		Vector<QoSParamValue> changedValues = new Vector<QoSParamValue>();
		/*
		 * Update Average Value
		 */
		long oldAverageValue = this.getLongValue(QoSParamKey.ResponseTimeAverage);
		long newAverageValue;
		if ( oldAverageValue != 0 )
			newAverageValue = (oldAverageValue + _responseTimeMS) / 2;
		else
			newAverageValue = _responseTimeMS;
		log.info("Average response time changed to " + newAverageValue);
		QoSParamValue averageValue = new QoSParamValue(QoSParamKey.ResponseTimeAverage, newAverageValue + "", QoSUnit.Milliseconds);
		this.changeQoSValue(averageValue);
		changedValues.add(averageValue);
		
		/*
		 * Minimum and Maximum
		 */
		long oldMinimumValue = this.getLongValue(QoSParamKey.ResponseTimeMinimum);
		if ( _responseTimeMS < oldMinimumValue || oldMinimumValue == 0) {
			log.info("Minimum response time changed to " + _responseTimeMS);
			QoSParamValue minimumAverage = new QoSParamValue(QoSParamKey.ResponseTimeMinimum, _responseTimeMS + "", QoSUnit.Milliseconds);
			this.changeQoSValue(minimumAverage);
			changedValues.add(minimumAverage);
		}
		
		long oldMaximumValue = this.getLongValue(QoSParamKey.ResponseTimeMaximum);
		if ( _responseTimeMS > oldMaximumValue || oldMaximumValue == 0 ) {
			log.info("Maximum response time changed to " + _responseTimeMS);
			QoSParamValue maximumAverage = new QoSParamValue(QoSParamKey.ResponseTimeMaximum, _responseTimeMS + "", QoSUnit.Milliseconds);
			this.changeQoSValue(maximumAverage);
			changedValues.add(maximumAverage);
		}
		return changedValues;
	}
	
	public void changePayloadSize(int _payloadKB) throws QueryEvaluationException, RepositoryException, MalformedQueryException, IOException {
		long oldAveragePayload = this.getLongValue(QoSParamKey.PayloadSizeAverage);
		long newAverageValue;
		if ( oldAveragePayload != 0 )
			newAverageValue = ( oldAveragePayload + _payloadKB ) / 2;
		else
			newAverageValue = _payloadKB;
		log.info("Average payload size changed to " + newAverageValue);
		QoSParamValue averageValue = new QoSParamValue(QoSParamKey.PayloadSizeAverage, newAverageValue + "", QoSUnit.Bytes);
		this.changeQoSValue(averageValue);
		
		/*
		 * Minimum and Maximum
		 */
		long oldMinimumValue = this.getLongValue(QoSParamKey.PayloadSizeMinimum);
		if ( _payloadKB < oldMinimumValue || oldMinimumValue == 0) {
			log.info("Minimum payload size changed to " + _payloadKB);
			QoSParamValue minimumAverage = new QoSParamValue(QoSParamKey.PayloadSizeMinimum, _payloadKB + "", QoSUnit.Bytes);
			this.changeQoSValue(minimumAverage);
		}
		
		long oldMaximumValue = this.getLongValue(QoSParamKey.PayloadSizeMaximum);
		if ( _payloadKB > oldMaximumValue || oldMaximumValue == 0 ) {
			log.info("Maximum payload size changed to " + _payloadKB);
			QoSParamValue maximumAverage = new QoSParamValue(QoSParamKey.PayloadSizeMaximum, _payloadKB + "", QoSUnit.Bytes);
			this.changeQoSValue(maximumAverage);
		}
	}
	
	public void changeQoSValue(QoSParamValue _value) throws IOException, RepositoryException {
		String endpointString = this.endpoint.toExternalForm();
		String qosParamID = PersistentHandler.getQoSParamID(endpointString, _value.getType());
		
		this.persHandler.addResourceTriple(endpointString, QueryHelper.getWSMFURI("hasQoSParam"), qosParamID, endpointString);
		
		this.persHandler.updateResourceTriple(qosParamID,
				QueryHelper.getRDFURI("type"), QueryHelper.getWSMFURI("QoSParam"),
				endpointString);
		
		this.persHandler.updateResourceTriple(qosParamID,
				QueryHelper.getWSMFURI("type"), QueryHelper.WSMF_NS + _value.getType().name(),
				endpointString);
		
		this.persHandler.updateResourceTriple(qosParamID,
				QueryHelper.getWSMFURI("unit"), QueryHelper.WSMF_NS + _value.getUnit().name(),
				endpointString);
		
		this.persHandler.updateLiteralTriple(qosParamID,
				QueryHelper.getWSMFURI("value"), _value.getValue(),
				endpointString);
		
		this.persHandler.updateLiteralTriple(qosParamID,
				QueryHelper.getDCURI("modified"), DateHelper.getXSDDateTime(),
				endpointString);
	}
	
	public WSAvailabilityState getAvailabilityStatus() {
		return this.availabilitySate;
	}
	
	public URL getEndpoint() {
		return this.endpoint;
	}
	
}
