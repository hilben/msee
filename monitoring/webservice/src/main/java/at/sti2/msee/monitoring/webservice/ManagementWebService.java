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
//package at.sti2.msee.monitoring.webservice;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Vector;
//
//import javax.jws.WebMethod;
//import javax.jws.WebParam;
//import javax.jws.WebService;
//
//import at.sti2.wsmf.api.data.qos.QoSParamKey;
//import at.sti2.wsmf.api.data.state.WSInvocationState;
//import at.sti2.wsmf.api.ws.IManagementWebService;
//import at.sti2.wsmf.core.PersistentHandler;
//
///**
// * @author Alex Oberhauser
// * 
// * @author Benjamin Hiltpolt
// */
//@WebService
//public class ManagementWebService implements IManagementWebService {
//
//	/**
//	 * @see at.sti2.wsmf.api.ws.IManagementWebService#listInstanceIDs()
//	 */
//	@WebMethod
//	@Override
//	public String[] listInstanceIDs() throws Exception {
//		PersistentHandler persHandler = PersistentHandler.getInstance();
//		Vector<String> resultVector = persHandler.getInstanceIDs();
//		return resultVector.toArray(new String[resultVector.size()]);
//	}
//
//	/**
//	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getInvocationState(java.lang.String)
//	 */
//	@WebMethod
//	@Override
//	public WSInvocationState getInvocationState(
//			@WebParam(name = "activityStartedEvent") String _activityStartedEvent)
//			throws Exception {
//		PersistentHandler persHandler = PersistentHandler.getInstance();
//		return persHandler.getInvocationState(_activityStartedEvent);
//	}
//
//	/**
//	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getQoSParam(java.lang.String)
//	 */
//	@WebMethod
//	@Override
//	public double getQoSParamValue(@WebParam(name = "endpoint") URL _endpoint,
//			@WebParam(name = "key") QoSParamKey _key) throws Exception {
//		PersistentHandler persHandler = PersistentHandler.getInstance();
//		return persHandler.getQoSParamValue(_endpoint, _key);
//	}
//
//	/**
//	 * @see at.sti2.wsmf.api.ws.IManagementWebService#changeQoSThresholdValue(java.net.URL,
//	 *      at.sti2.wsmf.api.data.qos.IQoSThresholdValue)
//	 */
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see at.sti2.wsmf.api.ws.IManagementWebService#listEndpoints()
//	 */
//	@Override
//	public String[] listEndpoints() throws Exception {
//		PersistentHandler persHandler = PersistentHandler.getInstance();
//
//		List<String> eps = persHandler.getEndpoints();
//
//		String[] ret = new String[eps.size()];
//
//		for (int i = 0; i < ret.length; i++) {
//			ret[i] = eps.get(i);
//		}
//
//		return ret;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * at.sti2.wsmf.api.ws.IManagementWebService#getSubcategoriesAndServices
//	 * (java.lang.String)
//	 */
//
//	@Override
//	public List<String> getSubcategoriesAndServices(String category) throws Exception {
//		String[] allendpoints = this.listEndpoints();
//
//		List<String> endpoints = new ArrayList<String>();
//
//		if (category.compareTo("root") == 0) {
//			endpoints.add("DeliveryService");
//			endpoints.add("TShirtCreationService");
//			return endpoints;
//		}
//		
//		if (category.compareTo("DeliveryService") == 0)
//		{
//			endpoints.add("FastDeliveryService");
//			for (int i = 0; i < allendpoints.length; i++) {
//
//				if (i % 3 == 0)
//				{
//					if (allendpoints[i].charAt(0) == '<') {
//						allendpoints[i] = allendpoints[i].substring(1,allendpoints[i].length() - 1);
//					}
//					endpoints.add(allendpoints[i]);
//				}
//			}
//			return endpoints;
//		}
//		
//		if (category.compareTo("FastDeliveryService") == 0)
//		{
//			for (int i = 0; i < allendpoints.length; i++) {
//
//				if (i % 3 == 1)
//				{
//					if (allendpoints[i].charAt(0) == '<') {
//						allendpoints[i] = allendpoints[i].substring(1,allendpoints[i].length() - 1);
//					}
//					endpoints.add(allendpoints[i]);
//				}
//			}
//			return endpoints;
//		}
//
//		if (category.compareTo("TShirtCreationService") == 0)
//		{
//			for (int i = 0; i < allendpoints.length; i++) {
//
//				if (i % 3 == 2)
//				{
//					if (allendpoints[i].charAt(0) == '<') {
//						allendpoints[i] = allendpoints[i].substring(1,allendpoints[i].length() - 1);
//					}
//					endpoints.add(allendpoints[i]);
//				}
//			}
//			return endpoints;
//		}
//		return endpoints;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see at.sti2.wsmf.api.ws.IManagementWebService#getQoSParamKeys()
//	 */
//	@Override
//	public List<String> getQoSParamKeys() throws Exception {
//		List<String> keys = new ArrayList<String>();
//		for (QoSParamKey k : QoSParamKey.values()) {
//			keys.add(k.toString());
//		}
//
//		return keys;
//	}
//
//}
