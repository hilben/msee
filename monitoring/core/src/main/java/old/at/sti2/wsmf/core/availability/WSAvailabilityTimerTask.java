package old.at.sti2.wsmf.core.availability;
///**
// * Copyright (C) 2011 STI Innsbruck, UIBK
// * 
// * This library is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public
// * License as published by the Free Software Foundation; either
// * version 3 of the License, or (at your option) any later version.
// *
// * This library is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// * Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public
// * License along with this library. If not, see <http://www.gnu.org/licenses/>.
// */
//package at.sti2.wsmf.core.availability;
//
//import java.io.ByteArrayInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.TimerTask;
//
//import javax.xml.rpc.ServiceException;
//
//import org.apache.axis.AxisFault;
//import org.apache.axis.client.Call;
//import org.apache.axis.client.Service;
//import org.apache.axis.message.SOAPEnvelope;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.openrdf.query.MalformedQueryException;
//import org.openrdf.query.QueryEvaluationException;
//import org.openrdf.repository.RepositoryException;
//import org.openrdf.rio.RDFParseException;
//import org.xml.sax.SAXException;
//
//import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
//import at.sti2.wsmf.core.data.WebServiceEndpoint;
//import at.sti2.wsmf.core.data.channel.WSAvailabilityChannelHandler;
//
///**
// * @author Alex Oberhauser
// */
///**
// * @author Benjamin Hiltpolt
// * 
// */
//public class WSAvailabilityTimerTask extends TimerTask {
//	private final Logger LOGGER = LogManager.getLogger(this.getClass()
//			.getName());
//	
//	private WebServiceEndpoint webserviceendpoint;
//
//	/*
//	 * Update time for logging / monitoring repository issues
//	 */
//	private long updateTimeMinutes = 0;
//
//	public WSAvailabilityTimerTask(WebServiceEndpoint webserviceendpoint,
//			long updateTimeMinuts) {
//		this.webserviceendpoint = webserviceendpoint;
//		this.updateTimeMinutes = updateTimeMinuts;
//	}
//
//	private WSAvailabilityState updateAvailabilityState(
//			WebServiceEndpoint _webservice, long updateTimeMinutes)
//			throws RepositoryException, RDFParseException,
//			FileNotFoundException, IOException {
//
//		this.updateTimeMinutes = updateTimeMinutes;
//
//		WSAvailabilityState oldstate = _webservice.getAvailabilityStatus();
//		WSAvailabilityState state = WSAvailabilityState.WSUnavailable;
//
//		URL endpoint = _webservice.getEndpoint();
//
//		if (MonitoringAvailabilityChecker.isWebServiceAvailable(
//				endpoint.toExternalForm(), null)) {
//			state = WSAvailabilityState.WSAvailable;
//		} else {
//			state = WSAvailabilityState.WSUnavailable;
//		}
//		LOGGER.info("Availability of '" + endpoint + "' = " + state);
//
//		_webservice.changeAvailabilityStatus(state, this.updateTimeMinutes);
//
//		if (oldstate != state) {
//			try {
//				WSAvailabilityChannelHandler.getInstance().sendState(
//						_webservice.getEndpoint().toExternalForm(), state);
//			} catch (QueryEvaluationException e) {
//				LOGGER.error("Not able to send availability changed message, through exception: "
//						+ e.getLocalizedMessage());
//			} catch (MalformedQueryException e) {
//				LOGGER.error("Not able to send availability changed message, through exception: "
//						+ e.getLocalizedMessage());
//			}
//		}
//		return state;
//	}
//	
//	public String invoke(String serviceID,
//			String operationName, String inputData)   {
//
//		String results="";
//		String endpoint = serviceID;
//		String soapFile = inputData;
//		
//		Service service = new Service();
//		Call call;
//		
//		LOGGER.debug("Invoking " + serviceID);
//		LOGGER.debug("Using SOAP-Message " + soapFile);
//		LOGGER.debug("Operation: " + operationName);
//				
//		try {
//			
//		call = (Call) service.createCall();
//		call.setTargetEndpointAddress(new java.net.URL(endpoint));
//		
//		
//		ByteArrayInputStream soapMessageStream = new ByteArrayInputStream(soapFile.getBytes("UTF-8"));
//		
//		SOAPEnvelope env = new SOAPEnvelope(soapMessageStream);
//
//		results = call.invoke(env).toString();
//		LOGGER.debug("Obtainer results" + results);
//		
//		
//		} catch (ServiceException | MalformedURLException | SAXException | AxisFault | UnsupportedEncodingException e) {
//			LOGGER.error(e);
//			throw new ServiceInvokerException(e);
//		}
//		
//		return results;
//	}
//
//	/**
//	 * @see java.util.TimerTask#run()
//	 */
//	@Override
//	public void run() {
//		LOGGER.info("Starting availability checker...");
//		try {
//
//			WebServiceEndpoint currentActiveWS = WebServiceEndpointConfig.getConfig(
//					this.webserviceendpoint.getEndpoint()).getWebServiceEndpoint();
//
//			this.updateAvailabilityState(currentActiveWS,
//					this.updateTimeMinutes);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//		} catch (RDFParseException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
