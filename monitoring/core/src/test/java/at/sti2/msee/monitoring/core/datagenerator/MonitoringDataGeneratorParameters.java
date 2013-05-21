package at.sti2.msee.monitoring.core.datagenerator;

import java.net.URL;
import java.util.Date;

public class MonitoringDataGeneratorParameters {
	long invocationCount;
	URL ws;
	long minPayloadSizeResponse;
	long maxPayloadSizeResponse;
	long minPayloadSizeRequest;
	long maxPayloadSizeRequest;
	long minResponseTime;
	long maxResponseTime;
	double failRate;
	Date startDate;
	long timeMinutes;

	public MonitoringDataGeneratorParameters(URL ws, long invocationCount,
			long minPayloadSizeResponse, long maxPayloadSizeResponse,
			long minPayloadSizeRequest, long maxPayloadSizeRequest,
			long minResponseTime, long maxResponseTime, double failRate, Date startDate, long time) {

		super();
		this.ws = ws;
		this.invocationCount = invocationCount;
		this.minPayloadSizeResponse = minPayloadSizeResponse;
		this.maxPayloadSizeResponse = maxPayloadSizeResponse;
		this.minPayloadSizeRequest = minPayloadSizeRequest;
		this.maxPayloadSizeRequest = maxPayloadSizeRequest;
		this.minResponseTime = minResponseTime;
		this.maxResponseTime = maxResponseTime;
		this.failRate = failRate;
		this.startDate = startDate;
		this.timeMinutes = time;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
