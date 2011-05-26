/**
 * IProxyWebService.java - at.sti2.wsmf.api.ws
 */
package at.sti2.wsmf.api.ws;

/**
 * @author Alex Oberhauser
 */
public interface IProxyWebService {
	
	/**
	 * @param _soapMessage
	 * @param _soapAction
	 * @return
	 */
	public String invoke(String _soapMessage, String _soapAction) throws Exception;
}
