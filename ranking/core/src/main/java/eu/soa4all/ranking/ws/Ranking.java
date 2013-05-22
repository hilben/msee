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

package eu.soa4all.ranking.ws;

import java.io.ByteArrayInputStream;

import org.ontoware.rdf2go.model.Syntax;
import org.wsmo.common.exception.InvalidModelException;

import eu.soa4all.ranking.RankingException;
import eu.soa4all.ranking.rules.RulesRanking;
import eu.soa4all.ranking.util.HTTPReader;
import eu.soa4all.ranking.wsmolite.WSMOLiteRDFReader;
import eu.soa4all.validation.RPC.MSMService;
import eu.soa4all.validation.RPC.Service;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;

public class Ranking {

	public String[] rank(String[] servicesURI, String templateURI,
			String instances) throws RankingException {

		RulesRanking engine = new RulesRanking();

		// load the services
		for (int i = 0; i < servicesURI.length; i++) {
			registerWebServiceByURI(servicesURI[i], engine);
		}
		// load the service template
		loadServiceTemplateByURI(templateURI, engine);

		// load the instances
		WSMOLiteRDFReader rdfReader = new WSMOLiteRDFReader();
		rdfReader.readContent(instances);
		try {
			engine.setInstancesOntology(rdfReader.getInstances());
		} catch (InvalidModelException e) {
			e.printStackTrace();
		}

		// do the ranking
		String[] rankedServices = engine.rankServices();

		engine.removeAllServices();
		engine.setServiceTemplate(null);

		return rankedServices;
	}

	private ServiceTemplate loadServiceTemplateByURI(String templateURI,
			RulesRanking engine) {
		HTTPReader httpReader = new HTTPReader();
		byte[] bytes = httpReader.read(templateURI).getBytes();

		ServiceTemplate st = new ServiceTemplate();
		st.readFrom(new ByteArrayInputStream(bytes), Syntax.Ntriples);
		engine.setServiceTemplate(st);
		return st;
	}

	private void registerWebServiceByURI(String webServiceURI,
			RulesRanking engine) throws RankingException {
		HTTPReader httpReader = new HTTPReader();
		byte[] bytes = httpReader.read(webServiceURI).getBytes();

		Service service = new MSMService();
		service.readFrom(new ByteArrayInputStream(bytes), Syntax.Ntriples);
		engine.addService(service);
	}

}
