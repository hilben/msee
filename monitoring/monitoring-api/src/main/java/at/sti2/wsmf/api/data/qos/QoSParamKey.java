/**
 * Copyright (C) 2011 STI Innsbruck, UIBK
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.wsmf.api.data.qos;

/**
 * @author Benjamin Hiltpolt
 *
 * 
 *
 * This enum represents all possible QoS Parameters (should be used for monitoring webservice)
 */
public enum QoSParamKey {
	
	
	
	RequestTotal,
	RequestSuccessful,   //addSuccessful Request (PayloadSize Request, PayloadSize Response, ResponseTime)
	RequestFailed,       //addUnsuccesful Request (PayloadSize Request)
	
	MonitoredTime,
	AvailableTime,   //unavailable (Time)
	UnavailableTime,     //available (Time)

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
