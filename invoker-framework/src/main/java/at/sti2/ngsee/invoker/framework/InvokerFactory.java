/**
 * 
 */
package at.sti2.ngsee.invoker.framework;

import at.sti2.ngsee.invoker.framework.soap.SoapInvoker;

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
	
	public static void createSoapInvoker(){
		new SoapInvoker();
	}
	
	public static void createRESTInvoker(){
		
	}

}
