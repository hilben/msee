package at.sti2.msee.monitoring.testws;

public abstract class AbstractTestWebService {
	
	protected static int minResponseTime = 10;
	protected static int maxResponseTime = 100;
	protected static int minPayloadSize  = 10;
	protected static int maxPayloadSize  = 100;
	
	
	
	public String getAnswer(String input) {
		
	     try {
			Thread.sleep(minResponseTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	     
	     
	     String returnString = "";
	     for (int i = 0;i < (minPayloadSize); i++) {
	    	returnString += "xxx";
	     }
	     
	     
	     for (int i = 0;i < (int)(maxPayloadSize*Math.random())-minPayloadSize; i++) {
	    	returnString += "aaa";
	     }
	     
	     
	     try {
	    	 
			Thread.sleep((long) ((Math.random()*(maxResponseTime-minResponseTime))));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	     
	     return input+returnString;
		
	}

}
