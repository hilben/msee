package at.sti2.msee.test;

import java.util.List;
import java.util.logging.Logger;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class TesterBase {
	final String server = "http://sesa.sti2.at:8080";
	Logger LOGGER = Logger.getLogger(this.getClass().getCanonicalName());
	@Rule
	public ExpectedException exception = ExpectedException.none();

	public String invokeSOAP(String endpoint, String operation,
			List<Pair<String, String>> inputParameters, String namespace) throws AxisFault {

		long startTime = System.currentTimeMillis();
		LOGGER.info(Long.toString(startTime));

		String results = "";

		EndpointReference targetEPR = new EndpointReference(endpoint);
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(namespace, "theMSEENamespace");
		OMElement method = fac.createOMElement(operation, omNs);
		if (inputParameters != null && inputParameters.size() > 0)
			for (Pair<String, String> entry : inputParameters) {
				OMElement value = fac.createOMElement(entry.L(), omNs);
				value.addChild(fac.createOMText(value, entry.R()));
				method.addChild(value);
			}

		OMElement payload = method;
		Options options = new Options();
		options.setTo(targetEPR);

		ServiceClient sender = new ServiceClient();
		sender.setOptions(options);
		OMElement result = sender.sendReceive(payload);

		if (result.getFirstElement() == null) {
			results = result.getText();
		} else {
			results = result.getFirstElement().getText();
		}
		sender.cleanup();
		sender.cleanupTransport();

		return results;
	}

	class Pair<L, R> {
		private L left;
		private R right;

		public Pair(L left, R right) {
			this.left = left;
			this.right = right;
		}

		public L L() {
			return left;
		}

		public R R() {
			return right;
		}
	}
}
