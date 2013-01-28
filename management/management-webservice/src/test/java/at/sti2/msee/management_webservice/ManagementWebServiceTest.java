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
package at.sti2.msee.management_webservice;

import org.junit.Assert;

import at.sti2.msee.management.api.exception.ManagementException;
import at.sti2.msee.management.ontology.OntologyManagement;
import at.sti2.msee.management.webservice.ManagementWebService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ManagementWebServiceTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public ManagementWebServiceTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ManagementWebServiceTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public void testManagementTesting() throws Exception {
		ManagementWebService ws = new ManagementWebService();
		Assert.assertNotNull(ws.managementTesting());
	}
	
	public void testAddOntology() throws ManagementException {
		String ontology = "http://xmlns.com/foaf/spec/index.rdf";
		String addedOntology = OntologyManagement.add(ontology);
		Assert.assertSame(ontology, addedOntology);
	}
	
	public void testUpdateOntology() throws ManagementException {
		String ontology = "http://xmlns.com/foaf/spec/index.rdf";
		String addedOntology = OntologyManagement.add(ontology);
		Assert.assertSame(ontology, addedOntology);
		
		// update
		String updatedOntology = OntologyManagement.update(addedOntology, ontology);
		Assert.assertSame(ontology, updatedOntology);
	}
	
	public void testDeleteOntology() throws ManagementException {
		String ontology = "http://xmlns.com/foaf/spec/index.rdf";
		String addedOntology = OntologyManagement.add(ontology);
		Assert.assertSame(ontology, addedOntology);
		
		// delete
		String deletedOntology = OntologyManagement.delete(addedOntology);
		Assert.assertSame(ontology, deletedOntology);
	}
}
