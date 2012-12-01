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
	public WebServiceEndpoint(URL _endpoint) throws IOException,
			RepositoryException {
		this.endpoint = _endpoint;
		this.persHandler = PersistentHandler.getInstance();
		String subject = this.endpoint.toExternalForm();
		WebServiceEndpointConfig cfg = WebServiceEndpointConfig
				.getConfig(subject);
		String webserviceURI = cfg.getInstancePrefix()
				+ cfg.getWebServiceName();

		this.persHandler.updateResourceTriple(subject,
				QueryHelper.getRDFURI("type"),
				QueryHelper.getWSMFURI("Endpoint"), subject);
		this.persHandler.updateResourceTriple(subject,
				QueryHelper.getWSMFURI("isRelatedToWebService"), webserviceURI,
				subject);

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

	private synchronized int getRequests(QoSParamKey _key)
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException {
		return new Integer(this.persHandler.getQoSValue(
				this.endpoint.toExternalForm(), _key));
	}

	private synchronized long getLongValue(QoSParamKey _key)
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException {
		return new Long(this.persHandler.getQoSValue(
				this.endpoint.toExternalForm(), _key));
	}

	/**
	 * 
	 * Creates a new availability status with timestamp
	 * 
	 * @param _state
	 * @throws RepositoryException
	 */
	public synchronized void changeAvailabilityStatus(
			WSAvailabilityState _state, long updateTimeMinutes)
			throws RepositoryException {
		this.availabilitySate = _state;
		String subject = this.endpoint.toExternalForm();

		try {
			WebServiceEndpointConfig cfg = WebServiceEndpointConfig
					.getConfig(endpoint);

			String availabilityState = cfg.getInstancePrefix()
					+ "availabilityState_" + UUID.randomUUID().toString();
			this.persHandler.updateResourceTriple(availabilityState,
					QueryHelper.getWSMFURI("state"),
					QueryHelper.getWSMFURI(this.availabilitySate.name()),
					subject);
			this.persHandler.updateResourceTriple(availabilityState,
					QueryHelper.getRDFURI("type"),
					QueryHelper.getWSMFURI("AvailabilityState"), subject);
			this.persHandler.updateLiteralTriple(availabilityState,
					QueryHelper.getDCURI("datetime"),
					DateHelper.getXSDDateTime(), subject);

			/*
			 * updateTime should be at least 1; (TODO: throw exception?)
			 */
			if (updateTimeMinutes > 0) {
				this.persHandler.updateLiteralTriple(availabilityState,
						QueryHelper.getWSMFURI("nextAvailabilityCheckMinutes"),
						String.valueOf(updateTimeMinutes), subject);

				this.updateAvailabilityTime(updateTimeMinutes, _state);
			}
			this.persHandler.addResourceTriple(subject,
					QueryHelper.getWSMFURI("hasAvailabilityState"),
					availabilityState, subject);

			this.persHandler.commit();
		} catch (IOException e) {
			log.error("Error loading the configuration file");
		}
	}

	/**
	 * @throws RepositoryException
	 * TODO: modify (should not work now since there are multiple instances of the monitoredTime QoSParam...)
	 */
	private void updateAvailabilityTime(long updateTimeMinutes,
			WSAvailabilityState _state) throws RepositoryException {

		long monitoredTime = updateTimeMinutes;
		long availableTime = updateTimeMinutes;
		long unavailableTime = updateTimeMinutes;

		try {
			monitoredTime += this.getLongValue(QoSParamKey.MonitoredTime);
			this.addQoSValue(new QoSParamValue(QoSParamKey.MonitoredTime,
					monitoredTime + "", QoSUnit.Minutes));

		} catch (QueryEvaluationException e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		} catch (MalformedQueryException e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}

		if (_state == WSAvailabilityState.WSAvailable) {
			try {
				availableTime += this.getLongValue(QoSParamKey.AvailableTime);

				this.addQoSValue(new QoSParamValue(
						QoSParamKey.AvailableTime, availableTime + "",
						QoSUnit.Minutes));
			} catch (QueryEvaluationException e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
			} catch (MalformedQueryException e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
			}

		} else {
			try {
				unavailableTime += this
						.getLongValue(QoSParamKey.UnavailableTime);
				this.addQoSValue(new QoSParamValue(
						QoSParamKey.UnavailableTime, unavailableTime + "",
						QoSUnit.Minutes));

			} catch (QueryEvaluationException e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
			} catch (MalformedQueryException e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
			}

		}
	}

	private synchronized int _incrementRequests(QoSParamKey _key)
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException {
		int oldValue = this.getRequests(_key);
		int newValue = oldValue + 1;
		log.info("Increment '" + _key.name() + "' to " + newValue);
		QoSParamValue value = new QoSParamValue(_key, newValue + "",
				QoSUnit.Requests);
		this.addQoSValue(value);
		return newValue;
	}

	public synchronized int incrementeTotalRequests()
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException, IOException {
		return this._incrementRequests(QoSParamKey.RequestTotal);
	}

	public synchronized int incrementeFailedRequests()
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException, IOException {
		return this._incrementRequests(QoSParamKey.RequestFailed);
	}

	public synchronized int incrementeSuccessfulRequests()
			throws QueryEvaluationException, RepositoryException,
			MalformedQueryException, IOException {
		return this._incrementRequests(QoSParamKey.RequestSuccessful);
	}

	/**
	 * 
	 * Changes a QoSParameterValue
	 * 
	 *TODO:modify so that old QoSValues are not overwritten... --> DEBUG
	 * 
	 * @param _value
	 */
	public void addQoSValue(QoSParamValue _value) {
		String endpointString = this.endpoint.toExternalForm();
		String qosParamID;
		try {
			qosParamID = HashValueHandler.computeSHA256(endpointString+DateHelper.getXSDDateTime()+
					_value.getType());

			this.persHandler.addResourceTriple(endpointString,
					QueryHelper.getWSMFURI("hasQoSParam"), qosParamID,
					endpointString);

			this.persHandler.updateResourceTriple(qosParamID,
					QueryHelper.getRDFURI("type"),
					QueryHelper.getWSMFURI("QoSParam"), endpointString);

			this.persHandler.updateResourceTriple(qosParamID,
					QueryHelper.getWSMFURI("type"), QueryHelper.WSMF_NS
							+ _value.getType().name(), endpointString);

			this.persHandler.updateResourceTriple(qosParamID,
					QueryHelper.getWSMFURI("unit"), QueryHelper.WSMF_NS
							+ _value.getUnit().name(), endpointString);

			this.persHandler.updateLiteralTriple(qosParamID,
					QueryHelper.getWSMFURI("value"), _value.getValue(),
					endpointString);

			this.persHandler.updateLiteralTriple(qosParamID,
					QueryHelper.getDCURI("date"),
					DateHelper.getXSDDateTime(), endpointString);

			this.persHandler.commit();

		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
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

}
