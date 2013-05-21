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

import java.io.File;
import java.io.FileInputStream;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.wsmo.common.exception.InvalidModelException;

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

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		engine = new RulesRanking();
	}

	public void testRanking() throws Exception {

		long startTime = System.currentTimeMillis();

		ClassLoader l = this.getClass().getClassLoader();
		
		// Loading service template
		ServiceTemplate template = new ServiceTemplate();
		
		logger.info("Loading service template");
		template.readFrom(new FileInputStream(l.getResource("stShipping.rdf.n3").getFile()), Syntax.Ntriples);
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
		racer.readFrom(new FileInputStream(l.getResource("WSRacerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
		engine.addService(racer);
		
		Service runner = new MSMService();
		runner.readFrom(new FileInputStream(l.getResource("WSRunnerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
		engine.addService(runner);
		
		Service walker = new MSMService();
		walker.readFrom(new FileInputStream(l.getResource("WSWalkerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
		engine.addService(walker);
		
		Service weasel = new MSMService();
		weasel.readFrom(new FileInputStream(l.getResource("WSWeaselFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
		engine.addService(weasel);
		
		Service muller = new MSMService();
		muller.readFrom(new FileInputStream(l.getResource("WSMullerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
		engine.addService(muller);
		
		System.out.println(engine.rank());
		
		long time = System.currentTimeMillis() - startTime;
		
		logger.info("...finish ranking in " + time + "    miliseconds");
		System.out.println("...finish ranking in " + time + "    miliseconds");
	}

}