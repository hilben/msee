package at.sti2.msee.monitoring.testws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class TestWebServiceSlow extends AbstractTestWebService {

	
	
	@WebMethod
	public String getSlowAnswer(@WebParam(name = "inputString") String string) {
	minResponseTime = 300;
	maxResponseTime = 1000;
		
		return this.getAnswer(string);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		for (int i = 0; i < 100; i++) {
			System.out.println(new TestWebServiceSlow().getSlowAnswer("ASD"));
		}
	}

}
