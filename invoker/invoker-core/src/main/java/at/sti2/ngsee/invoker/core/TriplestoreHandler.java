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
package at.sti2.ngsee.invoker.core;

import java.io.IOException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import at.sti2.ngsee.invoker.core.common.InvokerConfig;
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
	
	/**
	 * TODO: Only IN_OUT is supported. Make inputMessage and outputMessage optional to support also IN_ONLY and OUT_ONLY.
	 * 
	 * @param _serviceID
	 * @param _operationName
	 * @return
	 */
	public static String generateQuery(String _serviceID, String _operationName) {
		StringBuffer query = new StringBuffer();
		query.append("PREFIX msm: <http://cms-wg.sti2.org/ns/minimal-service-model#> \n");
		query.append("PREFIX wsdl: <http://www.w3.org/ns/wsdl-rdf#> \n");
		query.append("PREFIX sawsdl: <http://www.w3.org/ns/sawsdl#> \n");
		query.append("PREFIX wsoap: <http://www.w3.org/ns/wsdl/soap#> \n");
		query.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		query.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n");
		query.append("PREFIX msm_ext: <http://sesa.sti2.at/ns/minimal-service-model-ext#> \n");
		
		query.append("SELECT ?servicename ?portname ?namespace ?lifting ?lowering ?wsdl ?soapaction ?endpoint WHERE { \n");
		
		query.append("<" + _serviceID + "> rdfs:isDefinedBy ?wsdl . \n");
		query.append("<" + _serviceID + "> rdfs:label ?servicename . \n");
		query.append("<" + _serviceID + "> msm_ext:wsdlDescription ?descriptionBlock . \n");
		
		query.append("?descriptionBlock wsdl:namespace ?namespace . \n");
		query.append("?descriptionBlock wsdl:binding ?bindingBlock . \n");
		query.append("?descriptionBlock wsdl:service ?serviceBlock . \n");
		query.append("?descriptionBlock wsdl:interface ?interfaceBlock . \n");
		
		query.append("?bindingBlock rdfs:label ?portname . \n");
		query.append("?bindingBlock wsdl:bindingOperation ?bindingOperationBlock . \n");
		query.append("OPTIONAL { \n");
		query.append("?bindingOperationBlock wsoap:action ?soapaction . \n");
		query.append("} \n");
		
		query.append("?interfaceBlock wsdl:interfaceOperation ?interfaceOperation . \n");
		query.append("?interfaceOperation rdfs:label \"" + _operationName + "\" . \n");
		query.append("?interfaceOperation wsdl:interfaceMessageReference ?inputMessage . \n");
		query.append("?inputMessage rdf:type wsdl:InputMessage . \n");
		query.append("?inputMessage sawsdl:loweringSchemaMapping ?lowering. \n");
		
		query.append("?interfaceOperation wsdl:interfaceMessageReference ?outputMessage . \n");
		query.append("?outputMessage rdf:type wsdl:OutputMessage . \n");
		query.append("?outputMessage sawsdl:liftingSchemaMapping ?lifting . \n");
		
		query.append("?serviceBlock wsdl:endpoint ?endpointBlock . \n");
		query.append("?endpointBlock wsdl:address ?endpoint . \n");

		query.append("}");
		
		return query.toString();
	}
	
	public static InvokerMSM getInvokerMSM(String _serviceID, String _operationName) throws IOException, QueryEvaluationException, RepositoryException, MalformedQueryException {
		InvokerConfig cfg = new InvokerConfig();
		RepositoryHandler reposHandler = new RepositoryHandler(cfg.getSesameEndpoint(), cfg.getSesameReposID(),false);
		String query = generateQuery(_serviceID, _operationName);
		TupleQueryResult result = reposHandler.selectSPARQL(query);
		
		InvokerMSM msmInstance = new InvokerMSM();
		if ( result.hasNext() ) {
			BindingSet entry = result.next();
			
			String namespace = entry.getBinding("namespace").getValue().stringValue();
			
			msmInstance.setLiftingSchema(new URL(entry.getBinding("lifting").getValue().stringValue()));
			msmInstance.setLoweringSchema(new URL(entry.getBinding("lowering").getValue().stringValue()));
			msmInstance.setWSDL(new URL(entry.getBinding("wsdl").getValue().stringValue()));
			msmInstance.setOperationQName(new QName(namespace, _operationName));
			msmInstance.setServiceQName(new QName(namespace, entry.getBinding("servicename").getValue().stringValue()));
			msmInstance.setPortQName(new QName(namespace, entry.getBinding("portname").getValue().stringValue()));
			Binding soapAction = entry.getBinding("soapaction");
			if ( null != soapAction )
				msmInstance.setSOAPAction(soapAction.getValue().stringValue());
			msmInstance.setEndpointURL(new URL(entry.getBinding("endpoint").getValue().stringValue()));
			
		}
		
		return msmInstance;
	}
	
	public static void main(String[] args) throws Exception {
		InvokerMSM invokerMSM = TriplestoreHandler.getInvokerMSM("http://www.webserviceX.NET#GlobalWeather", "GetWeather");
		System.out.println(invokerMSM.getOperationQName());
		System.out.println(invokerMSM);
		
		invokerMSM = TriplestoreHandler.getInvokerMSM("http://www.nanonull.com/TimeService/TimeService.asmx", "getUTCTime");
		System.out.println(invokerMSM.getOperationQName());
		System.out.println(invokerMSM);
		
		invokerMSM = TriplestoreHandler.getInvokerMSM("http://sesa.sti2.at/services/dummy/#ValenciaPortWebServiceService", "submitFALForm");
		System.out.println(invokerMSM.getOperationQName());
		System.out.println(invokerMSM);
	}
	
}
