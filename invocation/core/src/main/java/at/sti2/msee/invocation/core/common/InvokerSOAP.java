package at.sti2.msee.invocation.core.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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

/**
 * Instances of this concrete class of the abstract {@link InvokerBase} can
 * invoke WSDL based services by providing SOAP functionality.
 * 
 * @author Christian Mayr
 * 
 */
public class InvokerSOAP extends InvokerBase {

	public InvokerSOAP(MonitoringComponent monitoring) {
		super(monitoring);
	}

	public String invokeSOAP(String endpoint, String operation,
			List<Parameter> parameters, String namespace) throws ServiceInvokerException {
		logger.debug("Invoking " + endpoint);
		logger.debug("Using Variables " + parameters);

		// monitoring
		try {
			initMonitoring(new URL(endpoint));
		} catch (MalformedURLException e) {
			throw new ServiceInvokerException(e);
		}
		long startTime = System.currentTimeMillis();
		long requestMessageSize = getParameterSize(parameters);

		String results = "";

		EndpointReference targetEPR = new EndpointReference(endpoint);
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(namespace, "theMSEENamespace");
		OMElement method = fac.createOMElement(operation, omNs);
		for (Pair<String, String> entry : parameters) {
			OMElement value = fac.createOMElement(entry.name(), omNs);
			value.addChild(fac.createOMText(value, entry.value()));
			method.addChild(value);
		}

		try {
			OMElement payload = method;
			Options options = new Options();
			options.setTo(targetEPR);

			// options.setTransportInProtocol(org.apache.axis2.namespace.Constants.URI_WSDL11_SOAP);
			// options.setTransportInProtocol(org.apache.axis2.Constants.TRANSPORT_HTTP);

			startMonitoring();

			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(payload);
			sender.cleanup();
			sender.cleanupTransport();

			if (result.getFirstElement() == null) {
				results = result.getText();
			} else {
				results = result.getFirstElement().getText();
			}

			successfulInvocation(startTime, requestMessageSize, results);
		} catch (org.apache.axis2.AxisFault | MonitoringException e) {
			monitorFailedService();
			throw new ServiceInvokerException(e.getLocalizedMessage());
		}

		return results;
	}
}
