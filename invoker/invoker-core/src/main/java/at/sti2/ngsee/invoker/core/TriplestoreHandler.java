package at.sti2.ngsee.invoker.core;

import java.io.IOException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import at.sti2.ngsee.invoker.core.common.Config;
import at.sti2.util.triplestore.RepositoryHandler;

/**
 * <b>Purpose:</b>
 * <br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Alex Oberhauser<br>
 * @version     $Id$<br>
 * Date of creation:  13.04.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
public class TriplestoreHandler {
	
	public static QName getStringToQName(String _value) { 
		String[] result = new String[2];
		result[0] = "";
		result[1] = "";
		String[] qnameParts = _value.split("/");
		for ( int count = 0; count < (qnameParts.length-1); count++ )
			result[0] += qnameParts[count] + "/";
		result[1] = qnameParts[qnameParts.length-1];
//		if ( !_value.endsWith("/") )
//			result[0] = (String) result[0].subSequence(0, result[0].length()-1);
		return new QName(result[0], result[1]);
	}
	
	public static String generateQuery(String _serviceID, String _operationName) {
		StringBuffer query = new StringBuffer();
		query.append("PREFIX msm: <http://cms-wg.sti2.org/ns/minimal-service-model#> \n");
		query.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");
		query.append("PREFIX wsoap: <http://www.w3.org/ns/wsdl/soap#> \n");
		query.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		query.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		
		query.append("SELECT ?servicename ?portname ?namespace ?lifting ?lowering ?wsdl ?soapaction ?endpoint WHERE { \n");
		
		query.append("<" + _serviceID + "> rdfs:isDefinedBy ?wsdl . \n");
		query.append("<" + _serviceID + "> rdfs:label ?servicename . \n");
		query.append("<" + _serviceID + "> rdfs:seeAlso ?descriptionBlock . \n");
		
		query.append("?descriptionBlock wsdl:namespace ?namespace . \n");
		query.append("?descriptionBlock wsdl:binding ?bindingBlock . \n");
		query.append("?descriptionBlock wsdl:service ?serviceBlock . \n");
		
		query.append("?bindingBlock rdfs:label ?portname . \n");
		query.append("?bindingBlock wsdl:bindingOperation ?bindingOperationBlock . \n");
		query.append("?bindingOperationBlock wsoap:action ?soapaction . \n");
		
		query.append("?serviceBlock wsdl:endpoint ?endpointBlock . \n");
		query.append("?endpointBlock wsdl:address ?endpoint . \n");
		
//		query.append(" sawsdl:loweringSchemaMapping ?lowering. \n");
//		
//		query.append(" sawsdl:liftingSchemaMapping ?lifting . \n");
//		
		
		query.append("}");
		
		return query.toString();
	}
	
	public static InvokerMSM getInvokerMSM(String _serviceID, String _operationName) throws IOException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		Config cfg = new Config();
		RepositoryHandler reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		String query = generateQuery(_serviceID, _operationName);
		TupleQueryResult result = reposHandler.selectSPARQL(query);
		
		InvokerMSM msmInstance = new InvokerMSM();
		if ( result.hasNext() ) {
			BindingSet entry = result.next();
			
			String namespace = entry.getBinding("namespace").getValue().stringValue();
			
//			msmInstance.setLiftingSchema(new URL(entry.getBinding("lifting").getValue().stringValue()));
//			msmInstance.setLoweringSchema(new URL(entry.getBinding("lowering").getValue().stringValue()));
			msmInstance.setWSDL(new URL(entry.getBinding("wsdl").getValue().stringValue()));
			msmInstance.setOperationQName(new QName(namespace, _operationName));
			msmInstance.setServiceQName(new QName(namespace, entry.getBinding("servicename").getValue().stringValue()));
			msmInstance.setPortQName(new QName(namespace, entry.getBinding("portname").getValue().stringValue()));
			msmInstance.setSOAPAction(entry.getBinding("soapaction").getValue().stringValue());
			msmInstance.setEndpointURL(new URL(entry.getBinding("endpoint").getValue().stringValue()));
			
		}
		
		return msmInstance;
	}
	
}
