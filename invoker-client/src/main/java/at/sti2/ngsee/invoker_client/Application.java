package at.sti2.ngsee.invoker_client;

import java.io.IOException;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class Application {
	
    public static void main(String[] args) throws UnsupportedOperationException, SOAPException, IOException {
    	Logger logging = Logger.getLogger(Application.class);
    	logging.setLevel(Level.ALL);
    	
    	Invoker invoker = new Invoker();
    	
    	logging.info("[*] Service Invoker Version: " + invoker.getVersion());
    	logging.info("[*] Invoking Example Service: \n---\n" + invoker.invokeService("NGSW-Europe-1", "getStatus", "") + "\n---");
    }
}
