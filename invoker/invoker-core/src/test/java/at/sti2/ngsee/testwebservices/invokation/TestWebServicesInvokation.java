/**
 * 
 */
package at.sti2.ngsee.testwebservices.invokation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import at.sti2.ngsee.invoker.core.InvokerCore;
import at.sti2.ngsee.testwebservices.soapmessages.TestWebServicesSOAPMessages;
import at.sti2.wsmf.core.InvocationHandler;
import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
import at.sti2.wsmf.core.data.ActivityInstantiatedEvent;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class TestWebServicesInvokation {

	protected static Logger logger = Logger
			.getLogger(TestWebServicesInvokation.class);

	public static final String ENDPOINT = "http://localhost:9292/at.sti2.ngsee.testwebservices/services/";

	public static final String[] TESTSTRING = { "ABCDEF", "Apple", "Orange",
			"Pineapple", "Maximum", "Minimum", "STI-Innsbruck",
			"Sesa Monitoring Framework", "WebService", "123456", "Java",
			"Banana", "EndpointHandler", "Benjamin", "Hello World",
			"This is some longer text for testing purposes", "Semantic Web",
			"lalalaLalala", "www.google.com", "RandomWord", "___A___" };

	@Deprecated
	public static void main(String args[]) throws Exception {

		for (int i = 0; i < 80; i++) {
		
		TestWebServicesInvokation
				.invokeWebServiceViaMonitoring(ENDPOINT + "randomnumber",
						"RandomNumberWebService", TestWebServicesSOAPMessages
								.getRandomNumberWebServiceSOAP((int) (Math
										.random() * 10000)));
		TestWebServicesInvokation.invokeWebServiceViaMonitoring(ENDPOINT
				+ "randomstring", "RandomStringWebService",
				TestWebServicesSOAPMessages
						.getRandomStringWebServiceSOAP(getRandomWord()));
		TestWebServicesInvokation.invokeWebServiceViaMonitoring(ENDPOINT
				+ "reversestring", "ReverseStringWebService",
				TestWebServicesSOAPMessages
						.getReverseStringWebServiceSOAP(getRandomWord()));
		TestWebServicesInvokation.invokeWebServiceViaMonitoring(ENDPOINT
				+ "stringmulti", "StringMultiplierWebService",
				TestWebServicesSOAPMessages
						.getStringMultiplierWebServiceSOAP(getRandomWord()));
		TestWebServicesInvokation.invokeWebServiceViaMonitoring(ENDPOINT
				+ "stringuppercase", "StringUppercaseWebService",
				TestWebServicesSOAPMessages
						.getStringUppercaseWebServiceSOAP(getRandomWord()));
		}
	}

	public static void invokeWebServiceViaMonitoring(String endpointUrl,
			String webServiceName, String SOAPMsg) throws RepositoryException,
			Exception {
		SOAPMessage message = InvokerCore.generateSOAPMessage(SOAPMsg);

		message.saveChanges();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);

		WebServiceEndpointConfig cfg = WebServiceEndpointConfig
				.getConfig(endpointUrl);
		cfg.setWebServiceName(webServiceName);

		logger.info("RESULTS: "
				+ InvocationHandler.invoke(message, null,
						new ActivityInstantiatedEvent(endpointUrl), os.size()));
	}

	public static String getRandomWord() {
		return TESTSTRING[(int) (Math.random() * TESTSTRING.length)];
	}
}
