package at.sti2.ngsee.invoker_grounding;

import java.net.URL;

import at.sti2.ngsee.invoker_api.grounding.IGroundingEngine;

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
public class XSLTGroundingEngine implements IGroundingEngine {
	private URL liftingSchemaURL;
	private URL loweringSchemaURL;
	
	public XSLTGroundingEngine(URL _liftingSchemaURL, URL _loweringSchemaURL) {
		this.liftingSchemaURL = _liftingSchemaURL;
		this.loweringSchemaURL = _loweringSchemaURL;
	}

	@Override
	public String lowering(String rdfInputData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String lifting(String xmlInputData) {
		// TODO Auto-generated method stub
		return null;
	}

}
