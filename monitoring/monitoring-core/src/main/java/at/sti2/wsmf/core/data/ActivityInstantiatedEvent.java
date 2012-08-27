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
import java.util.UUID;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import at.sti2.util.triplestore.QueryHelper;
import at.sti2.wsmf.api.data.state.WSInvocationState;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.common.Config;

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
	
	public ActivityInstantiatedEvent() throws IOException, RepositoryException {
		this.identifier = UUID.randomUUID().toString();
		this.state = WSInvocationState.None;
		Config cfg = Config.getDefaultConfig();
		this.subject = cfg.getInstancePrefix() + this.identifier;
		this.persHandler = PersistentHandler.getInstance();
		
		this.persHandler.updateResourceTriple(this.subject, QueryHelper.getRDFURI("type"), QueryHelper.getWSMFURI("InvocationInstance"), this.subject);
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
			this.persHandler.updateResourceTriple(this.subject, QueryHelper.getWSMFURI("state"), QueryHelper.WSMF_NS + this.state, this.subject);
			this.persHandler.updateResourceTriple(this.subject, QueryHelper.getWSMFURI("relatedTo"), QueryHelper.WSMF_NS + this.endpoint, this.subject);
			log.info("[" + this.identifier + "] Change Status from '" + oldState + "' to '" + _state + "'");	
		} catch (RepositoryException e) {
			log.error("Not able to store invocation instance state in triplestore.");	
		}
	}
	
	public WSInvocationState getState() {
		return this.state;
	}
	
	public String getEndpoint() {
		return this.endpoint;
	}
	
}
