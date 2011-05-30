/**
 * wsmf-core - at.sti2.wsmf.core.data.channel
 *
 * Copyright (C) 2011 by Networld Project
 * Written by Alex Oberhauser <oberhauseralex@networld.to>
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

package at.sti2.wsmf.api.ws.channel.data;

import java.net.URI;

import at.sti2.wsmf.api.data.state.WSInvocationState;

/**
 * @author Alex Oberhauser
 */
public class InvocationStateChannelResponse {
	private static final long serialVersionUID = -7056578155174781128L;
	
	public URI instanceURI;
	public WSInvocationState state;
	
	public InvocationStateChannelResponse() {}
	
	public InvocationStateChannelResponse(URI _instanceURI, WSInvocationState _state) {
		this.instanceURI = _instanceURI;
		this.state = _state;
	}
	
	public URI getInstanceURI() { return this.instanceURI; }

	public WSInvocationState getState() { return this.state; }

}