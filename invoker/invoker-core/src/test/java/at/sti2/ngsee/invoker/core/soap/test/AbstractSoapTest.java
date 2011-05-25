package at.sti2.ngsee.invoker.core.soap.test;

import org.apache.log4j.Logger;

public class AbstractSoapTest {
	
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
