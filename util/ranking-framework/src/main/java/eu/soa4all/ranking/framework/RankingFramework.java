/*
 * Copyright (c) 2009, University of Innsbruck, Austria.
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

package eu.soa4all.ranking.framework;

import ie.deri.wsmx.core.configuration.annotation.Exposed;
import ie.deri.wsmx.core.configuration.annotation.Stop;
import ie.deri.wsmx.core.configuration.annotation.WSMXComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.omwg.ontology.Ontology;
import org.wsmo.common.IRI;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.execution.commons.exception.ComponentException;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.mediator.WGMediator;
import org.wsmo.service.Goal;
import org.wsmo.service.WebService;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;

import eu.soa4all.ranking.Ranking;
import eu.soa4all.ranking.RankingEngineInterface;
import eu.soa4all.ranking.RankingException;
import eu.soa4all.ranking.util.Constants;
import eu.soa4all.ranking.util.DefaultLocator;
import eu.soa4all.validation.RPC.Service;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;


/**
 * A ranking framework supporting ranking based on keywords and nfps expressed
 * as logical expressions.
 * 
 * @author Ioan Toma
 */

@WSMXComponent(name = "Ranking", events = "WEBSERVICERANKING", description = "A ranking engine based on multiple nfps with logical expressions evaluation")
public class RankingFramework implements Ranking {

	protected static Logger logger = Logger.getLogger(RankingFramework.class);

	protected Parser parser;

	protected WsmoFactory wsmoFactory;

	protected int[] types = { Constants.VALUEQA };

	protected Set<Service> registeredWebServices;

	protected List<RankingEngineInterface> rankingEngines;

	@Stop()
	public void stop() {
	}

	public RankingFramework() {
		wsmoFactory = Factory.createWsmoFactory(new HashMap());
		LogicalExpressionFactory leFactory = Factory
				.createLogicalExpressionFactory(new HashMap());

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(Factory.WSMO_FACTORY, wsmoFactory);
		props.put(Factory.LE_FACTORY, leFactory);
		parser = Factory.createParser(props);

		registeredWebServices = new HashSet<Service>();

		Factory.getLocatorManager().addLocator(new DefaultLocator());

		rankingEngines = new ArrayList<RankingEngineInterface>();
		
		for (int type : types)
			rankingEngines.add(RankingFactory.createRankingEngine(type));

	}

	protected String getRankingOutput(ServiceTemplate template) {
		List<Service> services;
		String result = "";
		try {
			if (registeredWebServices != null) {
				services = this.rank(registeredWebServices, template);

				if (services.isEmpty()) {
					result = "No web services to rank";
				} else {
					result = "\nThe ranked set of Web services is:\n";					
					for (Service s : services)
						result += s.resource + "\n";
				}
			}
		} catch (UnsupportedOperationException e) {
			logger.warn("Operation not supported.", e);
			return "Operation not supported: " + e.getMessage();
		}

		return result;
	}

//	@Exposed(description = "Human-interface-friendly wrapper around the operation "
//			+ "of adding a Service Description to the ranking engine from a given WSML document. "
//			+ "Returns a human-readable confirmation or an error description in case of failure. ")
//	public String addServiceDescriptionByContent(String serviceDoc) {
//		WebService service = null;
//		try {
//			service = this
//					.getServiceDescriptionByContent(serviceDoc);
//		} catch (ComponentException e) {
//			logger.warn("Failed to parse Service description.", e);
//			return "Failed to parse Service description: " + e.getMessage();
//		}
//		if (service == null)
//			return "Failed to locate a Web service";
//
//		return addServiceDescription(service);
//	}

	public String addServiceDescription(Service service) {

		if (!registeredWebServices.contains(service))
			registeredWebServices.add(service);

		String message = "";
		for (RankingEngineInterface engine : rankingEngines)
			message += "\n" + addService(service, engine)
					+ "\n";

		return message;
	}

	public String addServicesDescriptions(
			Set<Service> serviceDescriptions) {
		String message = "";
		for (Service sd : serviceDescriptions)
			message += "\n" + addServiceDescription(sd) + "\n";

		return message;
	}

	/*
	 * Adds a service to a ranking engine. Returns a corresponding
	 * success/failure message
	 */
	protected String addService(Service service,
			RankingEngineInterface engine) {
		String message;

		try {
			engine.addService(service);
			message = "Successfully added service "
					+ service.resource + " to "
					+ engine.getClass().getSimpleName() + " engine";
		} catch (RankingException e) {
			String failed = "Failed to add service description "
					+ service.resource + " to "
					+ engine.getClass().getSimpleName() + " engine";
			logger.warn(failed, e);
			message = failed + ":\n " + e.getMessage();
		}
		return message;
	}

	@Exposed(description = "Human-interface-friendly wrapper around the operation "
			+ "of listing Service Description ids register with ranking engine. Returns "
			+ "a human-readable confirmation or an error description in case of failure. ")
	public String listServices() {
		String result = "";
		for (Service ws : registeredWebServices) {
			result += ws.resource + " \n";
		}
		return result;
	}

//	@Exposed(description = "Human-interface-friendly wrapper around the operation "
//			+ "of removing a Service Description from the ranking engine. Returns "
//			+ "a human-readable confirmation or an error description in case of failure. ")
//	public String removeWebServiceByContent(String serviceDoc) {
//		Service service = null;
//		try {
//			service = this
//					.getServiceDescriptionByContent(serviceDoc);
//		} catch (ComponentException e) {
//			logger.warn("Failed to parse Web service.", e);
//			return "Failed to parse Web service: " + e.getMessage();
//		}
//		if (service == null)
//			return "Failed to locate a Web service";
//
//		return removeServiceDescription(service);
//	}

	public String removeServiceDescription(Service service) {
		if (registeredWebServices.contains(service))
			registeredWebServices.remove(service);

		String message = "";
		for (RankingEngineInterface engine : rankingEngines)
			message += "\n"
					+ removeServiceDescription(service, engine)
					+ "\n";

		return message;
	}

	public String removeServicesDescriptions(
			List<Service> services) {
		String message = "";
		for (Service service : services)
			message += "\n" + removeServiceDescription(service)
					+ "\n";
		return message;
	}

	/*
	 * Removes a Service description from a ranking engine. Returns a
	 * corresponding success/failure message
	 */
	protected String removeServiceDescription(
			Service service, RankingEngineInterface engine) {
		String message;

		try {
			engine.removeService(service);
			message = "Successfully removed service description "
					+ service.resource + " to " + "from " +

					engine.getClass().getSimpleName() + " engine";
		} catch (IllegalArgumentException e) {
			String failed = "Failed to Service description from "
					+ engine.getClass().getSimpleName() + " engine";
			logger.warn(failed, e);
			message = failed + ":\n " + e.getMessage();
		}
		return message;
	}

	/*
	 * Returns a service description (web service + associated ontologies
	 * describing its nfps) from the given WSMLDocument
	 */
	protected WebService getServiceDescriptionByContent(String serviceDoc)
			throws ComponentException {
		TopEntity[] topEntities = parse(serviceDoc);
		Set<Ontology> ontos = new HashSet<Ontology>();
		Set<WebService> webservices = new HashSet<WebService>();

		for (TopEntity entity : topEntities) {
			if (entity instanceof Ontology) {
				ontos.add((Ontology) entity);
			}
		}
		
		for (TopEntity entity : topEntities) {		
			if (entity instanceof WebService) {
				webservices.add((WebService) entity);
				break;
			}
		}

		if (webservices.size() != 0)
			return (WebService) webservices.toArray()[0];
		else
			return null;
	}

	/*
	 * Returns a goal description (goal + associated ontologies) from the given
	 * WSMLDocument
	 */
	protected Goal getGoalByContent(String goalDoc)
			throws ComponentException {
		TopEntity[] topEntities = parse(goalDoc);
		Set<Ontology> ontos = new HashSet<Ontology>();
		Goal goal = null;
		
		for (TopEntity entity : topEntities) {
			if (entity instanceof Ontology) {
				ontos.add((Ontology) entity);
			}
		}
		for (TopEntity entity : topEntities) {
			if (entity instanceof Goal) {
				goal = (Goal) entity;
				break;
			}
		}

		return goal;
	}

	protected TopEntity[] parse(String wsmlDocument) throws ComponentException {
		TopEntity[] parsed = null;
		try {
			StringBuffer buffer = new StringBuffer(wsmlDocument + " ");
			parsed = parser.parse(buffer);
		} catch (ParserException e) {
			throw new ComponentException("Parsing failed:" + e.getMessage(), e);
		} catch (InvalidModelException e) {
			throw new ComponentException("Parsing failed:" + e.getMessage(), e);
		}

		return parsed;
	}
	

	public List<Service> rank(Set<Service> webServices, ServiceTemplate template) {
		List<Service> ranked = new ArrayList<Service>();

		ListIterator engineIt = rankingEngines.listIterator();
		while (engineIt.hasNext()) {
			Ranking engine = (Ranking) engineIt.next();
			logger.debug("Testing ranking on "
					+ engine.getClass().getSimpleName());
			System.out.println("Testing ranking on "
					+ engine.getClass().getSimpleName());
			

			try {
								
				ranked = engine.rank(webServices, template);

				logger.debug(engine.getClass().getSimpleName()
						+ " ranking results: ");
				for (Service result : ranked) {
					//list the Web service
					logger.debug(result.resource);						
				}

			} catch (Exception e) {
				logger.warn("Ranking failed on "
						+ engine.getClass().getSimpleName() + " engine\n "
						+ e.getMessage());
				e.printStackTrace();
				continue;
			}
		}

		return ranked;
	}

}
