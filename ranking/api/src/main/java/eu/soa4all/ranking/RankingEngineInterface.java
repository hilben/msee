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

package eu.soa4all.ranking;

import java.util.Set;

import at.sti2.msee.ranking.api.exception.RankingException;
import eu.soa4all.validation.RPC.Service;

/**
 * An interface that must be implemented by every 
 * ranking engine that is to be include in the 
 * Ranking framework
 *  
 * @author Ioan Toma
 * 
 **/

public interface RankingEngineInterface extends Ranking{

	/**
	 * Adds a Service and the set of ontologies describing its nfps
	 * to the ranking engine
	 * @throws RankingException 
	 */
	public void addService(Service service) throws RankingException;

	/**
	 * Adds the set of services and the associated sets of ontologies 
	 * describing their nfps to the ranking engine
	 * @throws RankingException 
	 */	
	public void addServices(Set<Service> services) throws RankingException;
	
	/**
	 * Removes a Web service and the set of ontologies describing its nfps
	 * from the ranking engine
	 */
	public void removeService(Service service);
	
	/**
	 * Removes the set of Web services and the associated sets of ontologies 
	 * describing their nfps from the ranking engine
	 */
	public void removeServices(Set<Service> services);
    
}
