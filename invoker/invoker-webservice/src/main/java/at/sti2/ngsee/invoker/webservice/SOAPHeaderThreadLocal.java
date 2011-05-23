/**
 * SOAPHeaderThreadLocal.java - at.sti2.ngsee.invoker_webservice
 */
package at.sti2.ngsee.invoker.webservice;

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
