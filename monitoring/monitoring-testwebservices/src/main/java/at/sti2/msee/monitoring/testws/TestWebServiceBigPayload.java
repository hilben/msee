package at.sti2.msee.monitoring.testws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class TestWebServiceBigPayload extends AbstractTestWebService {

	@WebMethod
	public String getBigAnswer(@WebParam(name = "inputString") String string) {
        minPayloadSize = 1000;
        maxPayloadSize = 10000;
		
		
		return this.getAnswer(string);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(new TestWebServiceBigPayload().getBigAnswer("ASD"));
		}
	}

}
