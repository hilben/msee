package at.sti2.msee.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.axis2.AxisFault;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesterDiscovery extends TesterBase {
	final String endpoint = server + "/discovery-webservice/services/service";
	String namespace = "http://msee.sti2.at/delivery/discovery/";

	@Test
	public void testDiscover1NoCategories() throws AxisFault {

		String operation = "discover";
		List<Pair<String, String>> parameters = null;
		exception.expectMessage("Category list is empty");
		String result = invokeSOAP(endpoint, operation, parameters, namespace);
	}

	@Test
	public void testDiscover2OneCategory() throws AxisFault {
		String operation = "discover";
		String category = "http://msee.sti2.at/categories#BUSINESS";
		List<Pair<String, String>> parameters = new ArrayList<TesterBase.Pair<String, String>>();
		parameters.add(new Pair<String, String>("categoryList", category));
		String result = invokeSOAP(endpoint, operation, parameters, namespace);
		assertTrue(result, result.contains(category));
	}

	@Test
	public void testDiscover3TwoCategory() throws AxisFault {
		String operation = "discover";
		String category1 = "http://msee.sti2.at/categories#BUSINESS";
		String category2 = "http://msee.sti2.at/categories#WEB_APP";

		List<Pair<String, String>> parameters = new ArrayList<TesterBase.Pair<String, String>>();
		parameters.add(new Pair<String, String>("categoryList", category1));
		parameters.add(new Pair<String, String>("categoryList", category2));
		String result = invokeSOAP(endpoint, operation, parameters, namespace);
		assertTrue(result, result.contains(category1));
		assertTrue(result, result.contains(category2));
	}
}
