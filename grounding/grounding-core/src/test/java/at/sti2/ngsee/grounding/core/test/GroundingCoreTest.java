/**
 * Copyright (C) 2012 STI Innsbruck, UIBK
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
package at.sti2.ngsee.grounding.core.test;

import org.junit.Test;

import at.sti2.ngsee.grounding.api.IGroundingEngine;
import at.sti2.ngsee.grounding.core.GroundingFactory;

/**
 * @author Alex Oberhauser
 */
public class GroundingCoreTest {
	
	@Test
	public void testTransformation() throws Exception {
		String xmlInput = null;
		IGroundingEngine engine = GroundingFactory.createGroundingEngine(null, null);
		
		
		String rdfInstance = engine.lift(xmlInput);
		String outputData = engine.lower(rdfInstance);
	}
}
