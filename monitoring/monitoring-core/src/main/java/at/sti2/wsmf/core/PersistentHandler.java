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
package at.sti2.wsmf.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import at.sti2.util.triplestore.QueryHelper;
import at.sti2.util.triplestore.RepositoryHandler;
import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
import at.sti2.wsmf.api.data.qos.QoSThresholdValue;
import at.sti2.wsmf.api.data.qos.QoSUnit;
import at.sti2.wsmf.api.data.state.WSAvailabilityState;
import at.sti2.wsmf.api.data.state.WSInvocationState;
import at.sti2.wsmf.core.common.Config;
import at.sti2.wsmf.core.common.DateHelper;
import at.sti2.wsmf.core.common.HashValueHandler;
import at.sti2.wsmf.core.data.channel.ChannelSubscriber;
import at.sti2.wsmf.core.data.qos.QoSParamValue;

/**
 * @author Alex Oberhauser
 */
public class PersistentHandler {
	private static Logger log = Logger.getLogger(PersistentHandler.class);
	
	private static PersistentHandler instance = null;
	private String triplestoreEndpoint;
	private String repositoryID;
	private RepositoryHandler reposHandler;
	
	public static PersistentHandler getInstance() throws FileNotFoundException, IOException {
		if ( null == instance )
			instance = new PersistentHandler();
		return instance;
	}
	
	private PersistentHandler() throws FileNotFoundException, IOException {
		Config cfg = Config.getInstance();
		this.triplestoreEndpoint = cfg.getTripleStoreEndpoint();
		this.repositoryID = cfg.getTripleStoreReposID();
		this.reposHandler = new RepositoryHandler(this.triplestoreEndpoint, this.repositoryID);
	}
	
	public synchronized WSAvailabilityState getWSAvailabilityState(String _subject) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		StringBuffer selectSPARQL = new StringBuffer();
		selectSPARQL.append("SELECT ?availabilityState WHERE { ");
		selectSPARQL.append("  <" + _subject + "> rdf:type wsmf:Endpoint . ");
		selectSPARQL.append("  <" + _subject + "> wsmf:availabilityState ?availabilityState . ");
		selectSPARQL.append("}");
		
		WSAvailabilityState state = WSAvailabilityState.WSNotChecked;
		TupleQueryResult queryResult = this.reposHandler.selectSPARQL(QueryHelper.getNamespacePrefix() + selectSPARQL.toString());;
		if ( queryResult.hasNext() ) {
			BindingSet entry = queryResult.next();
			String stateString = entry.getBinding("availabilityState").getValue().stringValue().replace(QueryHelper.WSMF_NS, "");
			state = WSAvailabilityState.valueOf(stateString);
		}
		return state;
	}
	
	public static String getQoSParamID(String _endpoint, QoSParamKey _type) {
		String qosParamID = "unknown";
		try {
			Config cfg = Config.getInstance();
			qosParamID = cfg.getInstancePrefix() 
					+ _type + "_"
					+ HashValueHandler.computeSHA1(_endpoint);
		} catch (Exception e) {
			log.error("Hash computation failed for '" + _endpoint + "'");
		}
		return qosParamID;
	}
	
	public static String getQoSParamID(String _endpoint, QoSThresholdKey _key) {
		String qosParamID = "unknown";
		try {
			Config cfg = Config.getInstance();
			qosParamID = cfg.getInstancePrefix() 
					+ _key + "_"
					+ HashValueHandler.computeSHA1(_endpoint);
		} catch (Exception e) {
			log.error("Hash computation failed for '" + _endpoint + "'");
		}
		return qosParamID;
	}
	
	public synchronized void updateResourceTriple(String _subject, String _predicate, String _object, String _context) throws RepositoryException {
		this.reposHandler.updateResourceTriple(_subject, _predicate, _object, _context);
	}
	
	public synchronized void updateLiteralTriple(String _subject, String _predicate, String _object, String _context) throws RepositoryException {
		this.reposHandler.updateLiteralTriple(_subject, _predicate, _object, _context);
	}
	
	public synchronized void addResourceTriple(String _subject, String _predicate, String _object, String _context) throws RepositoryException {
		this.reposHandler.addResourceTriple(_subject, _predicate, _object, _context);
	}
	
	public synchronized String getQoSValue(String _subject, QoSParamKey _key) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		String type = QueryHelper.WSMF_NS + _key.name();
		StringBuffer selectSPARQL = new StringBuffer();
		selectSPARQL.append("SELECT ?qosValue WHERE { ");
		selectSPARQL.append("  <" + _subject + "> rdf:type wsmf:Endpoint . ");
		selectSPARQL.append("  <" + _subject + "> wsmf:hasQoSParam ?qos . ");
		selectSPARQL.append("  ?qos wsmf:type <" + type + "> . ");
		selectSPARQL.append("  ?qos wsmf:value ?qosValue . ");	
		selectSPARQL.append("}");
		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper.getNamespacePrefix() + selectSPARQL.toString());;
		if ( result.hasNext() )
			return result.next().getBinding("qosValue").getValue().stringValue();
		else 
			return "0";
	}
	
	
	
	/**
	 * @param externalForm
	 * @param _key
	 * @return
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 * @throws QueryEvaluationException 
	 */
	public QoSParamValue getQoSParam(URL _endpoint, QoSParamKey _key) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		String type = QueryHelper.WSMF_NS + _key.name();
		StringBuffer selectSPARQL = new StringBuffer();
		selectSPARQL.append("SELECT ?qosValue ?qosUnit WHERE { ");
		selectSPARQL.append("  <" + _endpoint.toExternalForm()  + "> rdf:type wsmf:Endpoint . ");
		selectSPARQL.append("  <" + _endpoint.toExternalForm() + "> wsmf:hasQoSParam ?qos . ");
		selectSPARQL.append("  ?qos wsmf:type <" + type + "> . ");
		selectSPARQL.append("  ?qos wsmf:value ?qosValue . ");
		selectSPARQL.append("  ?qos wsmf:unit ?qosUnit . ");	
		selectSPARQL.append("}");
		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper.getNamespacePrefix() + selectSPARQL.toString());
		if ( result.hasNext() ) {
			BindingSet entry = result.next();
			String value = entry.getBinding("qosValue").getValue().stringValue();
			QoSUnit unit = QoSUnit.valueOf(entry.getBinding("qosUnit").getValue().stringValue().replace(QueryHelper.WSMF_NS, ""));
			return new QoSParamValue(_key, value, unit);
		} else 
			return null;
	}

	public Vector<ChannelSubscriber> getSubscriber(String _subject) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		StringBuffer selectSPARQL = new StringBuffer();
		selectSPARQL.append("SELECT ?subscriber ?namespace ?operation ?soapAction WHERE { ");
		selectSPARQL.append("  <" + _subject + "> wsmf:hasSubscriber ?subscriber . ");
		selectSPARQL.append("  ?subscriber wsmf:namespace ?namespace .");
		selectSPARQL.append("  ?subscriber wsmf:operation ?operation  . ");
		selectSPARQL.append("  OPTIONAL { ?subscriber wsmf:soapAction ?soapAction } . ");	
		selectSPARQL.append("}");
		Vector<ChannelSubscriber> resultVector = new Vector<ChannelSubscriber>();
		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper.getNamespacePrefix() + selectSPARQL.toString());
		while ( result.hasNext() ) {
			BindingSet entry = result.next();
			Binding soap = entry.getBinding("soapAction");
			String soapAction = null;
			if ( soap != null )
				soapAction = soap.getValue().stringValue();
			resultVector.add(new ChannelSubscriber(entry.getBinding("subscriber").getValue().stringValue(),
					entry.getBinding("namespace").getValue().stringValue(),
					entry.getBinding("operation").getValue().stringValue(), soapAction));
			
		}
		return resultVector;
	}

	/**
	 * @param payloadsizemaximum
	 * @return
	 * @throws QueryEvaluationException 
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 */
	public QoSThresholdValue getThresholdValue(URL _endpoint, QoSThresholdKey _key) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		String type = QueryHelper.WSMF_NS + _key.name();
		StringBuffer selectSPARQL = new StringBuffer();
		selectSPARQL.append("SELECT ?qosValue ?qosUnit WHERE { ");
		selectSPARQL.append("  <" + _endpoint.toExternalForm()  + "> rdf:type wsmf:Endpoint . ");
		selectSPARQL.append("  <" + _endpoint.toExternalForm() + "> wsmf:hasQoSThresholdParam ?qos . ");
		selectSPARQL.append("  ?qos wsmf:type <" + type + "> . ");
		selectSPARQL.append("  ?qos wsmf:value ?qosValue . ");
		selectSPARQL.append("  ?qos wsmf:unit ?qosUnit . ");	
		selectSPARQL.append("}");
		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper.getNamespacePrefix() + selectSPARQL.toString());
		if ( result.hasNext() ) {
			BindingSet entry = result.next();
			String value = entry.getBinding("qosValue").getValue().stringValue();
			QoSUnit unit = QoSUnit.valueOf(entry.getBinding("qosUnit").getValue().stringValue().replace(QueryHelper.WSMF_NS, ""));
			return new QoSThresholdValue(_key, value, unit);
		} else 
			return new QoSThresholdValue(_key, "-1", QoSUnit.Unknown);
	}
	
	public void changeQoSThresholdValue(URL _endpoint, QoSThresholdValue _value) throws IOException, RepositoryException {
		String endpoint = _endpoint.toExternalForm();
		String qosParamID = PersistentHandler.getQoSParamID(endpoint, _value.getType()) + "_threshold";
		
		this.addResourceTriple(endpoint, QueryHelper.getWSMFURI("hasQoSThresholdParam"), qosParamID, endpoint);
		
		this.updateResourceTriple(qosParamID,
				QueryHelper.getRDFURI("type"), QueryHelper.getWSMFURI("QoSParam"),
				endpoint);
		
		this.updateResourceTriple(qosParamID,
				QueryHelper.getWSMFURI("type"), QueryHelper.WSMF_NS + _value.getType().name(),
				endpoint);
		
		this.updateResourceTriple(qosParamID,
				QueryHelper.getWSMFURI("unit"), QueryHelper.WSMF_NS + _value.getUnit().name(),
				endpoint);
		
		this.updateLiteralTriple(qosParamID,
				QueryHelper.getWSMFURI("value"), _value.getValue(),
				endpoint);
		
		this.updateLiteralTriple(qosParamID,
				QueryHelper.getDCURI("modified"), DateHelper.getXSDDateTime(),
				endpoint);
	}

	/**
	 * @return
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 * @throws QueryEvaluationException 
	 */
	public Vector<String> getInstanceIDs() throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		StringBuffer selectSPARQL = new StringBuffer();
		selectSPARQL.append("SELECT ?instances WHERE { ");
		selectSPARQL.append("  ?instances rdf:type wsmf:InvocationInstance . ");
		selectSPARQL.append("}");
		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper.getNamespacePrefix() + selectSPARQL.toString());
		Vector<String> resultVector = new Vector<String>();
		
		while ( result.hasNext() ) {
			resultVector.add(result.next().getBinding("instances").getValue().stringValue());
		}
		
		return resultVector;
	}
	
	/**
	 * @param _activityStartedEvent
	 * @return
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 * @throws QueryEvaluationException 
	 */
	public synchronized WSInvocationState getInvocationState(String _activityStartedEvent) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		StringBuffer selectSPARQL = new StringBuffer();
		selectSPARQL.append("SELECT ?invocationState WHERE { ");
		selectSPARQL.append("  <" + _activityStartedEvent + "> rdf:type wsmf:InvocationInstance . ");
		selectSPARQL.append("  <" + _activityStartedEvent + "> wsmf:state ?invocationState . ");
		selectSPARQL.append("}");
		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper.getNamespacePrefix() + selectSPARQL.toString());
		
		WSInvocationState state = null;
		if  ( result.hasNext() ) {
			String stateString = result.next().getBinding("invocationState").getValue().stringValue().replace(QueryHelper.WSMF_NS, "");
			state = WSInvocationState.valueOf(stateString);;
		}
		return state;
	}
	
	public synchronized Vector<URL> getEndpointWS(String _webserviceURI) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		StringBuffer selectSPARQL = new StringBuffer();
		selectSPARQL.append("SELECT ?endpointURL WHERE { ");
		selectSPARQL.append("  ?endpointURL wsmf:isRelatedToWebService <" + _webserviceURI  +"> . ");
		selectSPARQL.append("  ?endpointURL rdf:type wsmf:Endpoint . ");
		selectSPARQL.append("}");
		Vector<URL> resultVector = new Vector<URL>();
		TupleQueryResult result = this.reposHandler.selectSPARQL(QueryHelper.getNamespacePrefix() + selectSPARQL.toString());
		
		while ( result.hasNext() ) {
			String endpointWSString = result.next().getBinding("endpointURL").getValue().stringValue();
			try {
				resultVector.add(new URL(endpointWSString));
			} catch (MalformedURLException e) {
				log.error("'" + endpointWSString + "' is not a well formed fallback web service URL!");
			}
		}
		return resultVector;
	}
	
	public synchronized void deleteEndpoint(URL _webserviceURL) throws RepositoryException {
		this.reposHandler.deleteContext(_webserviceURL.toExternalForm());
		this.reposHandler.shutdown();
	}
	
	public synchronized void deleteContext(String _contextURI) throws RepositoryException {
		this.reposHandler.deleteContext(_contextURI);
		this.reposHandler.shutdown();
	}
	
	public synchronized void commit() {
		try {
			this.reposHandler.commit();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
}
