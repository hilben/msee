package at.sti2.ngsee.monitoring.testws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class TestWebServiceConstant extends AbstractTestWebService {

	
	
	@WebMethod
	public String getConstantAnswer(@WebParam(name = "inputString") String string) {
		
		return this.getAnswer(string);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		for (int i = 0; i < 100; i++) {
			System.out.println(new TestWebServiceConstant().getConstantAnswer("ASD"));
		}
	}

}
