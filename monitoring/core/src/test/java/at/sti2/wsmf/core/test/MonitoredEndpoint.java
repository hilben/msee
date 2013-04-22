/**
 * 
 */
package at.sti2.wsmf.core.test;

/**
 * @author Benjamin Hiltpolt
 * 
 *         Dummyclass for testing
 * 
 */
public class MonitoredEndpoint {

	private String endpointName;
	private float responseTimeAverage;
	private float payloadSizeAverage;
	private int requestsTotal;

	/**
	 * @param responseTimeAverage
	 * @param payloadSizeAverage
	 * @param requestsTotal
	 */
	public MonitoredEndpoint(String EndpointName, float responseTimeAverage,
			float payloadSizeAverage, int requestsTotal) {

		this.endpointName = EndpointName;
		this.responseTimeAverage = responseTimeAverage;
		this.payloadSizeAverage = payloadSizeAverage;
		this.requestsTotal = requestsTotal;
	}

	public static MonitoredEndpoint getRandomMonitoredEndpoint(String name) {
		return new MonitoredEndpoint(name, (float) (Math.random() * 5000),
				(float) (Math.random() * 5000), (int) (Math.random() * 400));
	}

	public float getResponseTimeAverage() {
		return responseTimeAverage;
	}

	public void setResponseTimeAverage(float responseTimeAverage) {
		this.responseTimeAverage = responseTimeAverage;
	}

	public float getPayloadSizeAverage() {
		return this.payloadSizeAverage;
	}

	public void setPayloadSizeAverage(float payloadSizeAverage) {
		this.payloadSizeAverage = payloadSizeAverage;
	}

	public float getRequestsTotal() {
		return this.requestsTotal;
	}

	public void setRequestsTotal(int requestsTotal) {
		this.requestsTotal = requestsTotal;
	}

	public String getEndpointName() {
		return this.endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}
	
	@Override
	public String toString() {
		return "MonitoredEndpoint [endpointName=" + endpointName
				+ ", responseTimeAverage=" + responseTimeAverage
				+ ", payloadSizeAverage=" + payloadSizeAverage
				+ ", requestsTotal=" + requestsTotal + "]";
	}
}
