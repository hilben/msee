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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Scanner;

import javax.xml.rpc.ServiceException;



import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import at.sti2.msee.invocation.api.ServiceInvocation;
import at.sti2.msee.invocation.api.exception.ServiceInvokerException;

/**
 * @author Benjamin Hiltpolt
 * 
 *         TODO: Documentation
 */
public class ServiceInvocationImpl implements ServiceInvocation{
	protected static Logger logger = Logger.getLogger(ServiceInvocationImpl.class);


	/**
	 * 
	 * TODO: lowering, lifting
	 * TODO: monitoring,
	 * TODO: use service IDs not endpoints
	 * 
	 * @param serviceID
	 * @param operationName
	 * @param inputData
	 * @return
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws SAXException 
	 * @throws AxisFault 
	 */
	public String invoke(String serviceID,
			String operationName, String inputData) throws ServiceInvokerException  {

		String results="";
		String endpoint = serviceID;
		String soapFile = inputData;
		
		Service service = new Service();
		Call call;
		
		logger.debug("Invoking " + serviceID);
		logger.debug("Using SOAP-Message " + soapFile);
		logger.debug("Operation: " + operationName);
				
		try {
			
		call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		
		
		ByteArrayInputStream soapMessageStream = new ByteArrayInputStream(soapFile.getBytes("UTF-8"));
		
		SOAPEnvelope env = new SOAPEnvelope(soapMessageStream);

		results = call.invoke(env).toString();
		logger.debug("Obtainer results" + results);
		
		
		} catch (ServiceException | MalformedURLException | SAXException | AxisFault | UnsupportedEncodingException e) {
			logger.error(e);
			throw new ServiceInvokerException(e);
		}
		
		return results;
	}

}
