package at.sti2.ngsee.invoker.framework.soap;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import at.sti2.ngsee.invoker.framework.InvokerFactory;
import at.sti2.ngsee.invoker_api.framework.ISOAPInvoker;


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
public class SOAPInvoker implements ISOAPInvoker {
	
	@Override
	public Vector<String> invoke(String _wsdlURL, QName _operationQName, String... _inputData) throws AxisFault, MalformedURLException, XMLStreamException { 
		ServiceClient serviceClient = new ServiceClient();
		Options opts = new Options();
		opts.setTo(new EndpointReference(_wsdlURL));
		opts.setAction("");
		serviceClient.setOptions(opts);
		
		OMFactory factory = OMAbstractFactory.getOMFactory();
		OMElement method = factory.createOMElement(_operationQName);
		
		if ( _inputData != null ) {
			for ( int count=0; count < _inputData.length; count++ ) {
				OMElement inputData = AXIOMUtil.stringToOM(_inputData[count]);
				method.addChild(inputData);
			}
		}
		
		Vector<String> retResultVector = new Vector<String>();
		
		Iterator<?> nodes = serviceClient.sendReceive(method).getChildElements();
		while ( nodes.hasNext() ) {
			String returnResult = ((OMElement)nodes.next()).getText();
			if ( returnResult != null )
				retResultVector.add(returnResult);
		}
		serviceClient.cleanup();
		serviceClient.cleanupTransport();
		serviceClient.removeHeaders();
		return retResultVector;
	}

	public static void main(String[] args) throws Exception {
		ISOAPInvoker invoker = InvokerFactory.createSOAPInvoker();
		
		String inputData0 = "<id>http://sigimera.networld.to/instances/entries#drone_00-00-00-00-00-00</id>";
		QName operationQName0 = new QName("http://ws.sigimera.networld.to/", "get");
		System.out.println(invoker.invoke("http://localhost:9091/groundstation-webservice/services/sigimeraEntity?wsdl", operationQName0, inputData0) + "\n");
		
		String inputData11 = "<id>http://sigimera.networld.to/instances/entries#drone_00-00-00-00-00-00</id>";
		String inputData12 = "<property>foaf:name</property>";
		QName operationQName1 = new QName("http://ws.sigimera.networld.to/", "getProperty");
		System.out.println(invoker.invoke("http://localhost:9091/groundstation-webservice/services/sigimeraEntity?wsdl", operationQName1, inputData11, inputData12) + "\n");

		QName operationQName2 = new QName("http://ws.sigimera.networld.to/", "getDrones");
		System.out.println(invoker.invoke("http://localhost:9091/groundstation-webservice/services/sigimeraID?wsdl", operationQName2) + "\n");
		
//		String inputData3 = "<query>SELECT * WHERE { GRAPH &lt;http://sigimera.networld.to/instances/entries&gt; { ?subject ?predicate ?object } }</query>";
		String inputData3 = "<query>PREFIX sigimera:&lt;http://sigimera.networld.to/ontology/core.owl#&gt;\n PREFIX rdf:&lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#&gt; SELECT * WHERE { ?subject rdf:type sigimera:Drone } LIMIT 3000000</query>";
		QName operationQName3 = new QName("http://ws.sigimera.networld.to/", "select");
		System.out.println(invoker.invoke("http://localhost:9091/groundstation-webservice/services/sparql?wsdl", operationQName3, inputData3) + "\n");
		
	}
   
}
