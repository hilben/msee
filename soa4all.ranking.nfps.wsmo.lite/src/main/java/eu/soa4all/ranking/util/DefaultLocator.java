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

package eu.soa4all.ranking.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.apache.log4j.Logger;
import org.omwg.ontology.Ontology;
import org.wsmo.common.Entity;
import org.wsmo.common.Identifier;
import org.wsmo.common.TopEntity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.factory.Factory;
import org.wsmo.locator.Locator;
import org.wsmo.service.Goal;
import org.wsmo.service.WebService;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;


/**
 * A default locator. Retrieves ontologies, Web Services and goals from their specified IRIs.
 * 
 */
public class DefaultLocator implements Locator {

	protected static Logger logger = Logger.getLogger(DefaultLocator.class);
	
	private Parser parser = Factory.createParser(null);
	
	public Entity lookup(Identifier id, Class type) throws SynchronisationException {
		Entity entity = null;
		
		if (type.equals(Ontology.class) || type.equals(WebService.class) || 
				type.equals(Goal.class)) {
			try {
				URL url = new URL(id.toString());
				Reader reader = new InputStreamReader(url.openStream());
				
				TopEntity[] entities = parser.parse(reader);
				for (TopEntity topEntity : entities)
					if (type.isInstance(topEntity)) {
						entity = topEntity;
						break;
					}			
			} catch (MalformedURLException e) {
				logger.error("Invalid " + type.getSimpleName() + " identifier " + id, e);
			} catch (IOException e) {
				logger.error("IOException while retrieving " + type.getSimpleName() + " " + id, e);
			} catch (ParserException e) {
				logger.error("Failed to parse " + type.getSimpleName() + " " + id, e);
			} catch (InvalidModelException e) {
				logger.error("Invalid " + type.getSimpleName() + " model for " + id, e);
			}
		}
		return entity;
	}

	public Set lookup(Identifier arg0) throws SynchronisationException {
		return null;
	}
	
}