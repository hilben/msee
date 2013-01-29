/**
 * 
 */
package at.sti2.msee.testwebservices.invokation;

import java.io.ByteArrayOutputStream;

import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.testwebservices.soapmessages.TestWebServicesSOAPMessages;
import at.sti2.wsmf.core.MonitoringInvocationHandler;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;

/**
 * @author Benjamin Hiltpolt
 * TODO: create some application which runs it self; create useful tests;
 */
public class TestWebServicesInvokation {

	protected static Logger logger = Logger
			.getLogger(TestWebServicesInvokation.class);

	// public static final String ENDPOINT =
	// "http://localhost:9292/at.sti2.msee.testwebservices/services/";
	public static final String ENDPOINT = "http://sesa.sti2.at:8080/monitoring-testwebservices/services/";

	public static final String[] TESTSTRING = { "ABCDEF", "Apple", "Orange",
			"Pineapple", "Maximum", "Minimum", "STI-Innsbruck",
			"Sesa Monitoring Framework", "WebService", "123456", "Java",
			"Banana", "EndpointHandler", "Benjamin", "Hello World",
			"This is some longer text for testing purposes", "Semantic Web",
			"lalalaLalala", "www.google.com", "RandomWord", "___A___" };

	@Deprecated
	public static void main(String args[]) throws Exception {

//		for (int i = 0; i < 999; i++) {
			try {

				TestWebServicesInvokation.invokeWebServiceViaMonitoring(
						ENDPOINT + "slow/getSlowAnswer",
						"", TestWebServicesSOAPMessages.getSoap("getSlowAnswer",getRandomWord(10)));
				TestWebServicesInvokation.invokeWebServiceViaMonitoring(
						ENDPOINT + "big/getBigAnswer",
						"", TestWebServicesSOAPMessages.getSoap("getBigAnswer",getRandomWord(100)));
				TestWebServicesInvokation.invokeWebServiceViaMonitoring(
						ENDPOINT + "constant/getConstantAnswer",
						"", TestWebServicesSOAPMessages.getSoap("getConstantAnswer",getRandomWord(10)));
				TestWebServicesInvokation.invokeWebServiceViaMonitoring(
						ENDPOINT + "random/getRandomAnswer",
						"", TestWebServicesSOAPMessages.getSoap("getRandomAnswer",getRandomWord(10)));
		          
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
				System.exit(1);
			}
//		}
	}

	public static void invokeWebServiceViaMonitoring(String endpointUrl,
			String webServiceName, String SOAPMsg) throws RepositoryException,
			Exception {
		SOAPMessage message = invocationCore.generateSOAPMessage(SOAPMsg);

		message.saveChanges();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);


		logger.info("RESULTS: "
				+ MonitoringInvocationHandler.invokeWithMonitoring(message,
						null, new ActivityInstantiatedEvent(endpointUrl),
						os.size()));
	}

	public static String getRandomWord(int val) {
		
		String returns = "";
		for (int i = 0;  i < Math.random()*val;i++) {
			returns = TESTSTRING[(int) (Math.random() * TESTSTRING.length)];
		}
		
		
		return returns;
	}
}
