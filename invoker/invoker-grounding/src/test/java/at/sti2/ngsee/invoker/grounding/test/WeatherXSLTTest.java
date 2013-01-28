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
package at.sti2.msee.invoker.grounding.test;

/**
 * <b>Purpose:</b> Test XSLT grounding engine<br> 
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import at.sti2.msee.invoker.api.grounding.IGroundingEngine;
import at.sti2.msee.invoker.api.grounding.exception.GroundingException;
import at.sti2.msee.invoker.grounding.GroundingFactory;

public class WeatherXSLTTest {

	private IGroundingEngine xsltGroundingEnginge;

	@Before
	public void setUp() throws Exception {

		final URL liftingSchemaURL = new URL(
				"http://sesa.sti2.at/services/xslt/weather-lifting.xslt");
		final URL loweringSchemaURL = new URL(
				"http://sesa.sti2.at/services/xslt/weather-lowering.xslt");

		xsltGroundingEnginge = GroundingFactory.createGroundingEngine(
				loweringSchemaURL, liftingSchemaURL);
	}

	@Test
	public void testLowering() throws IOException, GroundingException {
		String rdfInputData = loadResource("weather/weather-input.rdf.xml");
		String loweredData = xsltGroundingEnginge.lower(rdfInputData);
		
		assertNotNull(loweredData);
		assertNotSame(loweredData, "");
	}

	@Test
	public void testLifting() throws IOException, GroundingException {
		String xmlInputData = loadResource("weather/WeatherResponse.xml");
		String liftedData = xsltGroundingEnginge.lift(xmlInputData);
		
		assertNotNull(liftedData);
		assertNotSame(liftedData, "");
	}

	private String loadResource(String resource) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream(resource)));

		StringBuilder result = new StringBuilder();
		String line = "";
		while ((line = in.readLine()) != null) {
			result.append(line+"\n");
		}
		
		in.close();
		return result.toString();
	}

}
