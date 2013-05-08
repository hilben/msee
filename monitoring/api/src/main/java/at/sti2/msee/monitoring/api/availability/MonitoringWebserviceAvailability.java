package at.sti2.msee.monitoring.api.availability;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonitoringWebserviceAvailability {

	private MonitoringWebserviceAvailabilityState state;
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");;
	
	Date time;

	public MonitoringWebserviceAvailability(MonitoringWebserviceAvailabilityState state, Date time) {
		this.state = state;
		this.time = time;
	}
	
	public MonitoringWebserviceAvailability(String state, String time) throws ParseException {
		this.state = MonitoringWebserviceAvailabilityState.valueOf(state);
		this.time = format.parse(time);
	}
	


	public MonitoringWebserviceAvailabilityState getState() {
		return state;
	}

	public void setState(MonitoringWebserviceAvailabilityState state) {
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
