package at.sti2.msee.monitoring.core.datagenerator;

import java.net.URL;

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

	public MonitoringDataGeneratorParameters(URL ws, long invocationCount,
			long minPayloadSizeResponse, long maxPayloadSizeResponse,
			long minPayloadSizeRequest, long maxPayloadSizeRequest,
			long minResponseTime, long maxResponseTime, double failRate) {

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
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
