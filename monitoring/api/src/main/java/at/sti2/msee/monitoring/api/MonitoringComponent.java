package at.sti2.msee.monitoring.api;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailability;
import at.sti2.msee.monitoring.api.availability.MonitoringWebserviceAvailabilityState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.api.exception.MonitoringNoDataStoredException;
import at.sti2.msee.monitoring.api.qos.QoSParameter;
import at.sti2.msee.monitoring.api.qos.QoSType;

public interface MonitoringComponent {

	/**
	 * Return whether a webservice is currently monitored
	 * 
	 * @return
	 */
	public boolean isMonitoredWebService(URL WebService)
			throws MonitoringException;

	/**
	 * Returns an invocation instance generated for a web service.
	 * 
	 * @return
	 */
	public MonitoringInvocationInstance createInvocationInstance(URL WebService)
			throws MonitoringException;

	
	/**
	 * 
	 * Returns an invocation instance accordin to its id
	 * 
	 * @param UUID
	 * @return
	 * @throws MonitoringException
	 */
	public MonitoringInvocationInstance getInvocationInstance(String UUID)
			throws MonitoringException;

	
	
	/**
	 * Stores the data of the instance in the db.
	 * 
	 * @param instance
	 * @throws MonitoringException
	 */
	void updateInvocationInstance(MonitoringInvocationInstance instance)
			throws MonitoringException;

	
	
	public void enableMonitoring(URL WebService) throws MonitoringException;

	public void disableMonitoring(URL WebService) throws MonitoringException;

	public MonitoringWebserviceAvailability getAvailability(URL webService)
			throws MonitoringException;

	public void clearAllContentOfWebservice(URL webService)
			throws MonitoringException;

	public void addSuccessfulInvocationData(URL webService,
			double payloadSizeResponse, double payloadSizeRequest,
			double responseTime) throws MonitoringException;

	public void addUnsuccessfullInvocationData(URL webService)
			throws MonitoringException;

	public QoSParameter getQoSParameter(URL webService, QoSType qostype)
			throws MonitoringException, MonitoringNoDataStoredException;
	
	public List<QoSParameter> getQoSParametersInTimeframe(URL webService, QoSType qostype, Date start, Date end)
			throws MonitoringException, MonitoringNoDataStoredException;
	
	public List<MonitoringWebserviceAvailabilityState> getAvailabilityStatesInTimeframe(URL webService, Date start, Date end)
			throws MonitoringException, MonitoringNoDataStoredException;

	public List<MonitoringInvocationInstance> getInvocationInstancesInTimeframe(URL webService, Date start, Date end)
			throws MonitoringException, MonitoringNoDataStoredException;
	
	void updateAvailabilityState(URL WebService,
			MonitoringWebserviceAvailabilityState state) throws MonitoringException;
	

}
