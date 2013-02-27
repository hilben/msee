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
package at.sti2.wsmf.core.data.channel;

/**
 * @author Alex Oberhauser
 */
public class ChannelSubscriber {

	private final String endpoint;

	private final String namespace;

	private final String operationName;

	private final String soapAction;

	public ChannelSubscriber(String _endpoint, String _namespace,
			String _operationName, String _soapAction) {
		this.endpoint = _endpoint;
		this.namespace = _namespace;
		this.operationName = _operationName;
		this.soapAction = _soapAction;
	}

	public String getEndpoint() {
		return this.endpoint;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getOperationName() {
		return this.operationName;
	}

	public String getSoapAction() {
		return this.soapAction;
	}
}
