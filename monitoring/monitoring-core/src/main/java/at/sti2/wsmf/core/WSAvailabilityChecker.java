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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.core.data.WebServiceEndpoint;

/**
 * @author Alex Oberhauser
 *
 */
public class WSAvailabilityChecker {
	
	
	private static HashMap<String, WSAvailabilityChecker> instances = new HashMap<String, WSAvailabilityChecker>();
	
	private static String endpoint;
	
	private static Logger log = Logger.getLogger(WSAvailabilityChecker.class);
	private static WSAvailabilityChecker instance = null;
	
	private final Timer timer = new Timer();
	private final int minutes;
	
	public static WSAvailabilityChecker getInstance(String endpoint, int _minutes) {
		if ( null == instances.get(endpoint) )
			{instance = new WSAvailabilityChecker(_minutes);
			instances.put(endpoint, instance);}
		
		return instance;
	}
	
	private WSAvailabilityChecker(int _minutes) {
		this.minutes = _minutes;
		this.start();
	}
	
	
	//TODO: Exception Handling!!!
	public void start() {
		log.info("Web Service availability checks scheduled for each '" + this.minutes + "' minutes");
        try {
			this.timer.schedule(new WSAvailabilityTimerTask(new WebServiceEndpoint(new URL(endpoint))), 10, minutes * 60 * 1000);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
