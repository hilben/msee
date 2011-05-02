package at.sti2.ngsee.invoker_grounding;

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

import at.sti2.ngsee.invoker_api.grounding.IGroundingEngine;
import at.sti2.ngsee.invoker_api.grounding.exception.GroundingException;

public class XSLTGroundingEngineTest {

	private IGroundingEngine xsltGroundingEnginge;

	@Before
	public void setUp() throws Exception {

		final URL liftingSchemaURL = new URL(
				"http://demo.sti2.at/see/grounding/xslt/weather-lifting.xslt");
		final URL loweringSchemaURL = new URL(
				"http://demo.sti2.at/see/grounding/xslt/weather-lowering.xslt");

		xsltGroundingEnginge = GroundingFactory.createGroundingEngine(
				loweringSchemaURL, liftingSchemaURL);
	}

	@Test
	public void testLowering() throws IOException, GroundingException {
		String rdfInputData = loadResource("weather-input.rdf.xml");
		String loweredData = xsltGroundingEnginge.lower(rdfInputData);
		
		assertNotNull(loweredData);
		assertNotSame(loweredData, "");
	}

	@Test
	public void testLifting() throws IOException, GroundingException {
		String xmlInputData = loadResource("WeatherResponse.xml");
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
