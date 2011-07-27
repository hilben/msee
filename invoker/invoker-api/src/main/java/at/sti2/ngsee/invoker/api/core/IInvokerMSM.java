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

import java.net.URL;

import javax.xml.namespace.QName;

/**
 * @author Alex Oberhauser
 */
public interface IInvokerMSM {
	
	/**
	 * @return A link to the lowering schema.
	 */
	public URL getLoweringSchema();
	
	/**
	 * @return A link to the lifting schema.
	 */
	public URL getLifingSchema();
	
	/**
	 * @return Link to the Web Service Description Language File.
	 */
	public URL getWSDL();
	
	/**
	 * @return SOAPAction specified in the HTTP header
	 */
	public String getSOAPAction();
	
	/**
	 * @return Namespace + Operation as QName instance.
	 */
	public QName getOperationQName();
	
	public QName getServiceQName();
	
	public QName getPortQName();
	
	public URL getEndpointURL();

}
