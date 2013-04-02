package at.sti2.msee.discovery.webservice.test;

import java.net.MalformedURLException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

/**
 * This test class represents a SOAP client that tests the different methods of
 * the discovery webservice.
 * 
 */
public class DiscoveryWebserviceTestAxis {

	String endpoint = "http://msee.sti2.at/discovery-webservice/service/discovery?wsdl";

	// String endpoint =
	// "http://localhost:8080/at.sti2.msee.delivery.discovery.webservice-m17.1-SNAPSHOT/service?wsdl";

	@SuppressWarnings("unused")
	@Test
	public void discoverTest() throws ServiceException, MalformedURLException {
		Service service = new Service();
		Call call = null;

		// test no parameter
		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://sesa.sti2.at/services/",
					"discover"));
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			String returnValue = (String) call
					.invoke(new Object[] { "http://www.sti2.at/MSEE/ServiceCategories#BUSINESS" });
		} catch (Exception e) {
			Assert.assertEquals(
					"No parameters specified to the Call object!  You must call addParameter() for all parameters if you have called setReturnType().",
					e.toString());
		}
		
		// test empty list (NULL)
		// test parameter no uri
		String empty = null;
		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://sesa.sti2.at/services/",
					"discover"));
			call.addParameter("categoryList",
					org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			String returnValue = (String) call
					.invoke(new Object[] { empty });
		} catch (Exception e) {
			//e.printStackTrace();
			Assert.assertEquals(
					"org.openrdf.repository.http.HTTPQueryEvaluationException: Not a valid (absolute) URI:"
							, e.toString());
		}

		// test parameter no uri
		String wrongParameter = "abcde";
		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://sesa.sti2.at/services/",
					"discover"));
			call.addParameter("categoryList",
					org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			String returnValue = (String) call
					.invoke(new Object[] { wrongParameter });
		} catch (Exception e) {
			Assert.assertEquals(
					"org.openrdf.repository.http.HTTPQueryEvaluationException: Not a valid (absolute) URI: "
							+ wrongParameter, e.toString());
		}

		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://sesa.sti2.at/services/",
					"discover"));
			call.addParameter("categoryList",
					org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			String returnValue = (String) call
					.invoke(new Object[] { "http://www.sti2.at/MSEE/ServiceCategories#BUSINESS" });
			String minimalReturnString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:sawsdl=\"http://www.w3.org/ns/sawsdl#\" xmlns:msm_ext=\"http://sesa.sti2.at/ns/minimal-service-model-ext#\" xmlns:wsdl=\"http://www.w3.org/ns/wsdl-rdf#\"> </rdf:RDF>";
			Assert.assertTrue(returnValue.length() >= minimalReturnString
					.length());
		} catch (Exception e) {
			e.toString();
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void lookupTest() throws ServiceException, MalformedURLException {
		Service service = new Service();
		Call call = null;

		// test no parameters
		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://sesa.sti2.at/services/",
					"lookup"));
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			String returnValue = (String) call
					.invoke(new Object[] { "http://www.sti2.at/MSEE/ServiceCategories#BUSINESS" });
		} catch (Exception e) {
			Assert.assertEquals(
					"No parameters specified to the Call object!  You must call addParameter() for all parameters if you have called setReturnType().",
					e.toString());
		}

		// test valid service
		try {
			call = (org.apache.axis.client.Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://sesa.sti2.at/services/",
					"lookup"));
			call.addParameter("namespace",
					org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter("operationName",
					org.apache.axis.Constants.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			String returnValue = (String) call.invoke(new Object[] {
					"http://greath.example.com/2004/wsdl/resSvc",
					"opCheckAvailability" });
			// System.out.println(returnValue);
		} catch (Exception e) {
			Assert.assertEquals(
					"No parameters specified to the Call object!  You must call addParameter() for all parameters if you have called setReturnType().",
					e.toString());
		}
	}
}