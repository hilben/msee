package old.at.sti2.msee.registration.core.test;
///**
// * 
// */
//package at.sti2.msee.registration.core.test;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
///**
// * @author Benjamin Hiltpolt
// *
// *  Helper class to extract the wsdls file whichare used for the tests
// *
// */
//public class TestWebserviceExtractor {
//	
//	
//	public static final String WEBSERVICEPASSFOLDER = "/webservicesPass/";
//	public static final String WEBSERVICEFAILFOLDER = "/webservicesFail/";
//
//	private static Logger logger = Logger.getLogger(RegistrationTest.class);
//
//    private static List<URL> passingWSDLs = new ArrayList<URL>();;
//    private static List<URL> failingWSDLs = new ArrayList<URL>();
//	
//	
//	static {
//		URL webservicePassURL;
//		URL webserviceFailURL;
//
//		File webservicePassFolder;
//		File webserviceFailFolder;
//
//		File[] listOfFailingWebServices;
//		File[] listOfPassingWebServices;	
//	
//		webservicePassURL = new TestWebserviceExtractor().getClass().getResource(WEBSERVICEPASSFOLDER);
//		webserviceFailURL = new TestWebserviceExtractor().getClass().getResource(WEBSERVICEFAILFOLDER);
//
//		webservicePassFolder = new File(webservicePassURL.getFile());
//		webserviceFailFolder = new File(webserviceFailURL.getFile());
//
//		listOfPassingWebServices = webservicePassFolder.listFiles();
//		logger.info("Those services should pass registration: "
//				+ webservicePassFolder + "/"
//				+ listOfPassingWebServices);
//
//		listOfFailingWebServices = webserviceFailFolder.listFiles();
//		logger.info("Those services should fail registration: "
//				+ webserviceFailFolder + "/"
//				+ listOfPassingWebServices);
//
//		
//		for (File f : listOfPassingWebServices) {
//			URL url = null;
//			try {
//				url = f.toURI().toURL();
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//			
//		    passingWSDLs.add(url);
//		}
//		for (File f : listOfFailingWebServices) {
//			URL url = null;
//			try {
//				url = f.toURI().toURL();
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//			
//			failingWSDLs.add(url);
//		}
//	}
//	
//	public static List<URL> getPassingWSDLs() {
//		return passingWSDLs;
//	}
//	
//	public static List<URL> getFailingWSDLs() {
//		return failingWSDLs;
//	}
//	
//	
//	public static void main(String args[]) {
//		
//		logger.info("Passing: " + passingWSDLs);
//		
//		logger.info("Failing: " + failingWSDLs);		
//	}
//}
