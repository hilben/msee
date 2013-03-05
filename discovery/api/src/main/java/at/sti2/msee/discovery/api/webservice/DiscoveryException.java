/**
 * Copyright (C) 2012 STI Innsbruck, UIBK
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
package at.sti2.msee.discovery.api.webservice;

public class DiscoveryException extends Exception {

	private static final long serialVersionUID = -2289179077479549018L;

	public DiscoveryException() {
		super();
	}

	public DiscoveryException(String message) {
		super(message);
	}

	public DiscoveryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DiscoveryException(Throwable cause) {
		super(cause);
	}
}
