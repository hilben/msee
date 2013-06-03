package at.sti2.msee.monitoring.core.datagenerator;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.monitoring.core.MonitoringParameterStoreHandler;

public class MonitoringDataGenerator {

	private URL ws;

	private long invocationCount;

	private long minPayloadSizeResponse;
	private long maxPayloadSizeResponse;

	private long minPayloadSizeRequest;
	private long maxPayloadSizeRequest;

	private long minResponseTime;
	private long maxResponseTime;

	private double failRate;

	private Date startDate;
	private long timeMinutes;

	public MonitoringDataGenerator(MonitoringDataGeneratorParameters gen) {
		this.ws = gen.ws;
		this.invocationCount = gen.invocationCount;
		this.minPayloadSizeResponse = gen.minPayloadSizeResponse;
		this.maxPayloadSizeResponse = gen.maxPayloadSizeResponse;
		this.minPayloadSizeRequest = gen.minPayloadSizeRequest;
		this.maxPayloadSizeRequest = gen.maxPayloadSizeRequest;
		this.minResponseTime = gen.minResponseTime;
		this.maxResponseTime = gen.maxResponseTime;
		this.failRate = gen.failRate;
		this.startDate = gen.startDate;
		this.timeMinutes = gen.timeMinutes;
	}

	public void createDataTest() throws RepositoryException, MalformedQueryException, UpdateExecutionException, IOException, MonitoringException, ParseException {
		MonitoringComponent m = MonitoringComponentImpl.getInstance();

		for (int i = 0; i < this.invocationCount; i++) {

			// Create a random date within the range
			long dtime = (long) (Math.random() * (this.timeMinutes * 1000 * 60));
			Date rndDate = new Date(this.startDate.getTime() + dtime);

			if (Math.random() > this.failRate) {

				double payloadSizeResponse = this.minPayloadSizeResponse
						+ (Math.random() * (this.maxPayloadSizeResponse - this.minPayloadSizeResponse));
				double payloadSizeRequest = this.minPayloadSizeRequest
						+ (Math.random() * (this.maxPayloadSizeRequest - this.minPayloadSizeRequest));
				double responseTime = this.minResponseTime
						+ (Math.random() * (this.maxResponseTime - this.minResponseTime));

				MonitoringParameterStoreHandler.addSuccessfulInvocation(
						this.ws, payloadSizeResponse, payloadSizeRequest,
						responseTime, rndDate);

			} else {

				MonitoringParameterStoreHandler.addUnsuccessfulInvocation(
						this.ws, rndDate);

			}
		}
	}

	public URL getWs() {
		return ws;
	}

	public long getInvocationCount() {
		return invocationCount;
	}

	public long getMinPayloadSizeResponse() {
		return minPayloadSizeResponse;
	}

	public long getMaxPayloadSizeResponse() {
		return maxPayloadSizeResponse;
	}

	public long getMinPayloadSizeRequest() {
		return minPayloadSizeRequest;
	}

	public long getMaxPayloadSizeRequest() {
		return maxPayloadSizeRequest;
	}

	public long getMinResponseTime() {
		return minResponseTime;
	}

	public long getMaxResponseTime() {
		return maxResponseTime;
	}

	public double getFailRate() {
		return failRate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public long getTimeMinutes() {
		return timeMinutes;
	}

	public void setWs(URL ws) {
		this.ws = ws;
	}

	public void setInvocationCount(long invocationCount) {
		this.invocationCount = invocationCount;
	}

	public void setMinPayloadSizeResponse(long minPayloadSizeResponse) {
		this.minPayloadSizeResponse = minPayloadSizeResponse;
	}

	public void setMaxPayloadSizeResponse(long maxPayloadSizeResponse) {
		this.maxPayloadSizeResponse = maxPayloadSizeResponse;
	}

	public void setMinPayloadSizeRequest(long minPayloadSizeRequest) {
		this.minPayloadSizeRequest = minPayloadSizeRequest;
	}

	public void setMaxPayloadSizeRequest(long maxPayloadSizeRequest) {
		this.maxPayloadSizeRequest = maxPayloadSizeRequest;
	}

	public void setMinResponseTime(long minResponseTime) {
		this.minResponseTime = minResponseTime;
	}

	public void setMaxResponseTime(long maxResponseTime) {
		this.maxResponseTime = maxResponseTime;
	}

	public void setFailRate(double failRate) {
		this.failRate = failRate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setTimeMinutes(long timeMinutes) {
		this.timeMinutes = timeMinutes;
	}

}