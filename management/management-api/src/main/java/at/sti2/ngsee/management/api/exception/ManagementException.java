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
package at.sti2.msee.management.api.exception;

/**
 * <b>Purpose:</b> This exception indicates an error during execution of the registration engine.<br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2012 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Corneliu Stanciu<br>
 * @version     $Id$<br>
 * Date of creation:  23.05.2012<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */

public class ManagementException extends Exception {
	private static final long serialVersionUID = -8215018199535639439L;

	public ManagementException() {
		super();
	}
	
	public ManagementException(String message) {
		super(message);
	}
	
	public ManagementException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ManagementException(Throwable cause) {
        super(cause);
    }
}
