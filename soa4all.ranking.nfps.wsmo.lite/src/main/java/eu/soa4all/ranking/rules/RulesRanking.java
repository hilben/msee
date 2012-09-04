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

package eu.soa4all.ranking.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Axiom;
import org.omwg.ontology.Instance;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Variable;
import org.ontoware.rdf2go.model.node.URI;
import org.wsml.reasoner.api.LPReasoner;
import org.wsml.reasoner.api.WSMLReasonerFactory;
import org.wsml.reasoner.impl.DefaultWSMLReasonerFactory;
import org.wsmo.common.IRI;
import org.wsmo.common.Namespace;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.service.ServiceDescription;
import org.wsmo.wsml.ParserException;

import com.ibm.icu.util.StringTokenizer;

import eu.soa4all.ranking.RankingException;
import eu.soa4all.ranking.descriptions.RankingPreference;
import eu.soa4all.ranking.framework.RankingEngine;
import eu.soa4all.ranking.util.Constants;
import eu.soa4all.ranking.util.DefaultLocator;
import eu.soa4all.validation.RPC.Service;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;
import eu.soa4all.validation.WSMOLite.Annotation;
import eu.soa4all.validation.WSMOLite.RuleBasedRankingNonFunctionalParameter;

/**
 * Ranking engine for Web services based on nfps values; it ranks the services
 * based on the nfps specified by the user. The nfp values are computed by
 * querying a WSML reasoner given: (1) users preferences i.e. (i) the nfp (e.g.
 * obligations or discounts) and the order direction (descending or ascending)
 * (2) the nfp axiom definition from the service (3) instances from the goal It
 * returns a list of services ordered according to the specified preferences
 * 
 * @author Ioan Toma
 * 
 */

public class RulesRanking extends RankingEngine {

	protected static Logger logger = Logger.getLogger(RulesRanking.class);

	private WsmoFactory wsmoFactory;

	private LogicalExpressionFactory leFactory;

	private LPReasoner flightReasoner;

	// the set of services to be ranked
	private Set<Service> services;

	// the service template
	private ServiceTemplate template;

	// the instances ontology
	private Ontology instancesOntology;

	public Ontology getInstancesOntology() {
		return instancesOntology;
	}

	public void setInstancesOntology(Ontology instancesOntology) {
		this.instancesOntology = instancesOntology;
	}

	public RulesRanking() {
		wsmoFactory = Factory.createWsmoFactory(new HashMap());
		leFactory = Factory.createLogicalExpressionFactory(new HashMap());

		Factory.getLocatorManager().addLocator(new DefaultLocator());

		this.services = new HashSet<Service>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(WSMLReasonerFactory.PARAM_BUILT_IN_REASONER,
				WSMLReasonerFactory.BuiltInReasoner.IRIS_WELL_FOUNDED);
		flightReasoner = DefaultWSMLReasonerFactory.getFactory()
				.createFlightReasoner(params);

	}

	public String rank() {
		if (services == null)
			return "No web services to rank";

		List<Service> rez = new ArrayList<Service>();
		try {
			rez = this.rank(services, template);
		} catch (UnsupportedOperationException e) {
			logger.warn("Operation not supported.", e);
			return "Operation not supported: " + e.getMessage();
		}

		String result = "";
		if (rez.isEmpty()) {
			result = "No web service to rank";
		} else {
			result = "\nThe ranked set of Web services is:\n";
			for (Service s : rez)
				result += s.resource + "\n";
		}
		return result;
	}

	public String[] rankServices() {
		ArrayList<String> servicesIRIs = new ArrayList<String>();

		List<Service> rez = new ArrayList<Service>();
		try {
			rez = this.rank(services, template);
		} catch (UnsupportedOperationException e) {
			logger.warn("Operation not supported.", e);
		}

		// String result = "";
		// if (rez.isEmpty()) {
		// result = "No web service to rank";
		// } else {
		// result = "\nThe ranked set of Web services is:\n";
		for (Service s : rez)
			servicesIRIs.add(s.resource.toString());
		// }

		String[] rez1 = new String[servicesIRIs.size()];
		int i = 0;
		for (String s : servicesIRIs) {
			rez1[i] = s;
			i++;
		}

		return rez1;
	}

	public List<Service> rank(Set<Service> webServices, ServiceTemplate template) {
		SortedMap<Float, Set<Service>> result = new TreeMap<Float, Set<Service>>();
		List<Service> resultList = new ArrayList<Service>();

		Map<Integer, SortedMap<Float, Set<Service>>> all = new TreeMap<Integer, SortedMap<Float, Set<Service>>>();

		// map containing all services and the associated ontologies
		// where an ontology associated with a service contains all axioms
		// from ontologies in the service + all instances
		Map<Service, Ontology> wsOntos = new HashMap<Service, Ontology>();

		List<IRI> irisNFPs = new ArrayList<IRI>();
		Ontology ontoTemp = null;
		IRI iriNFPFunction = null;

		PreferencesExtractor pf = new PreferencesExtractor(template);
		try {
			pf.process();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		RankingPreference rankingPreference = new RankingPreference(
				pf.getNfps(), pf.getOrderingValue());

		Ontology onto = this.instancesOntology;

		Map<IRI, NFPInfo> m = rankingPreference.getNfpAttributes();

		irisNFPs.addAll(m.keySet());

		// for each service
		for (Service webservice : webServices) {
			try {
				ontoTemp = this.getServiceNFPDescr(webservice);
				// printTE(ontoTemp);
				wsOntos.put(webservice, ontoTemp);
			} catch (RankingException e) {
				e.printStackTrace();
			}
		}

		// for each NFP requested in the user preferences
		// and each service compute a value by evaluating
		// the rules based on the input data and user
		// preferences
		for (IRI iriNfp : m.keySet()) {
			SortedMap<Float, Set<Service>> map = new TreeMap<Float, Set<Service>>();
			// System.out.println("!!!!!!!!!!!DEBUG:"+iriNfp.getLocalName());
			map = new TreeMap<Float, Set<Service>>();

			// for each service
			for (Service webservice : webServices) {

				Ontology o_new, o_test;

				try {
					ontoTemp = wsOntos.get(webservice);

					// printTE(ontoTemp);
					Iterator<Annotation> it = webservice
							.getNonFunctionalParameters();
					while (it.hasNext()) {
						RuleBasedRankingNonFunctionalParameter a = (RuleBasedRankingNonFunctionalParameter) it
								.next();
						if (iriNfp.getLocalName().equalsIgnoreCase(
								wsmoFactory.createIRI(a.resource.toString())
										.getLocalName())) {

							// System.err.println("Create IRI for " +
							// webservice.SERVICE);
							// System.err.println("Create IRI for " +
							// webservice.getNonFunctionalParameters());
							// System.err.println("Create IRI for " +
							// a.getPredicate().toString());

							iriNFPFunction = wsmoFactory.createIRI(a
									.getPredicate().toString());
							break;
						}
					}

					o_test = onto;
					// printTE(o_test);
					// System.out.print("aaaaaaaaaaaaaaaaaaaaaaa");
					// printTE(ontoTemp);
					// System.out.print("bbbbbbbbbbbbbbbbbbbbbbb");
					o_new = mergeOntologies(ontoTemp, o_test);
					// printTE(o_new);

					for (Instance inst : o_test.listInstances()) {

						// create the query
						LogicalExpression le;
						le = (LogicalExpression) leFactory
								.createLogicalExpression("_\""
										+ iriNFPFunction
										// + "\"(_\""+
										// rankingPreference.getIriMainInstance().toString()+"\", ?v).",
										+ "\"(_\"" + inst.getIdentifier()
										+ "\", ?v).", o_new);

						// evaluate the NFP value corresponding to the service
						Float f = this.evaluateNFPValue(o_new, le);
						if (f != null) {
							if (!f.isNaN()) {
								logger.debug("NFP " + iriNfp.getLocalName()
										+ " value = " + f.toString() + " for "
										+ webservice.resource);
								System.out.println("\nNFP "
										+ iriNfp.getLocalName() + " value = "
										+ f.toString() + " for "
										+ webservice.resource + "\n");

								Set<Service> hs = new HashSet<Service>();
								if (map.containsKey(f)) {
									hs = (HashSet<Service>) map.get(f);
								}
								hs.add(webservice);
								map.put(f, hs);

							}
						}

					}
				} catch (ParserException e) {
					e.printStackTrace();
				}
			}
			all.put(new Integer(irisNFPs.indexOf(iriNfp)), map);
		}

		// normalize the values - for each NFP, the corresponding values for
		// each service is first divided by the highest value of that NFP
		// for one of the services and multiply afterwards with the importance
		// value
		for (IRI nfpKey : irisNFPs) {
			SortedMap<Float, Set<Service>> mapScores = new TreeMap<Float, Set<Service>>();
			SortedMap<Float, Set<Service>> mapNfp = all.get(irisNFPs
					.indexOf(nfpKey));
			for (Float value : mapNfp.keySet()) {
				float newValue = 0;
				if (((Float) mapNfp.lastKey()).floatValue() != 0)
					newValue = value / ((Float) mapNfp.lastKey()).floatValue();

				Set<Service> ws = mapNfp.get(value);
				if (m.get(nfpKey).getOrderingValue()
						.equals(new Integer(Constants.ASCENDING)))
					newValue = 1 - newValue;

				for (Service w : ws) {
					System.out.println("NFP " + nfpKey.getLocalName()
							+ " normalized value = " + newValue + " for "
							+ w.resource);
					System.out.print("\n\n");
				}

				newValue = m.get(nfpKey).getImportance().floatValue()
						* newValue;

				for (Service w : ws) {
					System.out.println("NFP " + nfpKey.getLocalName()
							+ " normalized value = " + newValue + " for "
							+ w.resource);
					System.out.print("\n\n");
				}

				mapScores.put(new Float(newValue), ws);
			}

			all.remove(irisNFPs.indexOf(nfpKey));
			all.put(irisNFPs.indexOf(nfpKey), mapScores);
		}

		// This is used to store for each service the nfps values scores
		// Instances of the concept IndividualNFPWebServiceScore from the
		// result ontology are created.
		Map<Service, Set<Instance>> individualNFPWebServiceScores = new HashMap<Service, Set<Instance>>();

		// compute the cumulative score for each service by
		// summing up the normalized NFPs values for each service
		for (Service webservice : webServices) {
			Float score = new Float(0);
			for (IRI nfpKey : irisNFPs) {
				SortedMap<Float, Set<Service>> mapNfp = all.get(irisNFPs
						.indexOf(nfpKey));
				for (Float value : mapNfp.keySet()) {
					Set<Service> ws = mapNfp.get(value);
					if (ws.contains(webservice)) {
						score += value;
					}
				}
			}
			System.out.println("Service = " + webservice.resource
					+ "; Score = " + score);
			Set<Service> hs = new HashSet<Service>();
			if (result.containsKey(score)) {
				hs = (HashSet<Service>) result.get(score);
			}
			hs.add(webservice);
			result.put(score, hs);
		}

		// build the list of ranked web services
		// based on the cumlative score computed above
		for (Float score : result.keySet()) {
			Set<Service> ws = result.get(score);
			for (Service w : ws) {
				resultList.add(w);
			}
		}

		if (rankingPreference.getOrder() == Constants.DESCENDING)
			Collections.reverse(resultList);

		return resultList;
	}

	/*
	 * Builds an ontology correspondent to the requested Web service
	 */

	private Ontology getServiceNFPDescr(Service serviceDescr)
			throws RankingException {
		// fixing unique identifiers
		String adr = "http://sws-ranking/ontology-"
		// + Math.abs(Helper.getRandomLong());
				+ Math.abs(Math.random());
		IRI iri = wsmoFactory.createIRI(adr);
		Ontology descr = wsmoFactory.createOntology(iri);

		descr.setWsmlVariant(org.wsmo.common.WSML.WSML_RULE);
		Namespace ns = wsmoFactory.createNamespace("", iri);
		descr.setDefaultNamespace(ns);

		Iterator<Annotation> it = serviceDescr.getNonFunctionalParameters();
		while (it.hasNext()) {
			RuleBasedRankingNonFunctionalParameter annotation = (RuleBasedRankingNonFunctionalParameter) it
					.next();
			Axiom axiom = wsmoFactory.createAxiom(wsmoFactory
					.createIRI("http://sws-ranking/axiom"
							+ Math.abs(Math.random())));
			try {
				axiom.addDefinition(leFactory
						.createLogicalExpression(annotation
								.getLogicalExpression()));
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				descr.addAxiom(axiom);
			} catch (SynchronisationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// printTE(descr);
		/*
		 * Set<Ontology> ontologies = serviceDescr.listOntologies();
		 * 
		 * // add namespaces for (Namespace namespace :
		 * serviceDescr.listNamespaces()) { descr.addNamespace(namespace); }
		 * 
		 * try { for (Ontology ontology : ontologies) {
		 * descr.addOntology(ontology);
		 * 
		 * Set<Axiom> ax = ontology.listAxioms();
		 * 
		 * for (Axiom a : ax) { descr.addAxiom(a); }
		 * 
		 * for (Instance i : ontology.listInstances()) { descr.addInstance(i); }
		 * }
		 * 
		 * } catch (InvalidModelException e) {
		 * logger.error("Invalid model found on service " +
		 * serviceDescr.resource); e.printStackTrace(); }
		 */

		return descr;
	}

	/*
	 * Gets the main functions defining the NFPs
	 */
	private IRI getServiceNFPMainFunction(ServiceDescription serviceDescr,
			IRI nfpID) {
		IRI result = null;
		if (serviceDescr.listNFPValues().containsKey(nfpID))
			result = (IRI) serviceDescr.listNFPValues(nfpID).iterator().next();
		return result;
	}

	private Ontology mergeOntologies(Ontology ontology1, Ontology ontology2) {
		// fixed support for unique identifiers

		IRI iri = wsmoFactory.createIRI("http://sws-ranking/tempOntology-"
		// + Math.abs(Helper.getRandomLong()) + ".wsml");
				+ Math.abs(Math.random()) + ".wsml");
		Ontology temp = wsmoFactory.createOntology(iri);

		temp.setWsmlVariant(org.wsmo.common.WSML.WSML_RULE);
		Namespace ns = wsmoFactory.createNamespace("", iri);
		temp.setDefaultNamespace(ns);

		temp.addOntology(ontology1);
		temp.addOntology(ontology2);

		return temp;
	}

	private Float evaluateNFPValue(Ontology ontology, LogicalExpression query) {
		Float result = null;
		try {
			flightReasoner.registerOntology(ontology);
			// logger.debug("Query is: \n" + query);
			// System.out.println("Query is: \n" + query);
			// printTE(ontology);

			// Set<Map<Variable, Term>> queryResult =
			// flightReasoner.executeQuery(
			// (IRI) ontology.getIdentifier(), query);
			Set<Map<Variable, Term>> queryResult = flightReasoner
					.executeQuery(query);

			Iterator it = queryResult.iterator();

			// we compute the mean of all variables buindings
			// for one variable
			float value = 0;
			int count = 0;
			while (it.hasNext()) {
				Map<Variable, Term> m = (Map<Variable, Term>) it.next();
				Set<Variable> v = m.keySet();
				Iterator itv = v.iterator();
				while (itv.hasNext()) {
					Variable var = (Variable) itv.next();
					Term t = m.get(var);
					if (t != null) {
						// System.out.println(t.toString());
						Float curr_value = new Float(t.toString());
						value += curr_value.floatValue();
						++count;
					}
				}
			}

			result = new Float(value / count);

			// flightReasoner.deRegisterOntology((IRI)
			// ontology.getIdentifier());
		} catch (Exception e) {
			logger.error("Error while querying the ontology", e);
			e.printStackTrace();
		}
		return result;
	}

	public static void printTE(TopEntity te) {
		StringBuffer buf = new StringBuffer();
		Factory.createSerializer(null).serialize(new TopEntity[] { te }, buf);
		System.out.println("------------------------------------");
		System.out.println(buf);
		System.out.println("------------------------------------\n\n");
	}

	public void addService(Service service) throws RankingException {
		services.add(service);
	}

	public void setServiceTemplate(ServiceTemplate template) {
		this.template = template;
	}

	public void removeService(Service service) {
		if (services.contains(service))
			services.remove(service);
	}

	public void removeAllServices() {
		services.removeAll(services);
	}

	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}

	public ServiceTemplate getServiceTemplate() {
		return template;
	}

	/**
	 * @deprecated
	 * @param args
	 */
	public static void main(String args[]) {
		RulesRanking ranking = new RulesRanking();

	}

}
