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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import junit.framework.TestCase;

import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.core.MonitoringInvocationHandler;
import at.sti2.wsmf.core.availability.WSAvailabilityChecker;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;

/**
 * @author Alex Oberhauser
 */
public class TestInvocationHandler extends TestCase {

	// @Test
	public void testInvocationHandler() throws RepositoryException, IOException {
		List<QName> soapHeaderList = new ArrayList<QName>();
		MessageFactory messageFactory;
		SOAPMessage message = null;
		SOAPHeader soapHeader = null;
		try {
			messageFactory = MessageFactory.newInstance();

			message = messageFactory.createMessage();
			soapHeader = message.getSOAPHeader();
			for (QName entry : soapHeaderList) {
				soapHeader.addChildElement(entry);
			}
		} catch (SOAPException e1) {
			e1.printStackTrace();
		}

		StringBuilder inputData = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(
					TestInvocationHandler.class.getResource(
							"/valencia-input.rdf.xml").getFile()));
		} catch (FileNotFoundException e) {

			System.exit(0);
			scanner = null;
			e.printStackTrace();
		}
		TestCase.assertNotNull(scanner);
		try {
			while (scanner.hasNextLine()) {
				inputData.append(scanner.nextLine() + NL);
			}
		} finally {
			scanner.close();
		}

		try {

			message.saveChanges();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			message.writeTo(os);
			String responseMessage = MonitoringInvocationHandler
					.invokeWithMonitoring(
							message,
							null,
							new ActivityInstantiatedEvent(
									"http://sesa.sti2.at:8080/invoker-dummy-webservice/services/valenciatPortWebService"),
							os.size());

			System.out.println(responseMessage);

		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(WSAvailabilityChecker.isWebServiceAvailable(
				"http://example.org", null));
		// System.out.println(WebServiceEndpointConfig.getDefaultConfig().getEndpointMaster());
		System.out
				.println("Sesa Valencia Port Service: "
						+ WSAvailabilityChecker
								.isWebServiceAvailable(
										"http://sesa.sti2.at:8080/invoker-dummy-webservice/services/valenciatPortWebService",
										null));
		System.out
				.println("Localhost Dummy Service: "
						+ WSAvailabilityChecker
								.isWebServiceAvailable(
										"http://localhost:9292/at.sti2.ngsee.testwebservices/services/dummy",
										null));
	}
}
