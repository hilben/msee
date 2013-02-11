/**
 * 
 */
package at.sti2.wsmf.core.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openrdf.model.datatypes.XMLDateTime;

import junit.framework.TestCase;

/**
 * @author Benjamin Hiltpolt
 * 
 */
public class TestDateTimeStorage extends TestCase {

	public void testDateTimeStorage() throws ParseException {
		XMLDateTime test = null;
		XMLDateTime test2 = null;
		try {
			test = new XMLDateTime("1999-05-31T13:20:00-05:00");
			test2 = new XMLDateTime("2012-11-05T16:14:05+01:00");

			Date a = new Date();
			System.out.println("Date: " + a);

			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ssZ");
			StringBuffer sb = new StringBuffer(sdf.format(a));

			System.out.println("Formated date: + [" + sb + "]");

			System.out.println(test);
			System.out.println(test2);

			sb.insert(22, ':');
			test = new XMLDateTime(sb.toString());
			System.out.println(test);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			TestCase.fail();
		}

		TestCase.assertNotNull(test);
		TestCase.assertNotNull(test2);
	}

}
