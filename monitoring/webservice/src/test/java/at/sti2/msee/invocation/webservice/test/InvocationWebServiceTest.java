//package at.sti2.msee.invocation.webservice.test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.xml.namespace.QName;
//
//import junit.framework.Assert;
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import org.apache.cxf.headers.Header;
//
//import at.sti2.msee.invocation.webservice.InvocationWebService;
//import at.sti2.msee.invocation.webservice.SOAPHeaderThreadLocal;
//
///**
// * Unit test for simple App.
// * 
// * TODO: Write real test for invocation
// */
//public class InvocationWebServiceTest extends TestCase {
//	/**
//	 * Create the test case
//	 * 
//	 * @param testName
//	 *            name of the test case
//	 */
//	public InvocationWebServiceTest(String testName) {
//		super(testName);
//	}
//
//	/**
//	 * @return the suite of tests being tested
//	 */
//	public static Test suite() {
//		return new TestSuite(InvocationWebServiceTest.class);
//	}
//
//	/**
//	 * Rigourous Test :-)
//	 */
//	public void testApp() {
//		assertTrue(true);
//	}
//
//	public void testSOAPHeaderThreadLocal() {
//		Assert.assertNull(SOAPHeaderThreadLocal.get());
//		String header = "<soap:Header> <m:Trans xmlns:m=\"http://www.w3schools.com/transaction/\" soap:mustUnderstand=\"1\">234 </m:Trans></soap:Header>";
//		List<Header> _soapHeaderList = new ArrayList<Header>();
//		_soapHeaderList.add(new Header(new QName("abc"), header));
//		SOAPHeaderThreadLocal.set(_soapHeaderList);
//		Assert.assertEquals(SOAPHeaderThreadLocal.get().size(), 1);
//	}
//	
//	public void testinvocationWebServic(){
//		InvocationWebService ws = new InvocationWebService();
//		Assert.assertTrue(ws.checkAvailability());
//		
//
//		try {
//			ws.invoke("http://abc.at", "doIt", "1");
//		} catch (Exception e) {
//			Assert.assertTrue(e.getMessage().contains( "Content is not allowed in prolog."));
//		}
//	}
//	
//
//}
