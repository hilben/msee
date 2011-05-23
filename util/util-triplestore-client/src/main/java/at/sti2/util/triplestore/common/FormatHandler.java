/**
 * util-sesame-client - to.networld.util.data.sesame.common
 *
 * Copyright (C) 2011 by Networld Project
 * Written by Alex Oberhauser <oberhauseralex@networld.to>
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

package at.sti2.util.triplestore.common;

import org.openrdf.rio.RDFFormat;

/**
 * @author Alex Oberhauser
 */
public class FormatHandler {
	
	public static RDFFormat getRDFFormatByMimeType(String _mimeType) {
		RDFFormat rdfFormat = RDFFormat.forMIMEType(_mimeType); 
		if ( _mimeType.equalsIgnoreCase("n3") )
			rdfFormat = RDFFormat.N3;
		else if ( _mimeType.equalsIgnoreCase("turtle") )
			rdfFormat = RDFFormat.TURTLE;
		else if ( _mimeType.equalsIgnoreCase("trix") )
			rdfFormat = RDFFormat.TRIX;
		else if ( _mimeType.equalsIgnoreCase("trig") )
			rdfFormat = RDFFormat.TRIG;
		else if ( _mimeType.equalsIgnoreCase("ntriples") )
			rdfFormat = RDFFormat.NTRIPLES;
		else if ( _mimeType.equalsIgnoreCase("rdfxml") )
			rdfFormat = RDFFormat.RDFXML;
		if ( rdfFormat == null )
			rdfFormat = RDFFormat.RDFXML;
		return rdfFormat;
	}
}
