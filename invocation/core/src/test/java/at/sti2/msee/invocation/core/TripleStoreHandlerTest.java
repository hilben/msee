package at.sti2.msee.invocation.core;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

public class TripleStoreHandlerTest extends TestCase{

	
	/**
	 *  TODO: usefull test with logging and usefull service
	 * @throws IOException 
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 * @throws QueryEvaluationException 
	 */
	@Test
	public void testTripleStoreHandler() throws QueryEvaluationException, RepositoryException, MalformedQueryException, IOException {
			InvocationMSM invocationMSM = TriplestoreHandler.getInvocationMSM(
					"http://www.webserviceX.NET#GlobalWeather", "GetWeather");
			System.out.println(invocationMSM.getOperationQName());
			System.out.println(invocationMSM);

			invocationMSM = TriplestoreHandler.getInvocationMSM(
					"http://www.nanonull.com/TimeService/TimeService.asmx",
					"getUTCTime");
			System.out.println(invocationMSM.getOperationQName());
			System.out.println(invocationMSM);

			invocationMSM = TriplestoreHandler
					.getInvocationMSM(
							"http://sesa.sti2.at/services/dummy/#ValenciaPortWebServiceService",
							"submitFALForm");
			System.out.println(invocationMSM.getOperationQName());
			System.out.println(invocationMSM);
	}
}
