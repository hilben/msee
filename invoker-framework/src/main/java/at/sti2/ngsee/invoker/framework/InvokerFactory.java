/**
 * 
 */
package at.sti2.ngsee.invoker.framework;

import at.sti2.ngsee.invoker.framework.soap.SOAPInvoker;
import at.sti2.ngsee.invoker_api.framework.ISOAPInvoker;

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
		return new SOAPInvoker();
	}
	
}
