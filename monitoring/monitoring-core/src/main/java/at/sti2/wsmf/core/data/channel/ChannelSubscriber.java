/**
 * wsmf-core - at.sti2.wsmf.core.data.channel
 */
package at.sti2.wsmf.core.data.channel;

/**
 * @author Alex Oberhauser
 */
public class ChannelSubscriber {
	private final String endpoint;
	private final String namespace;
	private final String operationName;
	private final String soapAction;
	
	public ChannelSubscriber(String _endpoint, String _namespace, String _operationName, String _soapAction) {
		this.endpoint = _endpoint;
		this.namespace = _namespace;
		this.operationName = _operationName;
		this.soapAction = _soapAction;
	}
	
	public String getEndpoint() { return this.endpoint; }
	public String getNamespace() { return this.namespace; }
	public String getOperationName() { return this.operationName; }
	public String getSoapAction() { return this.soapAction; }
}
