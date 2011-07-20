/**
 * 
 */
package at.sti2.ngsee.invoker.core;

import at.sti2.ngsee.invoker.api.core.ISOAPInvoker;
import at.sti2.ngsee.invoker.core.rest.RESTInvoker;
import at.sti2.ngsee.invoker.core.soap.SoapJaxWSInvoker;

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
public abstract class InvokerFactory {
	
	public static ISOAPInvoker createSOAPInvoker() {
		return new SoapJaxWSInvoker();
	}
	
	public static RESTInvoker createRESTInvoker() {
		return new RESTInvoker();
	}
	
}
