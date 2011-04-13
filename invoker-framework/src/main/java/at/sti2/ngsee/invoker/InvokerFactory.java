/**
 * 
 */
package at.sti2.ngsee.invoker;

import at.sti2.ngsee.invoker.rest.RESTInvoker;
import at.sti2.ngsee.invoker.soap.WSDLInvoker;
import at.sti2.ngsee.invoker_api.framework.ServiceInvoker;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      michaelrogger<br>
 * @version     $Id$<br>
 * Date of creation:  17.03.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public class InvokerFactory {
	
	public static ServiceInvoker createWSDLInvoker(){
		return new WSDLInvoker();
	}
	
	public static ServiceInvoker createRESTInvoker(){
		return new RESTInvoker();
	}

}
