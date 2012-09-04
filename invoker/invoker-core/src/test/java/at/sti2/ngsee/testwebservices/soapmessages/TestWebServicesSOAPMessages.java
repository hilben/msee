/**
 * 
 */
package at.sti2.ngsee.testwebservices.soapmessages;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class TestWebServicesSOAPMessages {

	public static final String getRandomNumberWebServiceSOAP(int number) {
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://sesa.sti2.at/services/\"> <soapenv:Header/><soapenv:Body> <ser:getRandomNumberAndCalculationTime><number>"
				+ number
				+ "</number></ser:getRandomNumberAndCalculationTime></soapenv:Body></soapenv:Envelope>";
	}

	public static final String getRandomStringWebServiceSOAP(String string) {
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://sesa.sti2.at/services/\"><soapenv:Header/><soapenv:Body><ser:getRandomStringAdded><string>"
				+ string
				+ "</string></ser:getRandomStringAdded></soapenv:Body></soapenv:Envelope>";
	}

	public static final String getReverseStringWebServiceSOAP(String string) {
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://sesa.sti2.at/services/\"><soapenv:Header/> <soapenv:Body><ser:getReversedString><string>"
				+ string
				+ "</string></ser:getReversedString></soapenv:Body></soapenv:Envelope>";
	}

	public static final String getStringMultiplierWebServiceSOAP(String string) {
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://sesa.sti2.at/services/\"><soapenv:Header/><soapenv:Body><ser:getStringMultiplied><string>"
				+ string
				+ "</string></ser:getStringMultiplied> </soapenv:Body></soapenv:Envelope>";
	}

	public static final String getStringUppercaseWebServiceSOAP(String string) {
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://sesa.sti2.at/services/\"><soapenv:Header/> <soapenv:Body><ser:getUppercaseString><string>"
				+ string
				+ "</string></ser:getUppercaseString></soapenv:Body></soapenv:Envelope>";
	}
}
