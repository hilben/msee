package at.sti2.ngsee.invoker_webservice;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import at.sti2.ngsee.invoker.InvokerFactory;
import at.sti2.ngsee.invoker_api.Invoker;

/**
 * 
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

@WebService
public class InvokerWebService implements Invoker
{
	
	protected static Logger logger = Logger.getLogger(InvokerWebService.class);

	/* (non-Javadoc)
	 * @see at.sti2.ngsee.invoker_api.Invoker#invoke(java.lang.String, java.lang.String)
	 */
	public String invoke(String webServiceID, String data) {
		
		//TODO retrieve wsmoLite from TripleStore using webServiceID
		//TODO extract from NFP the sawsdl URI. retrieve sawsdl from TripleStore
		//TODO 
		
		//TODO 
		
		
		//depending on service type
		InvokerFactory.createWSDLInvoker();
		
		//execute invoker
		
		logger.info("Invoking webservice "+ webServiceID + " with data "+data);
		
		return "result";
		
	}

}
