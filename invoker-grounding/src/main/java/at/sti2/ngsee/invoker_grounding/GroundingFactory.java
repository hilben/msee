package at.sti2.ngsee.invoker_grounding;

import java.net.URL;

import at.sti2.ngsee.invoker_api.grounding.IGroundingEngine;

/**
 * <b>Purpose:</b> This factory creates grouding engines.<br>
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
public abstract class GroundingFactory {

	/**
	 * Creates a grouding engine depending on the specified type. Currently only the {@link XSLTGroundingEngine} is supported.
	 * @param loweringSchemaURL {@link URL} that points to the lowering schema
	 * @param liftingSchemaURL {@link URL} that points to the lifting schema
	 * @return grounding engine
	 */
	public static IGroundingEngine createGroundingEngine(URL loweringSchemaURL,
			URL liftingSchemaURL) {
		/**
		 * TODO: Check here the grounding type, e.g. XSLT or Service and return
		 * the right instance.
		 */
		return new XSLTGroundingEngine(loweringSchemaURL, liftingSchemaURL);
	}
}
