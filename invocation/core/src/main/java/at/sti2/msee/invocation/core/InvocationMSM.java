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
package at.sti2.msee.invocation.core;

import java.net.URL;

import javax.xml.namespace.QName;

import at.sti2.msee.invocation.api.core.IInvocationMSM;

/**
 * @author Alex Oberhauser
 */
public class InvocationMSM implements IInvocationMSM {
	private URL loweringSchema;


	private URL liftingSchema;
	private URL wsdl;
	private String soapAction;
	private QName operationQName;
	private QName serviceName;
	private QName portQName;
	private URL endpointURL;
	
	public void setLoweringSchema(URL _loweringSchema) {
		this.loweringSchema = _loweringSchema;
	}
	
	public void setLiftingSchema(URL _liftingSchema) {
		this.liftingSchema = _liftingSchema;
	}
	
	public void setWSDL(URL _wsdl) {
		this.wsdl = _wsdl;
	}
	
	public void setSOAPAction(String _soapAction) {
		this.soapAction = _soapAction;
	}
	
	public void setOperationQName(QName _operationQName) {
		this.operationQName = _operationQName;
	}
	
	/**
	 * @see at.sti2.msee.invocation.api.core.IinvocationMSM#getLoweringSchema()
	 */
	@Override
	public URL getLoweringSchema() { return this.loweringSchema; }

	/**
	 * @see at.sti2.msee.invocation.api.core.IinvocationMSM#getLifingSchema()
	 */
	@Override
	public URL getLifingSchema() { return this.liftingSchema; }

	/**
	 * @see at.sti2.msee.invocation.api.core.IinvocationMSM#getWSDL()
	 */
	@Override
	public URL getWSDL() { return this.wsdl; }
	
	/**
	 * @see at.sti2.msee.invocation.api.core.IinvocationMSM#getSOAPAction()
	 */
	@Override
	public String getSOAPAction() { return this.soapAction; }
	
	/**
	 * @see at.sti2.msee.invocation.api.core.IinvocationMSM#getOperationQName()
	 */
	@Override
	public QName getOperationQName() { return this.operationQName; }

	/**
	 * @param stringValue
	 */
	public void setServiceQName(QName _serviceName) {
		this.serviceName = _serviceName;		
	}
	
	@Override
	public QName getServiceQName() { return this.serviceName; }

	
	public void setPortQName(QName _portQName) {
		this.portQName = _portQName;
	}
	
	/**
	 * @see at.sti2.msee.invocation.api.core.IinvocationMSM#getPortQName()
	 */
	@Override
	public QName getPortQName() { return this.portQName; }

	public void setEndpointURL(URL _endpointURL) {
		this.endpointURL = _endpointURL;
	}
	
	/**
	 * @see at.sti2.msee.invocation.api.core.IinvocationMSM#getEndpointURL()
	 */
	@Override
	public URL getEndpointURL() { return this.endpointURL; }

	
	@Override
	public String toString() {
		return "invocationMSM [loweringSchema=" + loweringSchema
				+ ", liftingSchema=" + liftingSchema + ", wsdl=" + wsdl
				+ ", soapAction=" + soapAction + ", operationQName="
				+ operationQName + ", serviceName=" + serviceName
				+ ", portQName=" + portQName + ", endpointURL=" + endpointURL
				+ "]";
	}
}
