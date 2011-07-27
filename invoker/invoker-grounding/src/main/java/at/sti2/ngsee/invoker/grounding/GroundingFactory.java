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
package at.sti2.ngsee.invoker.grounding;

import java.net.URL;

import at.sti2.ngsee.invoker.api.grounding.IGroundingEngine;
import at.sti2.ngsee.invoker.grounding.engine.xslt.XSLTGroundingEngine;

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
