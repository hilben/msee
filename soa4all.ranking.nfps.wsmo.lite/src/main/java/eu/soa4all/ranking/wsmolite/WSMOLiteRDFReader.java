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

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omwg.ontology.Attribute;
import org.omwg.ontology.Concept;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Ontology;
import org.wsmo.common.IRI;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.DataFactory;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.util.FileManager;

/**
 * This class is used to parse RDF files that contain instances (e.g. resources/instances.rdf.n3)
 * and create WSML instances that are used afterwards in the rule ranking process
 * @author ioantoma
 *
 */
public class WSMOLiteRDFReader {

	private Model model = null;

	private WsmoFactory wsmoFactory = Factory.createWsmoFactory(new HashMap());
	private DataFactory dataFactory = Factory
			.createDataFactory(new HashMap<String, Object>());
	private LogicalExpressionFactory leFactory = Factory
			.createLogicalExpressionFactory(new HashMap());

	public WSMOLiteRDFReader() {
		super();
		// create an empty model
		model = ModelFactory.createDefaultModel();
	}

	public void readFromFile(String inputFileName) {
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName
					+ " not found");
		}

		RDFReader r = model.getReader("N-TRIPLE");
		r.read(model, in, "");
		// model.write(System.out, "RDF/XML");
	}

	public void readContent(String content) {
		StringReader in = new StringReader(content);
		if (in == null) {
			throw new IllegalArgumentException("Content can't be null");
		}

		RDFReader r = model.getReader("N-TRIPLE");
		r.read(model, in, "");
		// model.write(System.out, "RDF/XML");
	}

	public Ontology getInstances() throws InvalidModelException {
		Ontology result = wsmoFactory.createOntology(wsmoFactory
				.createIRI("http://www.example.com/Gumble"));
		String queryString = "";
		Query query;
		QueryExecution qe;
		ResultSet results;

		// Get the instance and their types
		queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?instance ?type "
				+ "WHERE {"
				+ "      ?instance rdf:type ?type . " + "      }";

		query = QueryFactory.create(queryString);

		qe = QueryExecutionFactory.create(query, model);
		results = qe.execSelect();

		String instance = "", type = "", attribute = "", value = "";
		Map<IRI, IRI> instances = new HashMap<IRI, IRI>();

		// Output query results
		while (results.hasNext()) {
			Binding b = results.nextBinding();
			Iterator<Var> it = b.vars();

			while (it.hasNext()) {
				Var v = (Var) it.next();

				if (v.getName().equals("instance")) {
					instance = b.get(v).toString();
//					System.out.print(instance + " ");
				}

				if (v.getName().equals("type")) {
					type = b.get(v).toString();
//					System.out.print(type + " ");
				}
//				System.out.println();

				if (!instance.isEmpty() && !type.isEmpty()) {
					instances.put(wsmoFactory.createIRI(instance), wsmoFactory
							.createIRI(type));
					instance = "";
					type = "";
				}
			}

		}

//		System.out.println();

		queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?instance ?attribute ?value "
				+ "WHERE {"
				+ "      ?instance ?attribute ?value . " + "      }";

		query = QueryFactory.create(queryString);

		qe = QueryExecutionFactory.create(query, model);
		results = qe.execSelect();

		Concept c = null;
		Attribute a = null;
		Instance i = null;
		Set<Concept> concepts = new HashSet<Concept>();

		// Output query results
		while (results.hasNext()) {
			Binding b = results.nextBinding();
			Iterator<Var> it = b.vars();

			while (it.hasNext()) {
				Var v = (Var) it.next();

				if (v.getName().equals("instance")) {
					instance = b.get(v).toString();
//					System.out.print(instance + " ");
					for (IRI ist : instances.keySet()) {
						if (ist.toString().equalsIgnoreCase(instance)) {
							type = instances.get(ist).toString();
							c = wsmoFactory.createConcept(wsmoFactory
									.createIRI(type));
							if (!concepts.contains(c)) {
								concepts.add(c);
								i = wsmoFactory.createInstance(wsmoFactory
										.createIRI(instance), c);
								result.addInstance(i);
							}
							break;
						}
					}
				}

				if (v.getName().equals("attribute")) {
					attribute = b.get(v).toString();
//					System.out.print(attribute + " ");
					if (!attribute
							.contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
							&& c != null) {

						a = c.createAttribute(wsmoFactory.createIRI(attribute));
					}
				}

				if (v.getName().equals("value")) {
					value = b.get(v).toString();
					if (a != null
							&& !attribute
									.contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
						if (value
								.contains("http://www.w3.org/2001/XMLSchema#float")) {
							value = value
									.substring(
											1,
											value
													.indexOf("http://www.w3.org/2001/XMLSchema#float") - 3);
							i.addAttributeValue(a.getIdentifier(), dataFactory
									.createWsmlDecimal(value));
						} else {
							i.addAttributeValue(a.getIdentifier(), wsmoFactory.createInstance(wsmoFactory.createIRI(value))); 
						}
					}
//					System.out.print(value + " \n");
				}

			}

		}

//		printTE(result);
		return result;
	}

	public static void printTE(TopEntity te) {
		StringBuffer buf = new StringBuffer();
		Factory.createSerializer(null).serialize(new TopEntity[] { te }, buf);
		System.out.println("------------------------------------");
		System.out.println(buf);
		System.out.println("------------------------------------\n\n");
	}

}
