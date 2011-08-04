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
package at.sti2.ngsee.invoker.grounding.engine.service;

import java.net.URL;

import at.sti2.ngsee.invoker.api.grounding.IGroundingEngine;

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
