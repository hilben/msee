/*
 * Copyright (c) 20079, University of Innsbruck, Austria.
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

import eu.soa4all.ranking.Ranking;
import eu.soa4all.ranking.RankingEngineInterface;
import eu.soa4all.ranking.rules.RulesRanking;
import eu.soa4all.ranking.util.Constants;

/**
 * Ranking Factory
 * 
 * @author Ioan Toma
 * 
 **/
public class RankingFactory {
	
	public static Ranking createRankingEngine() {
		return new RulesRanking();
	}
	
	/**
	 * Returns a ranking engine 
	 */
	public static RankingEngineInterface createRankingEngine(int type) 
			throws UnsupportedOperationException {
		
		RankingEngineInterface rankingEngine = null;
				
		switch (type) {
		case Constants.VALUEQA:{
			rankingEngine = new RulesRanking();  
			break;
		}
		default:
			break;
		} 
			
		return rankingEngine;
	}
}