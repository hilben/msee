package at.sti2.msee.monitoring.api.qos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QoSParameter {
	private QoSType type;
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");;
	
	Date time;
	String value;
	
	
	public QoSParameter(String type, String value, String time) throws ParseException {
		this.type = QoSType.valueOf(type);
		this.value = value;
		this.time = format.parse(time);	
	}
	
	public QoSParameter(QoSType type, String value, String time) throws ParseException {
		this.type = type;
		this.value = value;
		this.time = format.parse(time);	
	}

	public QoSParameter(QoSType type, String value, Date time) {
		super();
		this.type = type;
		this.value = value;
		this.time = time;
	}

	public QoSType getType() {
		return type;
	}

	public void setType(QoSType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTime() {
		return QoSParameter.format.format(this.time);
	}
	
	public Date getDate() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "QoSParameter [type=" + type + ", time=" + time + ", value="
				+ value + "]";
	}
}
