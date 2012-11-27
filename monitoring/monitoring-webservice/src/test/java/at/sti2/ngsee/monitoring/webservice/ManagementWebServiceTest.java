/**
 * 
 */
package at.sti2.ngsee.monitoring.webservice;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.sti2.wsmf.api.data.qos.QoSParamKey;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class ManagementWebServiceTest {

	private static Logger logger = Logger.getLogger(ManagementWebServiceTest.class);
	public static final int MAX_INSTANCES = 5;
	public static final int MAX_ENDPOINTS = 1;
	private static ManagementWebService managementWS;
	private static String[] instances;
	private static List<String> keys;
	private static String[] endpoints;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		managementWS = new ManagementWebService();
		
		instances = managementWS.listInstanceIDs();
		endpoints = managementWS.listEndpoints();
		keys = managementWS.getQoSParamKeys();
	}

	/**
	 * Test method for {@link at.sti2.ngsee.monitoring.webservice.ManagementWebService#listInstanceIDs()}.
	 * @throws Exception 
	 */
	@Test
	public void testListInstanceIDs() throws Exception {
		
		
		int instances_c = 0;
		
		for (String i : instances) {
			if (instances_c++ >= MAX_INSTANCES) {
				break;
			}
			logger.info("instance["+instances+ "]: " + i);
		}
		
		if (instances_c > 0) {
			logger.info("InvokationState: " + managementWS.getInvocationState(instances[0]));
		} else {
			fail();
		}
	}


	/**
	 * Test method for {@link at.sti2.ngsee.monitoring.webservice.ManagementWebService#getQoSParam(java.net.URL, at.sti2.wsmf.api.data.qos.QoSParamKey)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetQoSParam()  {
		try {
			logger.info(managementWS.getQoSParamKeys());
		} catch (Exception e1) {
			logger.error(e1);
		}
		
		logger.info("Test QoS-Param Keys");
		int endpoints_c=0;
		for (String i : endpoints) {
			if (endpoints_c++ >= MAX_ENDPOINTS) {
				break;
			}
			logger.info("endpoint["+endpoints_c+ "]: " + i);
			
			
			//test 10 keys
			int c = 0;
			for (QoSParamKey k : QoSParamKey.values()) {
				if (c++ >= 7) {
					break;
				}
			try {
				logger.info("Get QoSParam " + k + " -> " + managementWS.getQoSParam(new URL(endpoints[endpoints_c]), k));
			} catch (MalformedURLException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}
			}
		}
		
	}

	/**
	 * Test method for {@link at.sti2.ngsee.monitoring.webservice.ManagementWebService#listEndpoints()}.
	 * @throws Exception 
	 */
	@Test
	public void testListEndpoints() throws Exception {
		logger.info(endpoints);
	}

	/**
	 * Test method for {@link at.sti2.ngsee.monitoring.webservice.ManagementWebService#getQoSRankedEndpoints(at.sti2.wsmf.api.data.qos.QoSParamKey[], java.lang.Float[], java.lang.String[])}.
	 */
	@Test
	public void testGetQoSRankedEndpoints() {
		
		int size = 4;
		QoSParamKey[] rkeys = new QoSParamKey[size];
		Float[] preferenceValues = new Float[size];
		String[] rendpoints = new String[size];
		
		for (int i = 0; i < rkeys.length; i++) {
			rkeys[i] = QoSParamKey.values()[i];
			preferenceValues[i] = new Float(Math.random());
			rendpoints[i] = endpoints[i];
		}
		
		List<String> result = null;
		try {
			result =managementWS.getQoSRankedEndpoints(rkeys, preferenceValues, rendpoints);
		} catch (Exception e) {
			logger.error(e);
		}
		
		assertNotNull(result);
		
		logger.info(result);
	}

	/**
	 * Test method for {@link at.sti2.ngsee.monitoring.webservice.ManagementWebService#getSubcategoriesAndServices(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetSubcategoriesAndServices() throws Exception {
		//TODO: just a dummy method
		logger.info(managementWS.getSubcategoriesAndServices("TestCategory"));
	}

}
