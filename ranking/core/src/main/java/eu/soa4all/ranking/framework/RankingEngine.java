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

package eu.soa4all.ranking.framework;

import java.util.Set;

import at.sti2.msee.ranking.api.exception.RankingException;
import eu.soa4all.ranking.RankingEngineInterface;
import eu.soa4all.validation.RPC.Service;


/**
 * An interface that mut be implemented by every 
 * ranking engine that is to be include in the 
 * Ranking framework
 *  
 * @author Ioan Toma
 * 
 **/
public abstract class RankingEngine implements RankingEngineInterface {

    public void removeServices(Set<Service> services) {
        for(Service s:services)
        	removeService(s);        
    }

    public void addServices(Set<Service> services)
            throws RankingException {
        for(Service s:services)
        	addService(s);                
    }
}
