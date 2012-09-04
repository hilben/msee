/**
 * 
 */
package at.sti2.wsmf.core.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
import at.sti2.wsmf.core.PersistentHandler;
import junit.framework.TestCase;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class TestPersistenceHandler extends TestCase {

	public static final String URL[] = {
			"http://sesa.sti2.at:8080/invoker-dummy-webservice/services/valenciatPortWebService",
			"http://localhost:9292/at.sti2.ngsee.testwebservices/services/randomnumber",
			"http://localhost:9292/at.sti2.ngsee.testwebservices/services/reversestring",
			"http://localhost:9292/at.sti2.ngsee.testwebservices/services/stringuppercase",
			"http://localhost:9292/at.sti2.ngsee.testwebservices/services/randomstring",
			"http://localhost:9292/at.sti2.ngsee.testwebservices/services/stringmulti"};

	public void testPersistenceHandler() {
		PersistentHandler phandler = null;
		try {
			phandler = PersistentHandler.getInstance();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.toString());
		}

		try {

			for (int i = 0; i < URL.length; i++) {

				System.out.println("#######################################");
				System.out.println("# PRINTING DATA FOR " + URL[i]);
				System.out.println("#######################################");
				
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.RequestTotal));

				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.PayloadSizeAverage));
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.PayloadSizeMaximum));
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.PayloadSizeMinimum));
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.RequestFailed));
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.RequestSuccessful));
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.RequestTotal));
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.ResponseTimeAverage));
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.ResponseTimeMaximum));
				System.out.println(phandler.getQoSParam(new URL(URL[i]),
						QoSParamKey.ResponseTimeMinimum));

			}
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (MalformedQueryException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
