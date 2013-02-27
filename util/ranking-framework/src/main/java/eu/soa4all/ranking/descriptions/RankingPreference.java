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

package eu.soa4all.ranking.descriptions;

import java.util.Map;

import org.omwg.ontology.Ontology;
import org.wsmo.common.IRI;

import eu.soa4all.ranking.rules.NFPInfo;
import eu.soa4all.ranking.util.Constants;


/**
 * Ranking Preference
 * 
 * Created on Jul 19, 2006
 * 
 * @author Ioan Toma
 * 
 **/

public class RankingPreference {
	// nfp attribute user is interested in, the value of
	// its interest represented as a float value and the 
	// direction of ordering for that nfp 
	private Map<IRI, NFPInfo> nfpAttributes; // the list of NFPs and their weights
	private Integer order;

	public RankingPreference(Map<IRI, NFPInfo> NFPAttribute, Integer order) {
		this.nfpAttributes = NFPAttribute;
		this.order = Constants.ASCENDING;
	}

	public RankingPreference(Map<IRI, NFPInfo> NFPAttribute,
			Ontology inputKnowledge) {
		this.nfpAttributes = NFPAttribute;
		this.order = Constants.ASCENDING;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Map<IRI, NFPInfo> getNfpAttributes() {
		return nfpAttributes;
	}

	public void setNfpAttributes(Map<IRI, NFPInfo> nfpAttributes) {
		this.nfpAttributes = nfpAttributes;
	}

}
