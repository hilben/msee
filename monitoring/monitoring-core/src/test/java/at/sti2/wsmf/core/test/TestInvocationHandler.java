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
package at.sti2.wsmf.core.test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import junit.framework.Assert;

import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.core.InvocationHandler;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;

/**
 * @author Alex Oberhauser
 */
public class TestInvocationHandler {
	
//	@Test
	public void testInvocationHandler() throws RepositoryException, Exception {
		List<QName> soapHeaderList = new ArrayList<QName>();
		soapHeaderList.add(new QName("http://example.org", "someTestingProperty"));

		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage message = messageFactory.createMessage();
		SOAPHeader soapHeader = message.getSOAPHeader();
		for ( QName entry : soapHeaderList ) {
			soapHeader.addChildElement(entry);
		}

		SOAPBody soapBody = message.getSOAPBody();
		SOAPElement operationElement = soapBody.addChildElement(new QName("http://see.sti2.at/", "invoke", "see"));
		operationElement.addChildElement("serviceID").setTextContent("http://www.sti2.at/sesa/service/WeatherService");
		operationElement.addChildElement("operation").setTextContent("GetWeather");	
		operationElement.addChildElement("inputData").setTextContent("<GetWeather xmlns='http://www.webserviceX.NET'><CountryName>Austria</CountryName><CityName>Innsbruck</CityName></GetWeather>");

		message.saveChanges();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		String responseMessage = InvocationHandler.invoke(message, null, new ActivityInstantiatedEvent(), os.size());
		
		System.out.println(responseMessage);
		Assert.assertNotNull(responseMessage);
		
//		System.out.println(InvocationHandler.isWebServiceAvailable("http://example.org", null));
//		System.out.println(Config.getInstance().getEndpointMaster());
//		System.out.println(InvocationHandler.isWebServiceAvailable(Config.getInstance().getEndpointMaster(), null));
	}
}
