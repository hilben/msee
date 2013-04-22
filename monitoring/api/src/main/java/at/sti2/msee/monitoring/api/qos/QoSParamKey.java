package at.sti2.msee.monitoring.api.qos;

/**
 * @author Benjamin Hiltpolt
 *
 * 
 *
 * This enum represents all possible QoS Parameters (should be used for monitoring webservice)
 */
public enum QoSParamKey {
	
	RequestTotal,
	RequestSuccessful,   
	RequestFailed,       
	
	MonitoredTime,
	AvailableTime,  
	UnavailableTime,    

	PayloadSizeResponse,    
	PayloadSizeResponseMinimum,
	PayloadSizeResponseAverage,
	PayloadSizeResponseMaximum,
	PayloadSizeResponseTotal,
	
	PayloadSizeRequest,
	PayloadSizeRequestMinimum,
	PayloadSizeRequestAverage,
	PayloadSizeRequestMaximum,
	PayloadSizeRequestTotal,
	
	ResponseTime,
	ResponseTimeMinimum,
	ResponseTimeMaximum,
	ResponseTimeAverage,
	ResponseTimeTotal,
}