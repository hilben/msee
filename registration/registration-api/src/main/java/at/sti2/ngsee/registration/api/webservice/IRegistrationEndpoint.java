package at.sti2.ngsee.registration.api.webservice;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Corneliu Stanciu<br>
 * @version     $Id$<br>
 * Date of creation:  17.03.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public interface IRegistrationEndpoint {

	/**
	 * 
	 * @param _wsdlURL The URL of the Web Service description (WSDL).
	 * @return true if the service was register successfully, otherwise false.  
	 */
	public boolean register(String _wsdlURL);
	
}
