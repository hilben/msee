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
package at.sti2.wsmf.api.ws.channel.data;

import java.net.URL;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;

/**
 * @author Alex Oberhauser
 */
public class QoSChannelResponse {
	public URL endpointURL;
	public QoSParamKey type;
	public QoSUnit unit;
	public String value;
	
	public QoSChannelResponse() {}
	
	public QoSChannelResponse(URL _endpointURL, QoSParamKey _type, String _value, QoSUnit _unit) {
		this.endpointURL = _endpointURL;
		this.type = _type;
		this.value = _value;
		this.unit = _unit;
	}

	public URL getEndpointURL() { return this.endpointURL; }
	public QoSParamKey getType() { return this.type; }
	public QoSUnit getUnit() { return this.unit; }
	public String getValue() { return this.value; }
}
