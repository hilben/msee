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
import java.util.UUID;

import org.apache.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.util.triplestore.QueryHelper;
import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;
import at.sti2.wsmf.api.data.state.WSAvailabilityState;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.common.DateHelper;
import at.sti2.wsmf.core.common.HashValueHandler;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Alex Oberhauser
 */
/**
 * @author Benjamin Hiltpolt
 * 
 *         represents an webservice endpoint by storing the url of the endpoint
 *         updates statuses, qos of the endpoint via the
 *         {@link PersistentHandler}
 * 
 */
public class WebServiceEndpoint {
	private static Logger log = Logger.getLogger(WebServiceEndpoint.class);

	private final URL endpoint;
	private WSAvailabilityState availabilitySate;
	private PersistentHandler persHandler;

	public WebServiceEndpoint(URL _endpoint, boolean _store)
			throws FileNotFoundException, IOException {
		this.endpoint = _endpoint;
		this.persHandler = PersistentHandler.getInstance();
		this.initAvailabilityState();
	}

	/**
	 * Creates a web service endpoint for the persistence handler (
	 * {@link PersistentHandler})
	 * 
	 * @param _endpoint
	 *            URL of the endpoint
	 * @throws IOException
	 *             can occur if there are problem initializing the persitence
	 *             handler
	 * @throws RepositoryException
	 */
	public WebServiceEndpoint(URL endpoint) throws IOException,
			RepositoryException {
		this.endpoint = endpoint;
		this.persHandler = PersistentHandler.getInstance();

		this.persHandler.createEndpoint(endpoint);

		this.initAvailabilityState();
	}

	/**
	 * 
	 * Initialize the availability state ({@link WSAvailabilityState})
	 */
	private synchronized void initAvailabilityState() {
		try {
			this.availabilitySate = this.persHandler
					.getWSAvailabilityState(this.endpoint.toExternalForm());
		} catch (QueryEvaluationException e) {
			log.error("Not endpoint status found! Setting to 'not checked'.");
			log.error(e.getCause());
			this.availabilitySate = WSAvailabilityState.WSNotChecked;
		} catch (RepositoryException e) {
			log.error("Not endpoint status found! Setting to 'not checked'.");
			log.error(e.getCause());
			this.availabilitySate = WSAvailabilityState.WSNotChecked;
		} catch (MalformedQueryException e) {
			log.error("Not endpoint status found! Setting to 'not checked'.");
			log.error(e.getCause());
			this.availabilitySate = WSAvailabilityState.WSNotChecked;
		}

		this.persHandler.commit();
	}

	/**
	 * 
	 * Creates a new availability status with timestamp
	 * 
	 * @param _state
	 * @throws RepositoryException 
	 */
	public synchronized void changeAvailabilityStatus(
			WSAvailabilityState state, long updateTimeMinutes) throws RepositoryException {
		this.availabilitySate = state;

		this.persHandler.addAvailabilityStatus(this.endpoint, state,
				updateTimeMinutes);
	}

	/**
	 * Returns the AvailabilityStatus of the WebServiceEndpoint
	 * 
	 * @return
	 */
	public WSAvailabilityState getAvailabilityStatus() {
		return this.availabilitySate;
	}

	/**
	 * Returns the URL of the Endpoint of the WebService
	 * 
	 * @return
	 */
	public URL getEndpoint() {
		return this.endpoint;
	}

	public synchronized void addSuccessfullInvoke(double PayloadsizeRequest,
			double PayloadsizeResponse, double responseTime) {

		QoSParamValue qosRequestSize = new QoSParamValue(
				QoSParamKey.PayloadSizeRequest, PayloadsizeRequest,
				QoSUnit.Bytes);
		QoSParamValue qosResponseSize = new QoSParamValue(
				QoSParamKey.PayloadSizeResponse, PayloadsizeResponse,
				QoSUnit.Bytes);
		QoSParamValue qosResponseTime = new QoSParamValue(
				QoSParamKey.ResponseTime, responseTime, QoSUnit.Milliseconds);

		// Add new QoSParamsValues
		this.persHandler.addQoSValue(endpoint, qosRequestSize, false);
		this.persHandler.addQoSValue(endpoint, qosResponseSize, false);
		this.persHandler.addQoSValue(endpoint, qosResponseTime, false);

		// Update old last ones
		this.persHandler.addQoSValue(endpoint, qosRequestSize, true);
		this.persHandler.addQoSValue(endpoint, qosResponseSize, true);
		this.persHandler.addQoSValue(endpoint, qosResponseTime, true);

		// Update Averages
		qosRequestSize.setType(QoSParamKey.PayloadSizeRequestAverage);
		qosResponseSize.setType(QoSParamKey.PayloadSizeResponseAverage);
		qosResponseTime.setType(QoSParamKey.ResponseTimeAverage);

		
		this.persHandler.updateQoSValueAverage(endpoint, qosRequestSize);
		this.persHandler.updateQoSValueAverage(endpoint, qosResponseSize);
		this.persHandler.updateQoSValueAverage(endpoint, qosResponseTime);

		// Update Minima
		qosRequestSize.setType(QoSParamKey.PayloadSizeRequestMinimum);
		qosResponseSize.setType(QoSParamKey.PayloadSizeResponseMinimum);
		qosResponseTime.setType(QoSParamKey.ResponseTimeMinimum);

		this.persHandler.updateQoSValueMinimum(endpoint, qosRequestSize);
		this.persHandler.updateQoSValueMinimum(endpoint, qosResponseSize);
		this.persHandler.updateQoSValueMinimum(endpoint, qosResponseTime);

		// Update Maxima
		qosRequestSize.setType(QoSParamKey.PayloadSizeRequestMaximum);
		qosResponseSize.setType(QoSParamKey.PayloadSizeResponseMaximum);
		qosResponseTime.setType(QoSParamKey.ResponseTimeMaximum);

		this.persHandler.updateQoSValueMaximum(endpoint, qosRequestSize);
		this.persHandler.updateQoSValueMaximum(endpoint, qosResponseSize);
		this.persHandler.updateQoSValueMaximum(endpoint, qosResponseTime);

		// Update Total
		qosRequestSize.setType(QoSParamKey.PayloadSizeRequestTotal);
		qosResponseSize.setType(QoSParamKey.PayloadSizeResponseTotal);
		qosResponseTime.setType(QoSParamKey.ResponseTimeTotal);

		this.persHandler.updateQoSValueTotal(endpoint, qosRequestSize);
		this.persHandler.updateQoSValueTotal(endpoint, qosResponseSize);
		this.persHandler.updateQoSValueTotal(endpoint, qosResponseTime);
		
		this.persHandler.commit();
	}

	public synchronized void addUnsuccessfullInvoke(double PayloadsizeRequest) {
//TODO:
		
		this.persHandler.commit();
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public double incrementeTotalRequests() throws Exception {
		QoSParamValue value = new QoSParamValue(QoSParamKey.RequestTotal, 1,
				QoSUnit.Requests);
		this.persHandler.updateQoSValueTotal(this.endpoint, value);

		// TODO: make return
		return this.persHandler.getQoSParamValue(endpoint, QoSParamKey.RequestTotal);
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public double incrementeSuccessfulRequests() throws Exception {
		QoSParamValue value = new QoSParamValue(QoSParamKey.RequestSuccessful,
				1, QoSUnit.Requests);
		this.persHandler.updateQoSValueTotal(this.endpoint, value);

		// TODO: make return
		return this.persHandler.getQoSParamValue(endpoint, QoSParamKey.RequestSuccessful);
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public double incrementeFailedRequests() throws Exception {
		QoSParamValue value = new QoSParamValue(QoSParamKey.RequestFailed, 1,
				QoSUnit.Requests);
		this.persHandler.updateQoSValueTotal(this.endpoint, value);

		return this.persHandler.getQoSParamValue(endpoint, QoSParamKey.RequestFailed);
	}

}
