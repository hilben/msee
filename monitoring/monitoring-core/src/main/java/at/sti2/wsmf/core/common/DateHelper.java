package at.sti2.wsmf.core.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alex Oberhauser
 */
public abstract class DateHelper {
	
	/**
	 * @return The dateTime in xsd format with timezone.
	 */
	public static String getXSDDateTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		return simpleDateFormat.format(new Date());
	}
	
	public static String getDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date());
	}
}
