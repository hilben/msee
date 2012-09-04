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


import java.util.List;
import java.util.Set;

import eu.soa4all.validation.RPC.Service;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;

/**
 * Ranking interface that has to be implemented by every ranking component
 * 
 * @author Ioan Toma
 * 
 **/

public interface Ranking {
	/**
	 * Ranks the list of service according to users preferences  
	 * @param services - the set of web services to be ranked
	 * @param template - the user goal that contains its preferences including the NFP according to 
	 * 							  which the set of services must be ordered, the direction of 
	 * 							  ordering (ascending, descending) and the knowledge collected from
	 * 							  the user goal  
	 * @return - the list of services that were ranked
	 */	
	public List<Service> rank(Set<Service> webServices, ServiceTemplate template);	
}
