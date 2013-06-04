package at.sti2.msee.monitoring.api.qos;

/**
 * @author Benjamin Hiltpolt
 * 
 * 
 * 
 *         This enum represents all possible QoS Parameters (should be used for
 *         monitoring webservice)
 */
public enum QoSType {

	RequestTotal("Number"), RequestSuccessful("Number"), RequestFailed("Number"),

	MonitoredTime("Minutes"), AvailableTime("Minutes"), UnavailableTime(
			"Minutes"),

	PayloadSizeResponse("Bytes"), PayloadSizeResponseMinimum("Bytes"), PayloadSizeResponseAverage(
			"Bytes"), PayloadSizeResponseMaximum("Bytes"), PayloadSizeResponseTotal(
			"Bytes"),

	PayloadSizeRequest("Bytes"), PayloadSizeRequestMinimum("Bytes"), PayloadSizeRequestAverage(
			"Bytes"), PayloadSizeRequestMaximum("Bytes"), PayloadSizeRequestTotal(
			"Bytes"),

	ResponseTime("Milliseconds"), ResponseTimeMinimum("Milliseconds"), ResponseTimeMaximum(
			"Milliseconds"), ResponseTimeAverage("Milliseconds"), ResponseTimeTotal(
			"Milliseconds");

	private String unit;

	private QoSType(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
	
		return this.unit;
	}
}