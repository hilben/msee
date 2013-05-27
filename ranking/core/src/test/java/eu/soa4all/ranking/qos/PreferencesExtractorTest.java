/**
 * 
 */
package eu.soa4all.ranking.qos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.Syntax;

import eu.soa4all.ranking.rules.PreferencesExtractor;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;

/**
 * @author Benjamin Hiltpolt
 *
 * Small test for the Preferences Extractor
 * 
 * TODO: continue implementing
 */
public class PreferencesExtractorTest extends TestCase{
	
	
	private static Logger logger = Logger.getLogger(PreferencesExtractorTest.class);
	
	
	public void testPreferencesExtractorTest()  {
		// Loading service template
		ServiceTemplate template = new ServiceTemplate();
		PreferencesExtractor pe = null;
		ClassLoader l = getClass().getClassLoader();
		
		
//		logger.info("Loading service template");

//		try {
//			template.readFrom(new FileInputStream(l.getResource("stQoSParams.rdf.n3").getFile()), Syntax.Ntriples);
//		} catch (FileNotFoundException e) {
//			logger.error(e.getCause());
//		}
//	
//		logger.info("extract nfps");
//		pe = new PreferencesExtractor(template);
//		try {
//			pe.process();
//		} catch (Exception e) {
//			logger.error(e.getCause());
//		}
//		
//		logger.info("NFPs:");
//		logger.info(pe.getNfps().keySet());
//		logger.info(pe.getNfps().entrySet());
//		
//		logger.info("Ordering value:");
//		logger.info(pe.getOrderingValue());
		
		
		logger.info("Load stShipping.rdf.n3");
		ServiceTemplate shippingtemplate = new ServiceTemplate();
		try {
			shippingtemplate.readFrom(new FileInputStream(l.getResource("stShipping.rdf.n3").getFile()), Syntax.Ntriples);
		} catch (FileNotFoundException e) {
			logger.error(e.getCause());
		}
		
		logger.info("extract nfps");
	    pe = new PreferencesExtractor(shippingtemplate);
		try {
			pe.process();
		} catch (Exception e) {
			logger.error(e.getCause());
		}
		
		logger.info("NFPs:");
		logger.info(pe.getNfps().keySet());
		logger.info(pe.getNfps().entrySet());
		
		logger.info("Ordering value:");
		logger.info(pe.getOrderingValue());
		
	}

}
