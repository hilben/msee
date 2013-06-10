package at.sti2.msee.invocation.core.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.exception.MonitoringException;

public class InvokerSOAP extends InvokerBase {

	public InvokerSOAP(MonitoringComponent monitoring) {
		super(monitoring);
	}

	public String invokeSOAP(String endpoint, String operation,
			Map<String, String> inputVariableMap, String namespace) throws ServiceInvokerException {
		logger.debug("Invoking " + endpoint);
		logger.debug("Using Variables " + inputVariableMap);

		// monitoring
		try {
			initMonitoring(new URL(endpoint));
		} catch (MalformedURLException e) {
			throw new ServiceInvokerException(e);
		}
		long startTime = System.currentTimeMillis();
		long requestMessageSize = getParameterSize(inputVariableMap);

		String results = "";

		EndpointReference targetEPR = new EndpointReference(endpoint);
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(namespace, "theMSEENamespace");
		OMElement method = fac.createOMElement(operation, omNs);
		for (Entry<String, String> entry : inputVariableMap.entrySet()) {
			OMElement value = fac.createOMElement(entry.getKey(), omNs);
			value.addChild(fac.createOMText(value, entry.getValue()));
			method.addChild(value);
		}

		try {
			OMElement payload = method;
			Options options = new Options();
			options.setTo(targetEPR);

			// options.setTransportInProtocol(Constants.URI_WSDL11_SOAP);

			startMonitoring();

			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(payload);

			String response = result.getFirstElement().getText();
			results = response;

			successfulInvocation(startTime, requestMessageSize, results);
		} catch (org.apache.axis2.AxisFault | MonitoringException e) {
			monitorFailedService();
			throw new ServiceInvokerException(e);
		}

		return results;
	}
}
