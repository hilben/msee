package at.sti2.ngsee.registration.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import at.sti2.ngsee.registration.api.IRegistrationEndpoint;

/**
 * 
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>    Copyright (c) 2011 STI<br>
 * <b>Company:</b>      STI Innsbruck<br>
 *
 * @author      		Corneliu Stanciu<br>
 * @version     		$Id$<br>
 * Date of creation:  	23.04.2011<br>
 * File:         		$Source$<br>
 * Modifier:     		$Author$<br>
 * Revision:     		$Revision$<br>
 * State:        		$State$<br>
 */

@WebService(targetNamespace="http://see.sti2.at/")
public class RegistrationWebService implements IRegistrationEndpoint
{	
	protected static Logger logger = Logger.getLogger(RegistrationWebService.class);
	
	@WebMethod
	public boolean register(@WebParam(name="wsdlURL")String _wsdlURL) {
		logger.info("Registration of Web Service '" + _wsdlURL + "'");
		return true;
	}
	
	public String getVersion() {
		logger.info("Registeration getVersion()");
		return "v9999";
	}
}
