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

import java.util.Set;

import org.omwg.ontology.Ontology;
import org.wsmo.service.Goal;

public class GoalDescription {
	private Goal goal;
	private Set<Ontology> ontologies;
	

	public GoalDescription(Goal service) {
		this.goal = service;
		this.ontologies = null;
	}
	
	public GoalDescription(Goal service, Set<Ontology> ontologies) {
		this.goal = service;
		this.ontologies = ontologies;
	}
	
	public Set<Ontology> getOntologies() {
		return ontologies;
	}
	public void setOntologies(Set<Ontology> ontologies) {
		this.ontologies = ontologies;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	
}
