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
package at.sti2.ngsee.invoker.core.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import at.sti2.ngsee.invoker.api.core.ISOAPInvoker;

public class SoapJaxWSInvoker implements ISOAPInvoker {
	
	public SOAPMessage invoke(QName _serviceName, QName _portName,
			String _endpointUrl, String _soapActionUri, SOAPMessage _soapMessage)
			throws Exception {
		/** Create a service and add at least one port to it. **/
		Service service = Service.create(_serviceName);
		service.addPort(_portName, SOAPBinding.SOAP11HTTP_BINDING, _endpointUrl);

		/** Create a Dispatch instance from a service. **/
		Dispatch<SOAPMessage> dispatch = service.createDispatch(_portName, SOAPMessage.class,
				Service.Mode.MESSAGE);

		if ( null != _soapActionUri ) {
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, true);
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, _soapActionUri);
		} else
			dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, false);

		SOAPMessage response = (SOAPMessage) dispatch.invoke(_soapMessage);
		return response;
	}

}
