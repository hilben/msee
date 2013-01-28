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

import org.wsmo.common.IRI;

public class NFPInfo {	
	private IRI name;
	private Float importance;
	private Integer orderingValue;
	
	
	public NFPInfo(IRI name, Float importance, Integer orderingValue) {
		super();
		this.importance = importance;
		this.name = name;
		this.orderingValue = orderingValue;
	}
	
	public IRI getName() {
		return name;
	}
	public void setName(IRI name) {
		this.name = name;
	}
	public Float getImportance() {
		return importance;
	}
	public void setImportance(Float importance) {
		this.importance = importance;
	}
	public Integer getOrderingValue() {
		return orderingValue;
	}
	public void setOrderingValue(Integer orderingValue) {
		this.orderingValue = orderingValue;
	}
	
	

}
