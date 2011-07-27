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
package at.sti2.wsmf.core;

import java.util.Timer;

import org.apache.log4j.Logger;

/**
 * @author Alex Oberhauser
 *
 */
public class WSAvailabilityChecker {
	private static Logger log = Logger.getLogger(WSAvailabilityChecker.class);
	private static WSAvailabilityChecker instance = null;
	
	private final Timer timer = new Timer();
	private final int minutes;
	
	public static WSAvailabilityChecker getInstance(int _minutes) {
		if ( null == instance )
			instance = new WSAvailabilityChecker(_minutes);
		return instance;
	}
	
	private WSAvailabilityChecker(int _minutes) {
		this.minutes = _minutes;
		this.start();
	}
	
	public void start() {
		log.info("Web Service availability checks scheduled for each '" + this.minutes + "' minutes");
        this.timer.schedule(new WSAvailabilityTimerTask(), 10, minutes * 60 * 1000);
    }

}
