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

/**
 * Extractor class allows to extract useful information 
 * from semantic descriptions e.g. ontologies, goals, services. 
 * 
 * @author Ioan Toma
 * 
 **/
package eu.soa4all.ranking.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.wsmo.common.IRI;
import org.wsmo.factory.Factory;
import org.wsmo.factory.WsmoFactory;
import org.wsmo.service.Goal;

public class Extractor {
	private static WsmoFactory wsmoFactory = Factory.createWsmoFactory(new HashMap());
	
	/*
	 * Gets the requested nfps and their importance from the goal
	 */
	public static HashMap<IRI,Float> getNfpsAndImportances(Goal goal) {		
		HashMap<IRI, Float> result = new HashMap<IRI, Float>();
		IRI nfpID = wsmoFactory.createIRI("http://www.wsmo.org/ontologies/nfp/upperOnto.wsml#nfp");
		IRI nfpImportance = wsmoFactory.createIRI("http://www.wsmo.org/ontologies/nfp/upperOnto.wsml#top");	
		Set<Object> nfps = new HashSet<Object>(), importances = new HashSet<Object>(); 
		if (goal.listNFPValues().containsKey(nfpID))
			nfps = goal.listNFPValues().get(nfpID);
				
		if (goal.listNFPValues().containsKey(nfpImportance))
			importances = goal.listNFPValues().get(nfpImportance);			
		
		if(nfps.size() == importances.size()){
			Object[] nfpsArray = nfps.toArray();
			Object[] importancesArray = importances.toArray();			
			for (int i=0; i<nfpsArray.length; i++){
				result.put((IRI)nfpsArray[i], new Float(importancesArray[i].toString()));				
			}
		}
		return result;
	}

	/*
	 * Gets the requested nfps and their functions from the goal
	 */
	public static HashMap<IRI,IRI> getNfpsAndFunctions(Goal goal) {		
		HashMap<IRI, IRI> result = new HashMap<IRI, IRI>();
		IRI nfpID = wsmoFactory.createIRI("http://www.wsmo.org/ontologies/nfp/upperOnto.wsml#nfp");
		IRI nfpFunction = wsmoFactory.createIRI("http://www.wsmo.org/ontologies/nfp/upperOnto.wsml#nfpFunction");	
		Set<Object> nfps = new HashSet<Object>(), functions = new HashSet<Object>(); 
		if (goal.listNFPValues().containsKey(nfpID))
			nfps = goal.listNFPValues().get(nfpID);
				
		if (goal.listNFPValues().containsKey(nfpFunction))
			functions = goal.listNFPValues().get(nfpFunction);			
		
		if(nfps.size() == functions.size()){
			Object[] nfpsArray = nfps.toArray();
			Object[] functionsArray = functions.toArray();			
			for (int i=0; i<nfpsArray.length; i++){
				result.put((IRI)nfpsArray[i], (IRI)functionsArray[i]);				
			}
		}
		return result;
	}
	
	/*
	 * Gets the ordering preference
	 */
	public static int getOrderingPreference(Goal goal) {		
		int result = Constants.ASCENDING;
		IRI order = wsmoFactory.createIRI("http://www.wsmo.org/ontologies/nfp/upperOnto.wsml#order");
		
		if (goal.listNFPValues().containsKey(order)){
			if(goal.listNFPValues().get(order).iterator().next().toString().contains("descending"));
				result = Constants.DESCENDING;
		}		
		return result;
	}
	
}
