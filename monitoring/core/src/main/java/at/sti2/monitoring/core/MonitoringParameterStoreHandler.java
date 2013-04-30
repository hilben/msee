package at.sti2.monitoring.core;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;

import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStored;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;

//TODO: move to core packages
public class MonitoringParameterStoreHandler {

	private MonitoringComponent monitoringComponent;
	private MonitoringRepositoryHandler repositoryHandler;

	private final Logger LOGGER = LogManager.getLogger(this.getClass()
			.getName());

	public MonitoringParameterStoreHandler(
			MonitoringComponent monitoringComponent)
			throws RepositoryException, IOException {
		this.monitoringComponent = monitoringComponent;

		this.repositoryHandler = MonitoringRepositoryHandler
				.getMonitoringRepositoryHandler();
	}


	public void addUnsuccessfulInvocation(URL webService) throws RepositoryException, MalformedQueryException, UpdateExecutionException, MonitoringException, ParseException, IOException {
		//increase successful requests
		QoSParameter requestSuccessful = new QoSParameter(
				QoSType.RequestFailed, "1", new Date());
		this.addTotalParameterValue(webService, requestSuccessful);
		
		//increase total requests
		QoSParameter requestTotal = new QoSParameter(
				QoSType.RequestTotal, "1", new Date());
		this.addTotalParameterValue(webService, requestTotal);
	}

	
	public void addSuccessfulInvocation(URL webService,
			double payloadSizeResponse, double payloadSizeRequest,
			double responseTime) throws RepositoryException, MalformedQueryException, UpdateExecutionException, IOException, MonitoringException, ParseException {

		//increase successful requests
		QoSParameter requestSuccessful = new QoSParameter(
				QoSType.RequestSuccessful, "1", new Date());
		this.addTotalParameterValue(webService, requestSuccessful);
		
		//increase total requests
		QoSParameter requestTotal = new QoSParameter(
				QoSType.RequestTotal, "1", new Date());
		this.addTotalParameterValue(webService, requestTotal);
		
		//PayloadSizeRequest
		QoSParameter playoadSizeRequestParameter = new QoSParameter(
				QoSType.PayloadSizeRequest, String.valueOf(payloadSizeRequest),
				new Date());
		this.repositoryHandler.addQoSParameter(webService, UUID.randomUUID().toString(),
				playoadSizeRequestParameter);
		
		playoadSizeRequestParameter.setType(QoSType.PayloadSizeRequestAverage);
		this.addAverageParameterValue(webService, playoadSizeRequestParameter);
		
		playoadSizeRequestParameter.setType(QoSType.PayloadSizeRequestTotal);
		this.addTotalParameterValue(webService, playoadSizeRequestParameter);
		
		playoadSizeRequestParameter.setType(QoSType.PayloadSizeRequestMinimum);
		this.addMinimumParameterValue(webService, playoadSizeRequestParameter);
		
		playoadSizeRequestParameter.setType(QoSType.PayloadSizeRequestMaximum);
		this.addMaximumParameterValue(webService, playoadSizeRequestParameter);
		
		//PayloadSizeResponse
		QoSParameter playloadSizeResponseParameter = new QoSParameter(
				QoSType.PayloadSizeResponse,
				String.valueOf(payloadSizeResponse), new Date());
		this.repositoryHandler.addQoSParameter(webService,UUID.randomUUID().toString(),
				playloadSizeResponseParameter);
		
		playloadSizeResponseParameter.setType(QoSType.PayloadSizeResponseAverage);
		this.addAverageParameterValue(webService, playloadSizeResponseParameter);
		
		playloadSizeResponseParameter.setType(QoSType.PayloadSizeResponseTotal);
		this.addTotalParameterValue(webService, playloadSizeResponseParameter);
		
		playloadSizeResponseParameter.setType(QoSType.PayloadSizeResponseMinimum);
		this.addMinimumParameterValue(webService, playloadSizeResponseParameter);
		
		playloadSizeResponseParameter.setType(QoSType.PayloadSizeResponseMaximum);
		this.addMaximumParameterValue(webService, playloadSizeResponseParameter);
		
		//ResponseTime
		QoSParameter responseTimeParameter = new QoSParameter(
				QoSType.ResponseTime, String.valueOf(responseTime), new Date());
		this.repositoryHandler.addQoSParameter(webService, UUID.randomUUID().toString(),
				responseTimeParameter);
		
		responseTimeParameter.setType(QoSType.ResponseTimeAverage);
		this.addAverageParameterValue(webService, responseTimeParameter);
		
		responseTimeParameter.setType(QoSType.ResponseTimeTotal);
		this.addTotalParameterValue(webService, responseTimeParameter);
		
		responseTimeParameter.setType(QoSType.ResponseTimeMinimum);
		this.addMinimumParameterValue(webService, responseTimeParameter);
		
		responseTimeParameter.setType(QoSType.ResponseTimeMaximum);
		this.addMaximumParameterValue(webService, responseTimeParameter);
	}

	private void addAverageParameterValue(URL webservice, QoSParameter parameter)
			throws MonitoringException, RepositoryException,
			MalformedQueryException, UpdateExecutionException, IOException,
			ParseException {

		String oldAverageValueStr = "";
		try {
			oldAverageValueStr = this.monitoringComponent.getQoSParameter(
					webservice, parameter.getType()).getValue();
		} catch (MonitoringNoDataStored e) {
			oldAverageValueStr = "0.0";
		}
		double oldAverageValue = Double.parseDouble(oldAverageValueStr);

		String totalSuccessfullRequestsStr;
		try {
			totalSuccessfullRequestsStr = this.monitoringComponent
					.getQoSParameter(webservice, QoSType.RequestSuccessful)
					.getValue();
		} catch (MonitoringNoDataStored e) {
			totalSuccessfullRequestsStr = "0";
		}
		double totalSuccessfullRequests = Double
				.parseDouble(totalSuccessfullRequestsStr);

		double newCurrentValue = Double.parseDouble(parameter.getValue());

		double newAverageValue = ((oldAverageValue
				* (totalSuccessfullRequests - 1) + newCurrentValue) / totalSuccessfullRequests);

		QoSParameter newAverageParameter = new QoSParameter(
				parameter.getType(), String.valueOf(newAverageValue),
				parameter.getTime());
		String id = UUID.randomUUID().toString();

		this.repositoryHandler.addQoSParameter(webservice, id,
				newAverageParameter);
	}

	private void addTotalParameterValue(URL webservice, QoSParameter parameter)
			throws MonitoringException, ParseException, RepositoryException,
			MalformedQueryException, UpdateExecutionException, IOException {

		String oldTotalValueStr = "";
		try {
			oldTotalValueStr = this.monitoringComponent.getQoSParameter(
					webservice, parameter.getType()).getValue();
		} catch (MonitoringNoDataStored e) {
			oldTotalValueStr = "0.0";
		}

		double oldTotalValue = Double.parseDouble(oldTotalValueStr);

		double newValue = Double.parseDouble(parameter.getValue());

		double newTotalValue = oldTotalValue + newValue;

		QoSParameter newTotalParameter = new QoSParameter(parameter.getType(),
				String.valueOf(newTotalValue), parameter.getTime());
		String id = UUID.randomUUID().toString();

		this.repositoryHandler.addQoSParameter(webservice, id,
				newTotalParameter);

	}

	private void addMinimumParameterValue(URL webservice, QoSParameter parameter)
			throws MonitoringException, ParseException, RepositoryException,
			MalformedQueryException, UpdateExecutionException, IOException {
		String oldMinimumValueStr = "";
		double oldMinimumValue = 0;
		try {
			oldMinimumValueStr = this.monitoringComponent.getQoSParameter(
					webservice, parameter.getType()).getValue();
			oldMinimumValue = Double.parseDouble(oldMinimumValueStr);
		} catch (MonitoringNoDataStored e) {
			oldMinimumValue = Double.POSITIVE_INFINITY;
		}

		double newValue = Double.parseDouble(parameter.getValue());

		QoSParameter newMinimumParameter = null;
		if (newValue < oldMinimumValue) {
			newMinimumParameter = new QoSParameter(parameter.getType(),
					String.valueOf(newValue), parameter.getTime());
		} else {
			newMinimumParameter = new QoSParameter(parameter.getType(),
					String.valueOf(oldMinimumValue), parameter.getTime());

		}

		String id = UUID.randomUUID().toString();

		this.repositoryHandler.addQoSParameter(webservice, id,
				newMinimumParameter);
	}

	private void addMaximumParameterValue(URL webservice, QoSParameter parameter)
			throws MonitoringException, ParseException, RepositoryException,
			MalformedQueryException, UpdateExecutionException, IOException {

		String oldMaximumValueStr = "";
		double oldMaximumValue = 0;
		try {
			oldMaximumValueStr = this.monitoringComponent.getQoSParameter(
					webservice, parameter.getType()).getValue();
			oldMaximumValue = Double.parseDouble(oldMaximumValueStr);
		} catch (MonitoringNoDataStored e) {
			oldMaximumValue = Double.NEGATIVE_INFINITY;
		}

		double newValue = Double.parseDouble(parameter.getValue());

		QoSParameter newMaximumParameter = null;
		if (newValue > oldMaximumValue) {
			newMaximumParameter = new QoSParameter(parameter.getType(),
					String.valueOf(newValue), parameter.getTime());
		} else {
			newMaximumParameter = new QoSParameter(parameter.getType(),
					String.valueOf(oldMaximumValue), parameter.getTime());

		}

		String id = UUID.randomUUID().toString();

		this.repositoryHandler.addQoSParameter(webservice, id,
				newMaximumParameter);
	}

}
