/**
 * IEventStreamWebService.java - at.sti2.wsmf.api.ws
 */
package at.sti2.wsmf.api.ws;

import java.net.URL;

/**
 * Event Stream implementation on the base of Publish/Subscribe message exchange pattern.
 * Provides the following functionality:
 * 
 * 	<ul>
 * 		<li>Stage Change Event Stream: Notification to subscriber when the state of an invocation instance changes.</li>
 * 		<li>Availability Event Stream: Notfiication to subscriber when the availability of an web service changes.</li>
 * 		<li>QoS Event Stream: Notification to subscriber when a QoS threshold is crossed (negativ/positiv).</li>
 * 	</ul>
 * 
 * @author Alex Oberhauser
 */
public interface IEventStreamWebService {
	/*
	 * [DONE] State Change Event Stream
	 */
	public void subscribeStateChannel(URL _endpoint, String _namespace, String _methodName, String _soapAction) throws Exception;
	public void unsubscribeStateChannel(URL _endpoint) throws Exception;
	
	/*
	 * [DONE] Availability Event Stream
	 */
	public void subscribeAvailabilityChannel(URL endpoint, String namespace, String methodName, String soapAction) throws Exception;
	public void unsubscribeAvailabilityChannel(URL endpoint) throws Exception;
	
	/*
	 * [DONE] QoS Event Stream
	 */
	public void subscribeQoSChannel(URL endpoint, String namespace, String methodName, String soapAction) throws Exception;
	public void unsubscribeQoSChannel(URL endpoint) throws Exception;
}
