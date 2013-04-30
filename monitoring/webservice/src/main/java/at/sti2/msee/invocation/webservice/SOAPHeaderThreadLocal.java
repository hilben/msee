/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.msee.invocation.webservice;

import java.util.List;

import org.apache.cxf.headers.Header;

/**
 * @author Alex Oberhauser
 *
 */
public abstract class SOAPHeaderThreadLocal {
	 private static ThreadLocal<List<Header>> threadLocal = new ThreadLocal<List<Header>>();
	 
	 public static void set(List<Header> _soapHeaderList) {
		 threadLocal.set(_soapHeaderList);
	}
	 
	  public static List<Header> get() {
		  return threadLocal.get();
	  }
}