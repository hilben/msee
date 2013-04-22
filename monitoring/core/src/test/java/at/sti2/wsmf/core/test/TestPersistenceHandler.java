/**
 * 
 */
package at.sti2.wsmf.core.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;
import at.sti2.wsmf.core.PersistentHandler;
import at.sti2.wsmf.core.data.WebServiceEndpoint;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Benjamin Hiltpolt
 * 
 *         TODO: implement
 */
public class TestPersistenceHandler extends TestCase {
	private static PersistentHandler phandler;
	private static Logger logger = Logger
			.getLogger(TestPersistenceHandler.class);
	private static final String testendpointstr = "http://www.sometestingurl.com";
	private static URL testendpoint = null;

	@Before
	public void setUp() {
		try {
			phandler = PersistentHandler.getInstance();
			testendpoint = new URL(testendpointstr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testInvocation() throws Exception {

		// Create a new endpoint
		WebServiceEndpoint ws = new WebServiceEndpoint(testendpoint);

		List<String> endpoints = phandler.getEndpoints();
		logger.info("Got endpoints " + endpoints);

		boolean match = false;
		for (String endpoint : endpoints) {
			if (endpoint.compareTo(testendpointstr) == 0) {
				match = true;
			}
		}

		TestCase.assertTrue(match);

		for (int i = 0; i <= 5; i++) {
			logger.info("Do a simulated invocation...");
			// Simulate Successful invocation
			ws.addSuccessfullInvoke(i * 20, i * 20, i * 20);
		}

		double v1 = phandler.getQoSParamValue(testendpoint,
				QoSParamKey.PayloadSizeRequest);
		double v2 = phandler.getQoSParamValue(testendpoint,
				QoSParamKey.PayloadSizeResponse);
		double v3 = phandler.getQoSParamValue(testendpoint,
				QoSParamKey.ResponseTime);

		logger.info("values are " + v1 + " " + v2 + " " + v3);

		TestCase.assertTrue(v1 == 100);
		TestCase.assertTrue(v2 == 100);
		TestCase.assertTrue(v3 == 100);

		for (QoSParamKey k : QoSParamKey.values()) {
			logger.info("Current value: " + k.name() + " : "
					+ phandler.getQoSParamValue(testendpoint, k));
			
			logger.info("Timeframe: " + phandler.getQoSTimeframe(testendpoint,
					k, null, null));
		}

		// for (int i = 0;i < 100; i++) {
		// double val = Math.random()*1000;
		// logger.info("Update ResponseTimeAverage: " + val);
		// phandler.updateQoSValueAverage(testendpoint, new
		// QoSParamValue(QoSParamKey.ResponseTimeAverage, val,
		// QoSUnit.Milliseconds));
		// logger.info("ResponseTimeAverage is now : " +
		// phandler.getQoSParamValue(testendpoint,
		// QoSParamKey.ResponseTimeAverage));
		// }


		phandler.deleteEndpoint(testendpoint);

		// match = false;
		// for (String endpoint : endpoints) {
		// if (endpoint.compareTo(testendpointstr)==0) {
		// match = true;
		// }
		// }
		//
		// TestCase.assertFalse(match);
		// logger.info("Endpoint successful deleted");
	}

	public void testPersistenceHandler() throws FileNotFoundException,
			IOException {

	}
}
