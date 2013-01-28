/**
 * 
 */
package at.sti2.msee.testwebservices.soapmessages;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class TestWebServicesSOAPMessages {

	public static final String getSoap(String action, String str) {
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://testws.monitoring.ngsee.sti2.at/\"> <soapenv:Header/><soapenv:Body> <ser:"+action+"><inputString>"
				+ str
				+ "</inputString></ser:"+action+"></soapenv:Body></soapenv:Envelope>";
	}

}
