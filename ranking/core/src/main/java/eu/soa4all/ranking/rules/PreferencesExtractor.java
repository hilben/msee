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

package eu.soa4all.ranking.rules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.wsmo.common.IRI;
import org.wsmo.factory.Factory;
import org.wsmo.factory.WsmoFactory;

import eu.soa4all.ranking.util.Constants;
import eu.soa4all.validation.ServiceTemplate.Preference;
import eu.soa4all.validation.ServiceTemplate.RuleBasedRankingPreference;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;
import eu.soa4all.validation.WSMOLite.Annotation;

public class PreferencesExtractor {

	private Map<IRI, NFPInfo> nfps;
	private Integer orderingValue;

	private WsmoFactory wsmoFactory = Factory.createWsmoFactory(new HashMap<String, Object>());
	protected static Logger logger = Logger
			.getLogger(PreferencesExtractor.class);

	private ServiceTemplate template;

	public PreferencesExtractor(ServiceTemplate template) {
		this.template = template;
		this.nfps = new HashMap<IRI, NFPInfo>();
	}

	public void process()  {
		if (this.template != null) {

			orderingValue = new Integer(Constants.DESCENDING);

			// get the NFPs, their associated interest values and their ordering
			// direction
			String nfpName, interestValue, order;
			Integer orderingVal = new Integer(-1);

			for (Iterator<Preference> preferences = template.getPreferences(); preferences
					.hasNext();) {
				RuleBasedRankingPreference preference = (RuleBasedRankingPreference)preferences.next();

				if (RuleBasedRankingPreference.class
						.isAssignableFrom(preference.getClass())) {

					nfpName = ((RuleBasedRankingPreference) preference)
							.getNonFunctionalProperty().toString();
					interestValue = new Float(((RuleBasedRankingPreference) preference)
							.getInterestValue()).toString();
					order = (((RuleBasedRankingPreference) preference).isOrderedAscending() ? "Ascending"
							: "Descending");

					if (order != null) {
						if (order.contains("ascending"))
							orderingVal = new Integer(Constants.ASCENDING);
						else
							orderingVal = new Integer(Constants.DESCENDING);
					}

					if (nfpName != null && interestValue != null
							&& order != null) {
						nfps.put(wsmoFactory.createIRI(nfpName), new NFPInfo(
								wsmoFactory.createIRI(nfpName), new Float(
										interestValue), orderingVal));
					}
				}
			}
		}
		for(Iterator<Annotation> requirements = template.getRequirements(); requirements.hasNext(); ){
			requirements.next().toString();
		}
		
	}

	public Map<IRI, NFPInfo> getNfps() {
		return nfps;
	}

	public Integer getOrderingValue() {
		return orderingValue;
	}
}
