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

import at.sti2.wsmf.api.data.state.WSAvailabilityState;

/**
 * @author Alex Oberhauser
 */
public class AvailabilityChannelResponse {
	
	public URI endpointURI;
	public WSAvailabilityState availability;
	
	public AvailabilityChannelResponse() {}
	
	public AvailabilityChannelResponse(URI _endpointURI, WSAvailabilityState _availability) {
		this.endpointURI = _endpointURI;
		this.availability = _availability;
	}

	public URI getEndpointURI() { return this.endpointURI; }

	public WSAvailabilityState getAvailability() { return this.availability; }

}
