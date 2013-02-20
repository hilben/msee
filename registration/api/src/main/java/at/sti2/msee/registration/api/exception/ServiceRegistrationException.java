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
package at.sti2.msee.registration.api.exception;

public class ServiceRegistrationException extends Exception {

	private static final long serialVersionUID = -2289179077479549018L;
	
	public ServiceRegistrationException() {
		super();
	}
	
	public ServiceRegistrationException(String message) {
		super(message);
	}
	
	public ServiceRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ServiceRegistrationException(Throwable cause) {
        super(cause);
    }
}
