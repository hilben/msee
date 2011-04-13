package at.sti2.ngsee.invoker;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Alex Oberhauser<br>
 * @version     $Id$<br>
 * Date of creation:  13.04.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public class ServiceHandler {
	
	public static String getServiceURL(String _serviceID) {
		// TODO: Retrieve here the service URL from the Triple Store.
		return null;
	}
	
	public static String getOperationQName(String _serviceID, String _operationName) {
		// TODO: Try to get the namespace of the _operationName and return the QName
		return _operationName;
		
	}
}
