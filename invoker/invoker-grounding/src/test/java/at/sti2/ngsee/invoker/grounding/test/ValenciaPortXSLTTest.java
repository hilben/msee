package at.sti2.ngsee.invoker.grounding.test;

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

import at.sti2.ngsee.invoker.api.grounding.IGroundingEngine;
import at.sti2.ngsee.invoker.api.grounding.exception.GroundingException;
import at.sti2.ngsee.invoker.grounding.GroundingFactory;

public class ValenciaPortXSLTTest {

	private IGroundingEngine xsltGroundingEnginge;

	@Before
	public void setUp() throws Exception {

		final URL liftingSchemaURL = ClassLoader.getSystemResource("./ValenciaPort/valenciaport-lowering.xslt");
		final URL loweringSchemaURL = ClassLoader.getSystemResource("./ValenciaPort/valenciaport-lifting.xslt");

		xsltGroundingEnginge = GroundingFactory.createGroundingEngine(
				loweringSchemaURL, liftingSchemaURL);
	}

	@Test
	public void testLowering() throws IOException, GroundingException {
		String rdfInputData = loadResource("ValenciaPort/falform1-input.rdf");
		String loweredData = xsltGroundingEnginge.lower(rdfInputData);
		
		assertNotNull(loweredData);
		assertNotSame(loweredData, "");
	}

//	@Test
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
	
//	private URL getURL(String resource) throws IOException{
//		
//	}

}
