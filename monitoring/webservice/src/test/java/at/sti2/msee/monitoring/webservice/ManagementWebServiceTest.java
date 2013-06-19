///**
// * 
// */
//package at.sti2.msee.monitoring.webservice;
//
//import static org.junit.Assert.*;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import at.sti2.wsmf.api.data.qos.QoSParamKey;
//
///**
// * @author Benjamin Hiltpolt
// *
// */
//public class ManagementWebServiceTest {
//
//	private static Logger logger = Logger.getLogger(ManagementWebServiceTest.class);
//	public static final int MAX_INSTANCES = 5;
//	public static final int MAX_ENDPOINTS = 1;
//	private static ManagementWebService managementWS;
//	private static String[] instances;
//	private static List<String> keys;
//	private static String[] endpoints;
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		managementWS = new ManagementWebService();
//		instances = managementWS.listInstanceIDs();
//		endpoints = managementWS.listEndpoints();
//		keys = managementWS.getQoSParamKeys();
//		
//		//Generate some test data	TODO
//	}
//
//	/**
//	 * Test method for {@link at.sti2.msee.monitoring.webservice.ManagementWebService#listInstanceIDs()}.
//	 * @throws Exception 
//	 */
//	@Test
//	public void testListInstanceIDs() throws Exception {
//		//TODO: Generate some test data to be sure it works
////		
////		int instances_c = 0;
////		
////		for (String i : instances) {
////			if (instances_c++ >= MAX_INSTANCES) {
////				break;
////			}
////			logger.info("instance["+instances+ "]: " + i);
////		}
////		
////		if (instances_c > 0) {
////			logger.info("InvokationState: " + managementWS.getInvocationState(instances[0]));
////		} else {
////			fail();
////		}
//	}
//
//
//	/**
//	 * Test method for {@link at.sti2.msee.monitoring.webservice.ManagementWebService#getQoSParam(java.net.URL, at.sti2.wsmf.api.data.qos.QoSParamKey)}.
//	 * @throws Exception 
//	 */
//	@Test
//	public void testGetQoSParam()  {
//		try {
//			logger.info(managementWS.getQoSParamKeys());
//		} catch (Exception e1) {
//			logger.error(e1);
//		}
//		
//		logger.info("Test QoS-Param Keys");
//		int endpoints_c=0;
//		for (String i : endpoints) {
//			if (endpoints_c++ >= MAX_ENDPOINTS) {
//				break;
//			}
//			logger.info("endpoint["+endpoints_c+ "]: " + i);
//			
//			
//			//test 10 keys
//			int c = 0;
//			for (QoSParamKey k : QoSParamKey.values()) {
//				if (c++ >= 7) {
//					break;
//				}
//			try {
//				logger.info("Get QoSParam " + k + " -> " + managementWS.getQoSParamValue(new URL(endpoints[endpoints_c]), k));
//			} catch (MalformedURLException e) {
//				logger.error(e);
//			} catch (Exception e) {
//				logger.error(e);
//			}
//			}
//		}
//		
//	}
//
//	/**
//	 * Test method for {@link at.sti2.msee.monitoring.webservice.ManagementWebService#listEndpoints()}.
//	 * @throws Exception 
//	 */
//	@Test
//	public void testListEndpoints() throws Exception {
//		logger.info(endpoints);
//	}
//
//
//
//	/**
//	 * Test method for {@link at.sti2.msee.monitoring.webservice.ManagementWebService#getSubcategoriesAndServices(java.lang.String)}.
//	 * @throws Exception 
//	 */
//	@Test
//	public void testGetSubcategoriesAndServices() throws Exception {
//		//TODO: just a dummy method
//		logger.info(managementWS.getSubcategoriesAndServices("TestCategory"));
//	}
//
//}
