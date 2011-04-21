/**
 * 
 */
package at.sti2.ngsee.invoker_api.framework;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>    Copyright (c) 2011 STI<br>
 * <b>Company:</b>      STI Innsbruck<br>
 *
 * @author      		michaelrogger<br>
 * @author 				Alex Oberhauser<br>
 * @version     		$Id$<br>
 * Date of creation:  	17.03.2011<br>
 * File:         		$Source$<br>
 * Modifier:     		$Author$<br>
 * Revision:     		$Revision$<br>
 * State:        		$State$<br>
 */
public interface ISOAPInvoker {
	
	public enum BINDING_TYPE {
		Automatic,
		SOAP11,
		SOAP12
		/* ... */
	}
	
	/**
	 * 
	 * @param serviceURL The Endpoint of the 
	 * @param bindingType If unsure choose Automatic.
	 * @param operationQName The operation that should be called.
	 * @param inputData The input data, understandable by the services.
	 * @return
	 */
	public String invoke(String serviceBindingURL, BINDING_TYPE bindingType, String operationQName, String inputData);
}
