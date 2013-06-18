package at.sti2.msee.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.axis2.AxisFault;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesterRegistration extends TesterBase {
	final String serviceURL = "http://chrismayrdp11.herokuapp.com/wsdl/Indesit_companysite.wsdl.xml";

	final String endpoint = server + "/registration-webservice/services/service";
	final String namespace = "http://msee.sti2.at/delivery/registration/";

	static String registeredServiceURI = null;

	@Test
	public void test1Register() throws AxisFault {
		String operation = "register";

		List<Pair<String, String>> parameters = new ArrayList<TesterBase.Pair<String, String>>();
		parameters.add(new Pair<String, String>("serviceDescriptionURL", serviceURL));
		// exception.expectMessage("Category list is empty");
		String result = invokeSOAP(endpoint, operation, parameters, namespace);
		registeredServiceURI = result;
		assertTrue(result.contains("http://msee.sti2.at/services"));
	}

	@Test
	public void test2Deregister() throws AxisFault {
		String operation = "deregister";

		List<Pair<String, String>> parameters = new ArrayList<TesterBase.Pair<String, String>>();
		parameters.add(new Pair<String, String>("serviceURI", registeredServiceURI));
		// exception.expectMessage("Category list is empty");
		String result = invokeSOAP(endpoint, operation, parameters, namespace);
		assertTrue(result.contains(registeredServiceURI));
	}

	@Test
	public void test3Update() throws AxisFault {
		test1Register(); // register first

		String operation = "update";
		List<Pair<String, String>> parameters = new ArrayList<TesterBase.Pair<String, String>>();
		parameters.add(new Pair<String, String>("serviceURI", registeredServiceURI));
		parameters.add(new Pair<String, String>("serviceURL", serviceURL));
		// exception.expectMessage("Category list is empty");
		String result = invokeSOAP(endpoint, operation, parameters, namespace);
		assertTrue(result.contains("http://msee.sti2.at/services"));

		test2Deregister();
	}
}