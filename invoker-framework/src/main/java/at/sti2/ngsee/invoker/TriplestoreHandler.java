package at.sti2.ngsee.invoker;

import javax.xml.namespace.QName;

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
public class TriplestoreHandler {
	
	public static String getServiceURL(String _serviceID) {
		// TODO: Retrieve here the service URL from the Triple Store.
		return _serviceID;
	}
	
	/**
	 * XXX: Only during development used. In productive use the operation qualified name
	 * is taken from an internal data source.
	 * 
	 * @param _operationName The operation name receive from the client. During testing phase it should be a QName.
	 * @return
	 */
	private static String[] _simulateNamespaceGathering(String _operationName) {
		String[] result = new String[2];
		result[0] = "";
		result[1] = "";
		String[] qnameParts = _operationName.split("/");
		for ( int count = 0; count < (qnameParts.length-1); count++ )
			result[0] += qnameParts[count] + "/";
		result[1] = qnameParts[qnameParts.length-1];
		if ( !_operationName.endsWith("/") )
			result[0] = (String) result[0].subSequence(0, result[0].length()-1);
		return result;
	}
	
	public static QName getOperationQName(String _serviceID, String _operationName) {
		// TODO: Try to get the namespace of the _operationName and return the QName
		String[] operationQName = _simulateNamespaceGathering(_operationName);
		return new QName(operationQName[0], operationQName[1]);
	}
}
