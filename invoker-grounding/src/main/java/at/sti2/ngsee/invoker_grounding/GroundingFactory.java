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
public abstract class GroundingFactory {
	public static IGroundingEngine createGroundingEngine(URL liftingSchemaURL, URL loweringSchemaURL){
		/**
		 * TODO: Check here the grounding type, e.g. XSLT or Service and 
		 * 	  	 return the right instance.
		 */
		return new XSLTGroundingEngine(liftingSchemaURL, loweringSchemaURL);
	}
}
