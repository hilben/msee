package at.sti2.ngsee.invoker.core.soap.test;

public class AbstractSoapTest {
	protected long start;
	
	protected synchronized void startTimer() {
		this.start = System.currentTimeMillis();
	}

	protected synchronized void stopTimer() {
		long end = System.currentTimeMillis();
		System.out.println("took ms: " + (end - start));
	}

}
