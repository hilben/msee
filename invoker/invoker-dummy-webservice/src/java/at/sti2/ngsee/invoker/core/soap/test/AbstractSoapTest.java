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
package at.sti2.ngsee.invoker.core.soap.test;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

public class AbstractSoapTest extends TestCase{
	
	protected static Logger logger = Logger.getLogger(AbstractSoapTest.class);
	
	protected long start;
	
	protected synchronized void startTimer() {
		this.start = System.currentTimeMillis();
	}

	protected synchronized long stopTimer() {
		long end = System.currentTimeMillis();
		return end-start;
	}

}
