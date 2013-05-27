/*
 * Copyright (c) 2010, University of Innsbruck, Austria.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package eu.soa4all.ranking.wsmolite;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.model.Syntax;
import org.wsmo.common.exception.InvalidModelException;

import at.sti2.msee.ranking.api.exception.RankingException;
import at.sti2.msee.ranking.repository.RankingRepositoryHandler;
import eu.soa4all.ranking.rules.RulesRanking;
import eu.soa4all.validation.RPC.MSMService;
import eu.soa4all.validation.RPC.Service;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;

/**
 * RulesRanking test class
 * 
 * computes the ranking list of services using the normal rule ranking approach
 * that consider the entire set of NFPs
 * 
 * @author Ioan Toma
 * 
 */

public class RulesRankingTest extends TestCase {

	protected static Logger logger = Logger.getLogger(RulesRankingTest.class);

	private RulesRanking engine = new RulesRanking();

	private String[] wsTestRules = { "/WSMullerFixed_new.rdf.n3",
			"/WSRacerFixed_new.rdf.n3", "/WSRunnerFixed_new.rdf.n3",
			"/WSWalkerFixed_new.rdf.n3", "/WSWeaselFixed_new.rdf.n3" };

	private RankingRepositoryHandler handler;

	@Before
	protected void setUp() throws Exception {

		super.setUp();

		engine = new RulesRanking();

		this.handler = RankingRepositoryHandler.getInstance();

		for (String s : wsTestRules) {
			handler.clearAllContentForWebservice(RulesRankingTest.class.getResource(s));
			handler.setRulesForWebservice(RulesRankingTest.class.getResource(s), readFile(s));
		}
	}
	
	@Test
	public void testRankingWithStoredRules() throws Exception {

		long startTime = System.currentTimeMillis();

		ClassLoader l = this.getClass().getClassLoader();

		// Loading service template
		ServiceTemplate template = new ServiceTemplate();

		logger.info("Loading service template");
		template.readFrom(new FileInputStream(l
				.getResource("stShipping.rdf.n3").getFile()), Syntax.Ntriples);
		engine.setServiceTemplate(template);

		WSMOLiteRDFReader rdfReader = new WSMOLiteRDFReader();
		rdfReader.readFromFile(l.getResource("instances.rdf.n3").getFile());
		try {
			engine.setInstancesOntology(rdfReader.getInstances());
		} catch (InvalidModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Loading services
		logger.info("Loading services");

		for (String s : this.wsTestRules) {
			Service service = new MSMService();
			service.readFrom(this.getInputStreamForWS(RulesRankingTest.class.getResource(s)), Syntax.Ntriples);
			engine.addService(service);
		}
		System.out.println(engine.rank());

		long time = System.currentTimeMillis() - startTime;

		logger.info("...finish ranking in " + time + "    miliseconds");
	}

	@Test
	public void testRanking() throws Exception {

		long startTime = System.currentTimeMillis();

		ClassLoader l = this.getClass().getClassLoader();

		// Loading service template
		ServiceTemplate template = new ServiceTemplate();

		logger.info("Loading service template");
		template.readFrom(new FileInputStream(l
				.getResource("stShipping.rdf.n3").getFile()), Syntax.Ntriples);
		engine.setServiceTemplate(template);

		WSMOLiteRDFReader rdfReader = new WSMOLiteRDFReader();
		rdfReader.readFromFile(l.getResource("instances.rdf.n3").getFile());
		try {
			engine.setInstancesOntology(rdfReader.getInstances());
		} catch (InvalidModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Loading services
		logger.info("Loading services");

		Service racer = new MSMService();
		racer.readFrom(
				new FileInputStream(l.getResource("WSRacerFixed_new.rdf.n3")
						.getFile()), Syntax.Ntriples);
		engine.addService(racer);

		Service runner = new MSMService();
		runner.readFrom(
				new FileInputStream(l.getResource("WSRunnerFixed_new.rdf.n3")
						.getFile()), Syntax.Ntriples);
		engine.addService(runner);

		Service walker = new MSMService();
		walker.readFrom(
				new FileInputStream(l.getResource("WSWalkerFixed_new.rdf.n3")
						.getFile()), Syntax.Ntriples);
		engine.addService(walker);

		Service weasel = new MSMService();
		weasel.readFrom(
				new FileInputStream(l.getResource("WSWeaselFixed_new.rdf.n3")
						.getFile()), Syntax.Ntriples);
		engine.addService(weasel);

		Service muller = new MSMService();
		muller.readFrom(
				new FileInputStream(l.getResource("WSMullerFixed_new.rdf.n3")
						.getFile()), Syntax.Ntriples);
		engine.addService(muller);

		System.out.println(engine.rank());

		long time = System.currentTimeMillis() - startTime;

		logger.info("...finish ranking in " + time + "    miliseconds");
	}

	private InputStream getInputStreamForWS(URL url) throws RankingException, IOException {
		String str = this.handler.getRulesForWebservice(url);
		
		InputStream is = new ByteArrayInputStream(str.getBytes());

		return is;
	}

	

	private String readFile(String path) throws IOException {
		Scanner sc = new Scanner(
				RulesRankingTest.class.getResourceAsStream(path));
		String text = sc.useDelimiter("\\A").next();
		sc.close();
		return text;
	}
}