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

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import at.sti2.util.triplestore.QueryHelper;
import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.state.WSInvocationState;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Alex Oberhauser
 */
public class ActivityInstantiatedEvent {
	Logger log = Logger.getLogger(ActivityInstantiatedEvent.class);

	private String endpoint = null;

	private final String identifier;

	private WSInvocationState state;

	private String subject;

	private PersistentHandler persHandler;

	public ActivityInstantiatedEvent(String endpoint) throws IOException,
			RepositoryException {
		this.identifier = UUID.randomUUID().toString();
		this.state = WSInvocationState.None;
		WebServiceEndpointConfig cfg = WebServiceEndpointConfig
				.getConfig(endpoint);
		this.setEndpoint(endpoint);
		this.subject = cfg.getInstancePrefix() + this.identifier;
		this.persHandler = PersistentHandler.getInstance();

		this.persHandler.updateResourceTriple(this.subject,
				QueryHelper.getRDFURI("type"),
				QueryHelper.getWSMFURI("InvocationInstance"), this.subject);
	}

	public void setEndpoint(String _endpoint) {
		this.endpoint = _endpoint;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void changeInvocationStatus(WSInvocationState _state) {
		WSInvocationState oldState = this.state;
		this.state = _state;
		try {

			// NEW TODO:
			String invocationState = QueryHelper.getWSMFURI("invocationState_"
					+ UUID.randomUUID().toString());
			this.persHandler.updateResourceTriple(invocationState,
					QueryHelper.getWSMFURI("state"),
					QueryHelper.getWSMFURI(this.state.name()), this.subject);

			this.persHandler.updateResourceTriple(invocationState,
					QueryHelper.getRDFURI("type"),
					QueryHelper.getWSMFURI("InvocationState"), this.subject);

			this.persHandler.updateLiteralTriple(invocationState,
					QueryHelper.getWSMFURI("time"), (new Date()).toString(),
					this.subject);
			this.persHandler.addResourceTriple(this.subject,
					QueryHelper.getWSMFURI("hasInvocationState"),
					invocationState, this.subject);

			// OLD
			// this.persHandler.updateResourceTriple(this.subject,
			// QueryHelper.getWSMFURI("state"), QueryHelper.WSMF_NS
			// + this.state, this.subject);
			this.persHandler.updateResourceTriple(this.subject,
					QueryHelper.getWSMFURI("relatedTo"), QueryHelper.WSMF_NS
							+ this.endpoint, this.subject);

			log.info("[" + this.identifier + "] Change Status from '"
					+ oldState + "' to '" + _state + "'");
		} catch (RepositoryException e) {
			log.error("Not able to store invocation instance state in triplestore.");
			log.error(e.getCause());
		}
	}

	public WSInvocationState getState() {
		return this.state;
	}

	public String getEndpoint() {
		return this.endpoint;
	}

	/**
	 * @param _soapMessageSize
	 *            TODO: good solution? test!
	 */
	public void setPayloadSizeRequest(long _soapMessageSize) {
		try {
			this.persHandler.updateLiteralTriple(this.subject,
					QueryHelper.getWSMFURI("PayloadsizeRequest"),
					String.valueOf(_soapMessageSize), this.subject);
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
	}

	/**
	 * @param milliseconds
	 */
	public void setResponseTime(long milliseconds) {
		try {
			this.persHandler.updateLiteralTriple(this.subject,
					QueryHelper.getWSMFURI("ResponseTime"),
					String.valueOf(milliseconds), this.subject);
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
	}

	/**
	 * @param _soapMessageSize
	 */
	public void setPayloadSizeResponse(long _soapMessageSize) {
		try {
			this.persHandler.updateLiteralTriple(this.subject,
					QueryHelper.getWSMFURI("PayloadSizeResponse"),
					String.valueOf(_soapMessageSize), this.subject);
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}
	}

}
