package at.sti2.ngsee.invoker.api.webservice;

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
 * Date of creation:  17.03.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public interface IInvokerEndpoint {
	
	/**
	 * 
	 * @param serviceID The abstract Identifier of the service.
	 * @param operationName The operation that should be called.
	 * @param rdfData The data described in RDF.
	 * @return
	 */
	public String invoke(String serviceID, String operationName, String rdfData) throws Exception;

}
