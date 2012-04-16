/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.ngsee.grounding.webservice;

import java.net.URL;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;
import org.apache.log4j.Logger;

import at.sti2.ngsee.grounding.api.IAvailabilityCheck;
import at.sti2.ngsee.grounding.api.IGroundingEndpoint;
import at.sti2.ngsee.grounding.api.IGroundingEngine;
import at.sti2.ngsee.grounding.api.data.DebugResponse;
import at.sti2.ngsee.grounding.core.GroundingFactory;

/**
 * 
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>    Copyright (c) 2011 STI<br>
 * <b>Company:</b>      STI Innsbruck<br>
 *
 * @author      		michaelrogger<br>
 * @author				Alex Oberhauser<br>
 * @version     		$Id$<br>
 * Date of creation:  	17.03.2011<br>
 * File:         		$Source$<br>
 * Modifier:     		$Author$<br>
 * Revision:     		$Revision$<br>
 * State:        		$State$<br>
 */

@WebService(targetNamespace="http://sesa.sti2.at/services/")
@WSDLDocumentationCollection(
		@WSDLDocumentation("SESA Grounding Component")
	)
public class GroundingWebService implements IGroundingEndpoint, IAvailabilityCheck {
	protected static Logger logger = Logger.getLogger(GroundingWebService.class);

	/**
	 * @see at.sti2.ngsee.grounding.api.webservice.IAvailabilityCheck#checkAvailability()
	 */
	@WebMethod
	@Override
	public boolean checkAvailability() {
		return true;
	}

	/**
	 * @see at.sti2.ngsee.grounding.api.IGroundingEndpoint#transform(java.lang.String, java.lang.String, java.lang.String)
	 */
	@WebMethod
	@Override
	public String transform(@WebParam(name="inputMessage")String _inputMessage,
			@WebParam(name="xsltToOntologyURL")String _xsltToOntology,
			@WebParam(name="xsltToOutputURL")String _xsltToOutput) throws Exception {
		IGroundingEngine engine = GroundingFactory.createGroundingEngine(new URL(_xsltToOutput), new URL(_xsltToOntology));
		
		String rdfInstance = engine.lift(_inputMessage);
		return engine.lower(rdfInstance);
	}
	
	/**
	 * @see at.sti2.ngsee.grounding.api.IGroundingEndpoint#transform(java.lang.String, java.lang.String, java.lang.String)
	 */
	@WebMethod
	@Override
	public DebugResponse transform_debug(@WebParam(name="inputMessage")String _inputMessage,
			@WebParam(name="xsltToOntologyURL")String _xsltToOntology,
			@WebParam(name="xsltToOutputURL")String _xsltToOutput) throws Exception {
		DebugResponse response = new DebugResponse(_inputMessage, _xsltToOntology, _xsltToOutput);
		
		long startProcessTime = System.nanoTime();
		IGroundingEngine engine = GroundingFactory.createGroundingEngine(new URL(_xsltToOutput), new URL(_xsltToOntology));
		
		long liftProcessTime = System.nanoTime();
		String rdfInstance = engine.lift(_inputMessage);
		response.setInput2OntologyExecutionTime((System.nanoTime() - liftProcessTime)/1000000.0);
		
		long lowerProcessTime = System.nanoTime();
		String outputInstance = engine.lower(rdfInstance);
		response.setOntology2OutputExecutionTime((System.nanoTime() - lowerProcessTime)/1000000.0);
		
		response.setIntermediateMessage(rdfInstance);
		response.setOutputMessage(outputInstance);
		
		response.setTotalExecutionTime((System.nanoTime() - startProcessTime)/1000000.0);
		return response;
	}

}
