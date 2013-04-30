package at.sti2.msee.monitoring.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonitoringWebserviceAvailability {

	private MonitoringWSAvailabilityState state;
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");;
	
	Date time;

	public MonitoringWebserviceAvailability(MonitoringWSAvailabilityState state, Date time) {
		this.state = state;
		this.time = time;
	}
	
	public MonitoringWebserviceAvailability(String state, String time) throws ParseException {
		this.state = MonitoringWSAvailabilityState.valueOf(state);
		this.time = format.parse(time);
	}
	
	public MonitoringWSAvailabilityState getState() {
		return state;
	}

	public void setState(MonitoringWSAvailabilityState state) {
		this.state = state;
	}

	public String getTime() {
		return format.format(time);
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "MonitoringWebserviceAvailability [state=" + state + ", time="
				+ time + "]";
	}
	
}
