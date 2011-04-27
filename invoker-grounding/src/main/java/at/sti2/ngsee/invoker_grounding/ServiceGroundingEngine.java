package at.sti2.ngsee.invoker_grounding;

import java.net.URL;

import at.sti2.ngsee.invoker_api.grounding.IGroundingEngine;

/**
 * <b>Purpose:</b> Grounding engine based on external transformation services<br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Michael Rogger, Alex Oberhauser, Corneliu Valentin Stanciu<br>
 * @version     $Id$<br>
 * Date of creation:  13.04.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */

public class ServiceGroundingEngine implements IGroundingEngine {
	
	private URL liftingSchemaURL;
	private URL loweringSchemaURL;
	
	public ServiceGroundingEngine(URL _liftingSchemaURL, URL _loweringSchemaURL) {
		this.liftingSchemaURL = _liftingSchemaURL;
		this.loweringSchemaURL = _loweringSchemaURL;
	}

	public String lower(String rdfInputData) {
		// TODO Auto-generated method stub
		return null;
	}

	public String lift(String xmlInputData) {
		// TODO Auto-generated method stub
		return null;
	}

}
