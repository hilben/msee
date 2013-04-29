package at.sti2.msee.monitoring.api.qos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QoSParameter {
	private QoSParamKey key;
	private SimpleDateFormat time = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");;
	String value;

	public QoSParameter(QoSParamKey key, String value, Date time) {
		super();
		this.key = key;
		this.value = value;
	}

	public QoSParamKey getKey() {
		return key;
	}

	public void setKey(QoSParamKey key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SimpleDateFormat getTime() {
		return time;
	}

	public void setTime(SimpleDateFormat time) {
		this.time = time;
	}

	/**
	 * TODO: where to put this
	 * 
	 * @return The dateTime in xsd format with timezone.
	 */
	public static String getXSDDateTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		return simpleDateFormat.format(new Date());
	}

}
