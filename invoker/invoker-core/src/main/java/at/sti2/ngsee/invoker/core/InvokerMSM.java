/**
 * InvokerMSM.java - at.sti2.ngsee.invoker
 */
package at.sti2.ngsee.invoker.core;

import java.net.URL;

import javax.xml.namespace.QName;

import at.sti2.ngsee.invoker.api.core.IInvokerMSM;

/**
 * @author Alex Oberhauser
 */
public class InvokerMSM implements IInvokerMSM {
	private URL loweringSchema;
	private URL liftingSchema;
	private URL wsdl;
	private String soapAction;
	private QName operationQName;
	private QName serviceName;
	private QName portQName;
	private URL endpointURL;
	
	public void setLoweringSchema(URL _loweringSchema) {
		this.loweringSchema = _loweringSchema;
	}
	
	public void setLiftingSchema(URL _liftingSchema) {
		this.liftingSchema = _liftingSchema;
	}
	
	public void setWSDL(URL _wsdl) {
		this.wsdl = _wsdl;
	}
	
	public void setSOAPAction(String _soapAction) {
		this.soapAction = _soapAction;
	}
	
	public void setOperationQName(QName _operationQName) {
		this.operationQName = _operationQName;
	}
	
	/**
	 * @see at.sti2.ngsee.invoker.api.core.IInvokerMSM#getLoweringSchema()
	 */
	@Override
	public URL getLoweringSchema() { return this.loweringSchema; }

	/**
	 * @see at.sti2.ngsee.invoker.api.core.IInvokerMSM#getLifingSchema()
	 */
	@Override
	public URL getLifingSchema() { return this.liftingSchema; }

	/**
	 * @see at.sti2.ngsee.invoker.api.core.IInvokerMSM#getWSDL()
	 */
	@Override
	public URL getWSDL() { return this.wsdl; }
	
	/**
	 * @see at.sti2.ngsee.invoker.api.core.IInvokerMSM#getSOAPAction()
	 */
	@Override
	public String getSOAPAction() { return this.soapAction; }
	
	/**
	 * @see at.sti2.ngsee.invoker.api.core.IInvokerMSM#getOperationQName()
	 */
	@Override
	public QName getOperationQName() { return this.operationQName; }

	/**
	 * @param stringValue
	 */
	public void setServiceQName(QName _serviceName) {
		this.serviceName = _serviceName;		
	}
	
	@Override
	public QName getServiceQName() { return this.serviceName; }

	
	public void setPortQName(QName _portQName) {
		this.portQName = _portQName;
	}
	
	/**
	 * @see at.sti2.ngsee.invoker.api.core.IInvokerMSM#getPortQName()
	 */
	@Override
	public QName getPortQName() { return this.portQName; }

	public void setEndpointURL(URL _endpointURL) {
		this.endpointURL = _endpointURL;
	}
	
	/**
	 * @see at.sti2.ngsee.invoker.api.core.IInvokerMSM#getEndpointURL()
	 */
	@Override
	public URL getEndpointURL() { return this.endpointURL; }

}