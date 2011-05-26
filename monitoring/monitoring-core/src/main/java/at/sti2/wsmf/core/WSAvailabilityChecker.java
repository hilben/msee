/**
 * WSAvailabilityCrontab.java - at.sti2.wsmf.core
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
