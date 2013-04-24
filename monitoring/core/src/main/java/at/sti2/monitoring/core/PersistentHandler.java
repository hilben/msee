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
//package at.sti2.monitoring.core;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//import java.util.Vector;
//
//import org.apache.log4j.Logger;
//import org.openrdf.query.Binding;
//import org.openrdf.query.BindingSet;
//import org.openrdf.query.MalformedQueryException;
//import org.openrdf.query.QueryEvaluationException;
//import org.openrdf.query.TupleQueryResult;
//import org.openrdf.repository.RepositoryException;
//
//import at.sti2.monitoring.core.data.channel.ChannelSubscriber;
//import at.sti2.monitoring.core.data.qos.QoSParamAtTime;
//import at.sti2.monitoring.core.data.qos.QoSParamValue;
//import at.sti2.util.triplestore.QueryHelper;
//import at.sti2.util.triplestore.RepositoryHandler;
//import at.sti2.wsmf.api.data.qos.QoSParamKey;
//import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
//import at.sti2.wsmf.api.data.qos.QoSUnit;
//import at.sti2.wsmf.api.data.state.WSAvailabilityState;
//import at.sti2.wsmf.api.data.state.WSInvocationState;
//import at.sti2.wsmf.core.common.DateHelper;
//import at.sti2.wsmf.core.common.HashValueHandler;
//import at.sti2.wsmf.core.common.MonitoringConfig;
//import at.sti2.wsmf.core.common.WebServiceEndpointConfig;
//
//
//
///**
// * @author Alex Oberhauser
// * 
// * @author Benjamin Hiltpolt
// * 
// * 
// *         The {@link PersistentHandler} handled several operations to query the
// *         web service monitoring repository
// */
//public class PersistentHandler {
//	private static Logger log = Logger.getLogger(PersistentHandler.class);
//
//	private static PersistentHandler instance = null;
//	private String triplestoreEndpoint;
//	private String repositoryID;
//	private RepositoryHandler reposHandler;
//
//	public static PersistentHandler getInstance() throws FileNotFoundException,
//			IOException {
//		if (null == instance) {
//			instance = new PersistentHandler();
//		}
//		return instance;
//	}
//
//	private PersistentHandler() throws FileNotFoundException, IOException {
//		MonitoringConfig cfg = MonitoringConfig.getConfig();
//		this.triplestoreEndpoint = cfg.getTriplestoreEndpoint();
//		this.repositoryID = cfg.getTriplestoreReposID();
//
//		this.reposHandler = new RepositoryHandler(this.triplestoreEndpoint,
//				this.repositoryID, false);
//	}
//
//	public synchronized WSAvailabilityState getWSAvailabilityState(
//			String _subject) throws QueryEvaluationException,
//			RepositoryException, MalformedQueryException {
//		StringBuffer selectSPARQL = new StringBuffer();
//		selectSPARQL.append("SELECT ?availabilityState WHERE { ");
//		selectSPARQL.append("  <" + _subject + "> rdf:type wsmf:Endpoint . ");
//		selectSPARQL.append("  <" + _subject
//				+ "> wsmf:availabilityState ?availabilityState . ");
//		selectSPARQL.append("}");
//
//		WSAvailabilityState state = WSAvailabilityState.WSNotChecked;
//		TupleQueryResult queryResult = this.reposHandler
//				.selectSPARQL(QueryHelper.getNamespacePrefix()
//						+ selectSPARQL.toString());
//		if (queryResult.hasNext()) {
//			BindingSet entry = queryResult.next();
//			String stateString = entry.getBinding("availabilityState")
//					.getValue().stringValue().replace(QueryHelper.WSMF_NS, "");
//			state = WSAvailabilityState.valueOf(stateString);
//		}
//		return state;
//	}
//
//	private synchronized void updateResourceTriple(String _subject,
//			String _predicate, String _object, String _context)
//			throws RepositoryException {
//		this.reposHandler.updateResourceTriple(_subject, _predicate, _object,
//				_context);
//	}
//
//	private synchronized void updateLiteralTriple(String _subject,
//			String _predicate, String _object, String _context)
//			throws RepositoryException {
//		this.reposHandler.updateLiteralTriple(_subject, _predicate, _object,
//				_context);
//	}
//
//	private synchronized void addResourceTriple(String _subject,
//			String _predicate, String _object, String _context)
//			throws RepositoryException {
//		this.reposHandler.addResourceTriple(_subject, _predicate, _object,
//				_context);
//	}
//
//	public synchronized String getQoSValue(String endpoint, String _key)
//			throws QueryEvaluationException, RepositoryException,
//			MalformedQueryException {
//		String type = QueryHelper.WSMF_NS + _key;
//		StringBuffer selectSPARQL = new StringBuffer();
//		selectSPARQL.append("SELECT ?qosValue WHERE { ");
//		selectSPARQL.append("  <" + endpoint + "> rdf:type wsmf:Endpoint . ");
//		selectSPARQL.append("  <" + endpoint
//				+ "> wsmf:hasCurrentQoSParam ?qos . ");
//		selectSPARQL.append("  ?qos wsmf:type <" + type + "> . ");
//		selectSPARQL.append("  ?qos wsmf:value ?qosValue . ");
//		selectSPARQL.append("}");
//		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper
//				.getNamespacePrefix() + selectSPARQL.toString());
//		if (result.hasNext())
//			return result.next().getBinding("qosValue").getValue()
//					.stringValue();
//		else
//			return "0";
//	}
//
//
//	/**
//	 * Returns a Vector of all Endpoint monitored by the Repository TODO:
//	 * implement
//	 * 
//	 * @throws MalformedQueryException
//	 * @throws RepositoryException
//	 * @throws QueryEvaluationException
//	 */
//	public List<String> getEndpoints() throws QueryEvaluationException,
//			RepositoryException, MalformedQueryException {
//		StringBuffer selectSPARQL = new StringBuffer();
//		selectSPARQL.append("SELECT distinct ?endpoints WHERE { ");
//		selectSPARQL
//				.append("  ?endpoints a <http://www.sti2.at/wsmf/ns#Endpoint> . ");
//		selectSPARQL.append("}");
//		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper
//				.getNamespacePrefix() + selectSPARQL.toString());
//		Vector<String> resultVector = new Vector<String>();
//
//		while (result.hasNext()) {
//			resultVector.add(result.next().getBinding("endpoints").getValue()
//					.stringValue().replace(QueryHelper.WSMF_NS, ""));
//		}
//
//		return resultVector;
//	}
//
//	/**
//	 * @return
//	 * @throws MalformedQueryException
//	 * @throws RepositoryException
//	 * @throws QueryEvaluationException
//	 */
//	public Vector<String> getInstanceIDs() throws QueryEvaluationException,
//			RepositoryException, MalformedQueryException {
//		StringBuffer selectSPARQL = new StringBuffer();
//		selectSPARQL.append("SELECT ?instances WHERE { ");
//		selectSPARQL.append("  ?instances rdf:type wsmf:InvocationInstance . ");
//		selectSPARQL.append("}");
//		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper
//				.getNamespacePrefix() + selectSPARQL.toString());
//		Vector<String> resultVector = new Vector<String>();
//
//		while (result.hasNext()) {
//			resultVector.add(result.next().getBinding("instances").getValue()
//					.stringValue());
//		}
//
//		return resultVector;
//	}
//
//	/**
//	 * 
//	 * TODO: not working anymore! FIX!
//	 * 
//	 * @param _activityStartedEvent
//	 * @return
//	 * @throws MalformedQueryException
//	 * @throws RepositoryException
//	 * @throws QueryEvaluationException
//	 */
//	public synchronized WSInvocationState getInvocationState(
//			String _activityStartedEvent) throws QueryEvaluationException,
//			RepositoryException, MalformedQueryException {
//		StringBuffer selectSPARQL = new StringBuffer();
//		selectSPARQL.append("SELECT ?invocationState WHERE { ");
//		selectSPARQL.append("  <" + _activityStartedEvent
//				+ "> rdf:type wsmf:InvocationInstance . ");
//		selectSPARQL.append("  <" + _activityStartedEvent
//				+ "> wsmf:state ?invocationState . ");
//		selectSPARQL.append("}");
//		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper
//				.getNamespacePrefix() + selectSPARQL.toString());
//
//		WSInvocationState state = null;
//		if (result.hasNext()) {
//			String stateString = result.next().getBinding("invocationState")
//					.getValue().stringValue().replace(QueryHelper.WSMF_NS, "");
//			state = WSInvocationState.valueOf(stateString);
//		}
//		return state;
//	}
//
//	public synchronized void deleteEndpoint(URL _webserviceURL)
//			throws RepositoryException {
//		this.reposHandler.deleteContext(_webserviceURL.toExternalForm());
//		this.reposHandler.commit();
//		this.reposHandler.shutdown();
//	}
//
//	public synchronized void deleteContext(String _contextURI)
//			throws RepositoryException {
//		this.reposHandler.deleteContext(_contextURI);
//		this.reposHandler.commit();
//		this.reposHandler.shutdown();
//	}
//
//	/**
//	 * TODO: implement begin and end date
//	 * 
//	 * @param endpoint
//	 * @param qostype
//	 * @param begin
//	 * @param end
//	 * @return
//	 * @throws RepositoryException
//	 * @throws MalformedQueryException
//	 * @throws QueryEvaluationException
//	 */
//	public List<QoSParamAtTime> getQoSTimeframe(URL endpoint,
//			QoSParamKey qostype, Date begin, Date end) throws Exception {
//		System.out.println(endpoint + " is searched...");
//
//		List<QoSParamAtTime> returnList = new ArrayList<QoSParamAtTime>();
//
//		StringBuffer selectSPARQL = new StringBuffer();
//
//		selectSPARQL.append("SELECT ?qosparamval ?time ");
//		selectSPARQL.append("WHERE {");
//		selectSPARQL.append("<" + endpoint
//				+ "> <http://www.sti2.at/wsmf/ns#hasQoSParam> ?qosparam . ");
//		selectSPARQL
//				.append("?qosparam <http://www.sti2.at/wsmf/ns#type> <http://www.sti2.at/wsmf/ns#"
//						+ qostype + "> . ");
//		selectSPARQL
//				.append("?qosparam <http://purl.org/dc/elements/1.1/date> ?time . ");
//		selectSPARQL
//				.append("?qosparam <http://www.sti2.at/wsmf/ns#value> ?qosparamval . ");
//		selectSPARQL.append("} ");
//
//		System.out.println(selectSPARQL.toString());
//
//		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper
//				.getNamespacePrefix()
//				+ "PREFIX :<http://www.sti2.at/wsmf/ns#>"
//				+ selectSPARQL.toString());
//
//		while (result.hasNext()) {
//
//			BindingSet next = result.next();
//			String qos = next.getBinding("qosparamval").getValue().toString();
//			String time = next.getBinding("time").getValue().toString();
//
//			// cut off "
//			qos = qos.substring(1, qos.length() - 1);
//			time = time.substring(1, time.length() - 1);
//
//			returnList.add(new QoSParamAtTime(qos, time));
//		}
//
//		return returnList;
//	}
//
//	public synchronized void commit() {
//		try {
//			this.reposHandler.commit();
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * TODO: testing
//	 * 
//	 * @param _endpoint
//	 * @param key
//	 * @return
//	 * @throws Exception
//	 */
//	public double getQoSParamValue(URL endpoint, QoSParamKey key) {
//
//		StringBuffer selectSPARQL = new StringBuffer();
//
//		selectSPARQL.append("SELECT ?v ");
//		selectSPARQL.append("WHERE {");
//		selectSPARQL
//				.append("<"
//						+ endpoint
//						+ "> <http://www.sti2.at/wsmf/ns#hasCurrentQoSParam> ?qosparam . ");
//		selectSPARQL
//				.append("?qosparam <http://www.sti2.at/wsmf/ns#value> ?v .");
//		selectSPARQL
//				.append("?qosparam <http://www.sti2.at/wsmf/ns#type> <http://www.sti2.at/wsmf/ns#"
//						+ key.name() + "> .");
//		selectSPARQL.append("} ");
//
//		// System.out.println(selectSPARQL.toString());
//
//		TupleQueryResult result = null;
//		try {
//			result = this.reposHandler.selectSPARQL(QueryHelper
//					.getNamespacePrefix()
//					+ "PREFIX :<http://www.sti2.at/wsmf/ns#>"
//					+ selectSPARQL.toString());
//		} catch (QueryEvaluationException e) {
//			e.printStackTrace();
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//		} catch (MalformedQueryException e) {
//			e.printStackTrace();
//		}
//
//		double ret = Double.NaN;
//		try {
//			if (result != null) {
//
//				if (result.hasNext()) {
//					ret = Double.parseDouble(result.next().getBinding("v")
//							.getValue().stringValue());
//				}
//			}
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (QueryEvaluationException e) {
//			e.printStackTrace();
//		}
//
//		return ret;
//	}
//
//	/**
//	 * @param _endpoint
//	 * @param payloadsizerequestmaximum
//	 * @return
//	 */
//	public QoSParamValue getThresholdValue(URL _endpoint,
//			QoSThresholdKey payloadsizerequestmaximum) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/**
//	 * @return
//	 * @throws IOException
//	 * @throws RepositoryException
//	 */
//	public String createInvocationInstance(String endpoint)
//			throws RepositoryException, IOException {
//		WebServiceEndpointConfig cfg = WebServiceEndpointConfig
//				.getConfig(endpoint);
//
//		String identifier = UUID.randomUUID().toString();
//		String subject = cfg.getInstancePrefix() + identifier;
//
//		this.updateResourceTriple(subject, QueryHelper.getRDFURI("type"),
//				QueryHelper.getWSMFURI("InvocationInstance"), subject);
//		this.commit();
//
//		return subject;
//	}
//
//	public synchronized void updateInvocationStatus(String subject,
//			String endpoint, WSInvocationState state)
//			throws RepositoryException, IOException {
//		WebServiceEndpointConfig cfg = WebServiceEndpointConfig
//				.getConfig(endpoint);
//
//		String invocationState = cfg.getInstancePrefix() + "invocationState_"
//				+ UUID.randomUUID().toString();
//		this.updateResourceTriple(invocationState,
//				QueryHelper.getWSMFURI("state"),
//				QueryHelper.getWSMFURI(state.name()), subject);
//
//		this.updateResourceTriple(invocationState,
//				QueryHelper.getRDFURI("type"),
//				QueryHelper.getWSMFURI("InvocationState"), subject);
//
//		this.updateLiteralTriple(invocationState, QueryHelper.getDCURI("date"),
//				DateHelper.getXSDDateTime(), subject);
//		this.addResourceTriple(subject,
//				QueryHelper.getWSMFURI("hasInvocationState"), invocationState,
//				subject);
//
//		this.updateResourceTriple(subject, QueryHelper.getWSMFURI("relatedTo"),
//				endpoint, subject);
//
//		this.commit();
//	}
//
//	/**
//	 * @param endpoint
//	 * @throws RepositoryException
//	 * @throws IOException
//	 */
//	public synchronized void createEndpoint(URL endpoint)
//			throws RepositoryException, IOException {
//		WebServiceEndpointConfig cfg = WebServiceEndpointConfig
//				.getConfig(endpoint);
//
//		String webserviceURI = cfg.getInstancePrefix()
//				+ cfg.getWebServiceName();
//
//		String subject = endpoint.toExternalForm();
//
//		this.updateResourceTriple(subject, QueryHelper.getRDFURI("type"),
//				QueryHelper.getWSMFURI("Endpoint"), subject);
//		this.updateResourceTriple(subject,
//				QueryHelper.getWSMFURI("isRelatedToWebService"), webserviceURI,
//				subject);
//
//		this.commit();
//	}
//
//	public synchronized void addAvailabilityStatus(URL endpoint,
//			WSAvailabilityState state, long updateTimeMinutes)
//			throws RepositoryException {
//		try {
//			String subject = endpoint.toExternalForm();
//
//			WebServiceEndpointConfig cfg = WebServiceEndpointConfig
//					.getConfig(endpoint);
//
//			String availabilityState = cfg.getInstancePrefix()
//					+ "availabilityState_" + UUID.randomUUID().toString();
//			this.updateResourceTriple(availabilityState,
//					QueryHelper.getWSMFURI("state"),
//					QueryHelper.getWSMFURI(availabilityState), subject);
//			this.updateResourceTriple(availabilityState,
//					QueryHelper.getRDFURI("type"),
//					QueryHelper.getWSMFURI("AvailabilityState"), subject);
//			this.updateLiteralTriple(availabilityState,
//					QueryHelper.getDCURI("datetime"),
//					DateHelper.getXSDDateTime(), subject);
//
//			/*
//			 * updateTime should be at least 1; (TODO: throw exception?)
//			 */
//			if (updateTimeMinutes > 0) {
//				this.updateLiteralTriple(availabilityState,
//						QueryHelper.getWSMFURI("nextAvailabilityCheckMinutes"),
//						String.valueOf(updateTimeMinutes), subject);
//
//				this.updateAvailabilityTime(endpoint, updateTimeMinutes, state);
//			}
//			this.addResourceTriple(subject,
//					QueryHelper.getWSMFURI("hasAvailabilityState"),
//					availabilityState, subject);
//
//			this.commit();
//		} catch (IOException e) {
//			log.error("Error loading the configuration file");
//		}
//	}
//
//	/**
//	 * 
//	 * Updates the available time of a end points
//	 * 
//	 * @param endpoint
//	 *            the endpoint
//	 * @param updateTimeMinutes
//	 *            the update time in minutes
//	 * @param _state
//	 *            the state of the endpoint ({@link WSAvailabilityState})
//	 */
//	private void updateAvailabilityTime(URL endpoint, long updateTimeMinutes,
//			WSAvailabilityState _state) {
//
//		long availableTime = updateTimeMinutes;
//		long unavailableTime = updateTimeMinutes;
//
//		// Update the monitored Time
//		QoSParamValue monitoredTime = new QoSParamValue(
//				QoSParamKey.MonitoredTime, updateTimeMinutes, QoSUnit.Minutes);
//
//		this.addQoSValue(endpoint, monitoredTime);
//		this.updateQoSValueTotal(endpoint, monitoredTime);
//
//		// If web service is availabe then update it's available time
//		if (_state == WSAvailabilityState.WSAvailable) {
//			QoSParamValue availTime = new QoSParamValue(
//					QoSParamKey.AvailableTime, availableTime, QoSUnit.Minutes);
//
//			this.addQoSValue(endpoint, availTime);
//			this.updateQoSValueTotal(endpoint, availTime);
//
//		} else {
//			// If web service is no available then update it's unavailable time
//			QoSParamValue unavailTime = new QoSParamValue(
//					QoSParamKey.UnavailableTime, unavailableTime,
//					QoSUnit.Minutes);
//
//			this.addQoSValue(endpoint, unavailTime);
//			this.updateQoSValueTotal(endpoint, unavailTime);
//		}
//	}
//
//	public void addQoSValue(URL endpoint, QoSParamValue value) {
//		this.addQoSValueAndUpdate(endpoint, value, true);
//	}
//
//	/**
//	 * TODO: test performUpdate Option
//	 * 
//	 * @param value
//	 */
//	private void addQoSValueAndUpdate(URL endpoint, QoSParamValue value,
//			boolean performUpdate) {
//		String endpointString = endpoint.toExternalForm();
//		String qosParamID;
//		try {
//
//			if (!performUpdate) {
//				qosParamID = endpointString + "_" + DateHelper.getXSDDateTime()
//						+ "_" + value.getType().name();
//
//				this.addResourceTriple(endpointString,
//						QueryHelper.getWSMFURI("hasQoSParam"), qosParamID,
//						endpointString);
//			} else {
//				qosParamID = endpointString + "_" + value.getType().name();
//
//				this.addResourceTriple(endpointString,
//						QueryHelper.getWSMFURI("hasCurrentQoSParam"),
//						qosParamID, endpointString);
//			}
//			this.updateResourceTriple(qosParamID,
//					QueryHelper.getRDFURI("type"),
//					QueryHelper.getWSMFURI("QoSParam"), endpointString);
//
//			this.updateResourceTriple(qosParamID,
//					QueryHelper.getWSMFURI("type"), QueryHelper.WSMF_NS
//							+ value.getType().name(), endpointString);
//
//			this.updateResourceTriple(qosParamID,
//					QueryHelper.getWSMFURI("unit"), QueryHelper.WSMF_NS
//							+ value.getUnit().name(), endpointString);
//
//			// TODO: add type double to triplestore ?
//			this.updateLiteralTriple(qosParamID,
//					QueryHelper.getWSMFURI("value"),
//					String.valueOf(value.getValue()), endpointString);
//
//			this.updateLiteralTriple(qosParamID, QueryHelper.getDCURI("date"),
//					DateHelper.getXSDDateTime(), endpointString);
//
//			if (performUpdate) {
//				addQoSValueAndUpdate(endpoint, value, false);
//			}
//
//		} catch (RepositoryException e) {
//			e.printStackTrace();
//			log.error(e.getLocalizedMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error(e.getLocalizedMessage());
//		}
//	}
//
//	// Assume total requests is already updated
//	public void updateQoSValueAverage(URL endpoint, QoSParamValue value) {
//		double totalRequests = this.getQoSParamValue(endpoint,
//				QoSParamKey.RequestTotal);
//
//		double oldAverage = this.getQoSParamValue(endpoint, value.getType());
//		double newAverage;
//
//		if (totalRequests > 1) {
//			newAverage = ((oldAverage * (totalRequests - 1) + value.getValue()) / totalRequests);
//		} else {
//			newAverage = value.getValue();
//		}
//
//		this.addQoSValue(endpoint, new QoSParamValue(value.getType(),
//				newAverage, value.getUnit()));
//	}
//
//	public boolean updateQoSValueMaximum(URL endpoint, QoSParamValue value) {
//
//		double oldValue = this.getQoSParamValue(endpoint, value.getType());
//
//		if (new Double(oldValue).isNaN()) {
//			oldValue = Double.NEGATIVE_INFINITY;
//		}
//
//		// no new minimum
//		if (oldValue > value.getValue()) {
//			return false;
//		}
//
//		this.addQoSValue(endpoint, value);
//
//		// There was a new maxima
//		return true;
//	}
//
//	public boolean updateQoSValueMinimum(URL endpoint, QoSParamValue value) {
//
//		double oldValue = this.getQoSParamValue(endpoint, value.getType());
//
//		if (new Double(oldValue).isNaN()) {
//			oldValue = Double.POSITIVE_INFINITY;
//		}
//
//		// no new minimum
//		if (oldValue < value.getValue()) {
//			return false;
//		}
//
//		this.addQoSValue(endpoint, value);
//
//		// There was a new maxima
//		return true;
//	}
//
//	public void updateQoSValueTotal(URL endpoint, QoSParamValue value) {
//
//		double oldValue = this.getQoSParamValue(endpoint, value.getType());
//
//		if (new Double(oldValue).isNaN()) {
//			oldValue = 0;
//		}
//
//		QoSParamValue newval = new QoSParamValue(value.getType(), oldValue
//				+ value.getValue(), value.getUnit());
//
//		this.addQoSValue(endpoint, newval);
//
//	}
//
//	public synchronized void subscribeChannel(String channelURL,
//			String subscribeContext, String _namespace, String _operationName,
//			String _soapAction) throws RepositoryException {
//		String endpointURL = subscribeContext;
//
//		this.addResourceTriple(channelURL, QueryHelper.getRDFURI("type"),
//				QueryHelper.getWSMFURI("EventChannel"), subscribeContext);
//
//		this.addResourceTriple(channelURL,
//				QueryHelper.getWSMFURI("hasSubscriber"), endpointURL,
//				subscribeContext);
//
//		this.addResourceTriple(endpointURL, QueryHelper.getRDFURI("type"),
//				QueryHelper.getWSMFURI("Subscriber"), subscribeContext);
//
//		this.updateLiteralTriple(endpointURL,
//				QueryHelper.getWSMFURI("namespace"), _namespace,
//				subscribeContext);
//
//		this.updateLiteralTriple(endpointURL,
//				QueryHelper.getWSMFURI("operation"), _operationName,
//				subscribeContext);
//
//		if (_soapAction != null) {
//			this.updateLiteralTriple(endpointURL,
//					QueryHelper.getWSMFURI("soapAction"), _soapAction,
//					subscribeContext);
//		}
//		this.commit();
//	}
//	
//}
