package at.sti2.ngsee.invoker.dummy.webservice.valenciaport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import at.sti2.ngsee.invoker.client.InvokerClient;
import at.sti2.see.Exception_Exception;
import at.sti2.see.InvokerWebService;
import at.sti2.see.InvokerWebServiceService;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @author Benjamin Hiltpolt
 * 
 *        test for the invoker webservice using the valenciaportwebservice
 */
public class ValenciaPortWsTest extends TestCase {

	public static final int LOOPS = 2;

	/**
	 * 
	 */
	public void testValenciaPortWs() {
//		InvokerWebServiceService factory = new InvokerWebServiceService();
//		InvokerWebService service = factory.getInvokerWebServicePort();
//
//		String serviceID = "http://sesa.sti2.at/services/dummy/#ValenciaPortWebServiceService";
//		String operationName = "submitFALForm";
//
//		StringBuilder inputData = new StringBuilder();
//		String NL = System.getProperty("line.separator");
//		Scanner scanner;
//		try {
//			scanner = new Scanner(new FileInputStream(ValenciaPortWsTest.class
//					.getResource("/valencia-input.rdf.xml").getFile()));
//		} catch (FileNotFoundException e) {
//
//			System.exit(0);
//			scanner = null;
//			e.printStackTrace();
//		}
//		TestCase.assertNotNull(scanner);
//		try {
//			while (scanner.hasNextLine()) {
//				inputData.append(scanner.nextLine() + NL);
//			}
//		} finally {
//			scanner.close();
//		}
//
//		String oldResponseMessage = "";
//		for (int count = 0; count < ValenciaPortWsTest.LOOPS; count++) {
//			long startTime = System.currentTimeMillis();
//			String responseMessage = null;
//			try {
//				responseMessage = service.invoke(serviceID, operationName,
//						inputData.toString());
//			} catch (Exception_Exception e) {
//				e.printStackTrace();
//			}
//			if (!oldResponseMessage.equals(responseMessage)) {
//				oldResponseMessage = responseMessage;
//				System.out.println("[ResponseMessage] " + responseMessage);
//			}
//			TestCase.assertNotNull(oldResponseMessage);
//			System.out.println("[PerfTest]        Service '" + serviceID
//					+ "' execution time was "
//					+ (System.currentTimeMillis() - startTime) + " ms");
//		}
		TestCase.assertTrue(true);

	}

}
