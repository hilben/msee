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
package at.sti2.ngsee.invoker.api.core;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>    Copyright (c) 2011 STI<br>
 * <b>Company:</b>      STI Innsbruck<br>
 *
 * @author      		michaelrogger<br>
 * @author 				Alex Oberhauser<br>
 * @version     		$Id$<br>
 * Date of creation:  	17.03.2011<br>
 * File:         		$Source$<br>
 * Modifier:     		$Author$<br>
 * Revision:     		$Revision$<br>
 * State:        		$State$<br>
 */
public interface ISOAPInvoker {	

	/**
	 * 
	 * @param serviceName The qualified service name.
	 * @param portName The qualified port name.
	 * @param endpointURL Endpoint URL of the web service
	 * @param soapActionURI HTTP Header SOAPAction field.
	 * @param soapMessage The SOAP message to send to the webservice
	 * @return
	 */
	public SOAPMessage invoke(QName serviceName,
			QName portName,
			String endpointURL,
			String soapActionURI,
			SOAPMessage soapMessage) throws Exception;
	
}
