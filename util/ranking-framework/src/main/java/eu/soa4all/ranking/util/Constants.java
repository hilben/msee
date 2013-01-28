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

/**
 * Constants used in the ranking implementation
 * 
 * @author Ioan Toma
 * 
 **/

public abstract class Constants {
	
	//simple ranking/sorting algorithms
	public static final int SIMPLE = 0;
	public static final int VALUEQA = 1;	
	
	public static final int ASCENDING = 0;
	public static final int DESCENDING = 1;	
	
	public static final int BORDA = 0;
	public static final int MARKOV_CHAINS = 1;

	public static final int KENDALL = 0;
	public static final int SPEARMAN = 1;
	
	public static final int MC1 = 1;
	public static final int MC2 = 2;
	public static final int MC3 = 3;
	public static final int MC4 = 4;	
	
}