/**
 * Main.java - org.example
 */
package org.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import at.sti2.wsmf.webservice.EventStreamWebService;
import at.sti2.wsmf.webservice.EventStreamWebServiceService;
import at.sti2.wsmf.webservice.ManagementWebService;
import at.sti2.wsmf.webservice.ManagementWebServiceService;
import at.sti2.wsmf.webservice.ProxyWebService;
import at.sti2.wsmf.webservice.ProxyWebServiceService;

/**
 * @author Alex Oberhauser
 *
 */
public class Main {
	
	private static String generateSOAPMessage(QName _body) throws SOAPException, IOException {
		MessageFactory msgFactory = MessageFactory.newInstance();
		SOAPMessage message = msgFactory.createMessage();
		SOAPBody body = message.getSOAPBody();
		body.addBodyElement(_body);
		
		message.saveChanges();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(System.out);
		System.out.println();
		message.writeTo(os);
		return os.toString();
	}
	
	public static void testProxy() throws SOAPException, IOException {
		ProxyWebServiceService proxyFactory = new ProxyWebServiceService();
		ProxyWebService proxyWS = proxyFactory.getProxyWebServicePort();
		/**
		 * Successful Request
		 */
		String soapResponse1 = proxyWS.invoke(generateSOAPMessage(new QName("http://ws.sigimera.networld.to/", "getDrones")), null);
		System.out.println(soapResponse1);
		
		String soapResponse3 = proxyWS.invoke(generateSOAPMessage(new QName("http://ws.sigimera.networld.to/", "getDrones")), null);
		System.out.println(soapResponse3);
		
		/**
		 * Failed Request
		 */
		StringBuffer getDronesRequest2 = new StringBuffer();
		getDronesRequest2.append("<ws:notExistentOperation xmlns:ws=\"http://ws.sigimera.networld.to/\"/>");
		try {
			proxyWS.invoke(generateSOAPMessage(new QName("http://ws.sigimera.networld.to/", "noMethodFound")), null);
		} catch (Exception e) {
			System.out.println("[**] This operation call was intended to fail..");
		}
	}
	
	public static void testManagement() {
		ManagementWebServiceService managementFactory = new ManagementWebServiceService();
		ManagementWebService managementWS = managementFactory.getManagementWebServicePort();
		
//		System.out.println(managementWS.listEndpoints());
		managementWS.addEndpoint("http://sigimera.networld.to:8080/services/sigimeraID");
		managementWS.addEndpoint("http://localhost:8080/services/sigimeraID");
		System.out.println(managementWS.listEndpoints());
		
		List<String> instances = managementWS.listInstanceIDs();
		System.out.println(managementWS.listInstanceIDs());
		
		for ( String entry : instances )
			System.out.println("'" + entry + "' has status '" + managementWS.getInvocationState(entry) + "'");
		
		String instance2 = "http://sti2.at/wsmf/instances#0bf6bd3f-b542-46ad-a839-65f113eae734";
		System.out.println("'" + instance2 + "' has status '" + managementWS.getInvocationState(instance2) + "'");
	}
	
	public static void testEvent() {
		EventStreamWebServiceService eventFactory = new EventStreamWebServiceService();
		EventStreamWebService eventWS = eventFactory.getEventStreamWebServicePort();
		
		eventWS.subscribeAvailabilityChannel("http://localhost:9999/wsmf-channellistener-example/services/events", "http://webservice.wsmf.sti2.at/", "getAvailabilityChangedEvent", null);
		eventWS.subscribeQoSChannel("http://localhost:9999/wsmf-channellistener-example/services/events", "http://webservice.wsmf.sti2.at/", "getQoSChangedEvent", null);
		eventWS.subscribeStateChannel("http://localhost:9999/wsmf-channellistener-example/services/events", "http://webservice.wsmf.sti2.at/", "getStateChangedEvent", null);
	}
	
	public static void main(String[] args) throws SOAPException, IOException {
		Main.testEvent();
		Main.testManagement();
		Main.testProxy();
	}
}
