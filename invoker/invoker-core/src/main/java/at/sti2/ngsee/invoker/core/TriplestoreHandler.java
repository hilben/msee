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
		if ( !_value.endsWith("/") )
			result[0] = (String) result[0].subSequence(0, result[0].length()-1);
		return new QName(result[0], result[1]);
	}
	
	public static InvokerMSM getInvokerMSM(String _serviceID, String _operationName) throws IOException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		Config cfg = new Config();
		RepositoryHandler reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID());
		TupleQueryResult result = reposHandler.selectSPARQL("SELECT ?serviceName ?lifting ?lowering ?wsdl ?operation ?portname ?soapaction ?endpoint WHERE { <<TODO>> }");
		
		InvokerMSM msmInstance = new InvokerMSM();
		if ( result.hasNext() ) {
			BindingSet entry = result.next();
			msmInstance.setLiftingSchema(new URL(entry.getBinding("lifting").getValue().stringValue()));
			msmInstance.setLoweringSchema(new URL(entry.getBinding("lowering").getValue().stringValue()));
			msmInstance.setWSDL(new URL(entry.getBinding("wsdl").getValue().stringValue()));
			msmInstance.setOperationQName(getStringToQName(entry.getBinding("operation").getValue().stringValue()));
			msmInstance.setServiceQName(getStringToQName(entry.getBinding("endpoint").getValue().stringValue()));
			msmInstance.setPortQName(getStringToQName(entry.getBinding("portname").getValue().stringValue()));
			msmInstance.setSOAPAction(entry.getBinding("soapaction").getValue().stringValue());
			msmInstance.setEndpointURL(new URL(entry.getBinding("endpoint").getValue().stringValue()));
		}
		
		return msmInstance;
	}
}
