package at.sti2.ngsee.invoker_api.framework;

import java.net.URL;

import javax.xml.namespace.QName;

/**
 * @author Alex Oberhauser
 */
public interface IInvokerMSM {
	
	/**
	 * @return A link to the lowering schema.
	 */
	public URL getLoweringSchema();
	
	/**
	 * @return A link to the lifting schema.
	 */
	public URL getLifingSchema();
	
	/**
	 * @return Link to the Web Service Description Language File.
	 */
	public URL getWSDL();
	
	/**
	 * @return SOAPAction specified in the HTTP header
	 */
	public String getSOAPAction();
	
	/**
	 * @return Namespace + Operation as QName instance.
	 */
	public QName getOperationQName();

}
