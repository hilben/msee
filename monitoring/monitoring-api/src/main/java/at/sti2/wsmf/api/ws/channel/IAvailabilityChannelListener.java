/**
 * wsmf-api - at.sti2.wsmf.api.ws.channel
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

package at.sti2.wsmf.api.ws.channel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.sti2.wsmf.api.ws.channel.data.AvailabilityChannelResponse;

/**
 * @author Alex Oberhauser
 */
@WebService(name="AvailabilityChannelListener", targetNamespace="http://monitoring.sti2.at/ns#")
public interface IAvailabilityChannelListener {

	/**
	 * @param _event
	 * @throws Exception
	 */
	@WebMethod(operationName="getAvailabilityChangedEvent")
	public void getAvailabilityChangedEvent(@WebParam(name="event")AvailabilityChannelResponse event) throws Exception;
}
