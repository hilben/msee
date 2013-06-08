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
package at.sti2.msee.invocation.core;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.rpc.ServiceException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.openrdf.repository.RepositoryException;
import org.xml.sax.SAXException;

import at.sti2.msee.discovery.api.webservice.DiscoveryException;
import at.sti2.msee.discovery.core.DiscoveryServiceImpl;
import at.sti2.msee.discovery.core.ServiceDiscoveryFactory;
import at.sti2.msee.discovery.core.tree.DiscoveredOperation;
import at.sti2.msee.discovery.core.tree.DiscoveredOperationBase;
import at.sti2.msee.discovery.core.tree.DiscoveredService;
import at.sti2.msee.invocation.api.ServiceInvocation;
import at.sti2.msee.invocation.api.exception.ServiceInvokerException;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.triplestore.ServiceRepository;

/**
 * @author Benjamin Hiltpolt
 * 
 *         TODO: Documentation
 */
public class ServiceInvocationImpl implements ServiceInvocation {
	protected static Logger logger = Logger.getLogger(ServiceInvocationImpl.class);

	private MonitoringComponent monitoring = null;
	private ServiceRepository serviceRepository = null;

	public ServiceInvocationImpl() {
		try {
			this.monitoring = MonitoringComponentImpl.getInstance();
		} catch (RepositoryException | IOException e) {
			e.printStackTrace();
		}
	}

	public ServiceInvocationImpl(ServiceRepository serviceRepository) {
		this();
		this.serviceRepository = serviceRepository;
	}

	/**
	 * 
	 * TODO: lowering, lifting TODO: monitoring, TODO: use service IDs not
	 * endpoints
	 * 
	 * @param webserviceURL
	 * @param operationName
	 * @param inputData
	 * @return
	 * @throws MonitoringException
	 * @throws ServiceException
	 * @throws MalformedURLException
	 * @throws SAXException
	 * @throws AxisFault
	 */
	@Override
	@Deprecated
	public String invokeSOAP(URL webserviceURL, String soapMessage) throws ServiceInvokerException {

		// Setup monitoring data and prepare stuff
		long requestMessageSize = soapMessage.getBytes().length;
		long startTime = System.currentTimeMillis();
		MonitoringInvocationInstance invocationinstance = initMonitoring(webserviceURL);

		String results = "";
		URL endpoint = webserviceURL;
		String soapFile = soapMessage;

		Service service = new Service();
		Call call;

		logger.debug("Invoking " + webserviceURL);
		logger.debug("Using SOAP-Message " + soapFile);

		try {
			startMonitoring(invocationinstance);

			call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint);

			ByteArrayInputStream soapMessageStream = new ByteArrayInputStream(
					soapFile.getBytes("UTF-8"));

			SOAPEnvelope env = new SOAPEnvelope(soapMessageStream);

			results = call.invoke(env).toString();
			logger.debug("Obtainer results" + results);

			if (invocationinstance != null) {
				invocationinstance.setState(MonitoringInvocationState.Completed);
				long responseMessageSize = results.getBytes().length;
				long time = System.currentTimeMillis() - startTime;

				invocationinstance.sendSuccessfulInvocation(responseMessageSize,
						requestMessageSize, time);

				logger.debug("Performed monitoring. Invocation took: " + time + " ms");
			}

		} catch (ServiceException | SAXException | AxisFault | UnsupportedEncodingException
				| MonitoringException e) {
			logger.error(e);
			monitorFailedService(invocationinstance);
			throw new ServiceInvokerException(e);
		}

		return results;
	}

	private void monitorFailedService(MonitoringInvocationInstance invocationinstance)
			throws ServiceInvokerException {
		if (invocationinstance != null) {
			try {
				invocationinstance.sendUnsuccessfulInvocation();
			} catch (MonitoringException e1) {
				throw new ServiceInvokerException(e1);
			}
		}
	}

	private void startMonitoring(MonitoringInvocationInstance invocationinstance)
			throws MonitoringException {
		// check if monitored
		if (invocationinstance != null) {
			invocationinstance.setState(MonitoringInvocationState.Started);
			logger.debug("Monitoring is activated");
		}
	}

	private MonitoringInvocationInstance initMonitoring(URL webserviceURL) {

		if (this.monitoring == null) {
			return null;
		}
		try {
			if (monitoring.isMonitoredWebService(webserviceURL)) {
				logger.debug("Loading a invocationinstance into the invoker");
				MonitoringInvocationInstance invocationinstance = monitoring
						.createInvocationInstance(webserviceURL);
				invocationinstance.setState(MonitoringInvocationState.Instantiated);
				return invocationinstance;
			}
		} catch (MonitoringException e) {
			return null;
		}

		return null;
	}

	public MonitoringComponent getMonitoring() {
		return monitoring;
	}

	public void setMonitoring(MonitoringComponent monitoring) {
		this.monitoring = monitoring;
	}

	protected String invokeREST(final URL serviceID, String address, final String method,
			final Map<String, String> parameters) throws ServiceInvokerException {
		// monitoring
		long startTime = System.currentTimeMillis();
		MonitoringInvocationInstance invocationinstance = initMonitoring(serviceID);
		long requestMessageSize = getParameterSize(parameters);

		// REST
		final String charset = "UTF-8";
		NameValuePair[] data = new NameValuePair[parameters.size()];
		int i = 0;
		for (Entry<String, String> parameterSet : parameters.entrySet()) {
			try {
				address = address.replace("{" + parameterSet.getKey() + "}",
						URLEncoder.encode(parameterSet.getValue(), charset));
			} catch (UnsupportedEncodingException e) {
				throw new ServiceInvokerException(e);
			}
			NameValuePair tmpPair = new NameValuePair(parameterSet.getKey(),
					parameterSet.getValue());
			data[i++] = tmpPair;
		}
		logger.info("Invocation of: " + address);

		HttpClient client = new HttpClient();
		String output = "";

		try {
			startMonitoring(invocationinstance);
		} catch (MonitoringException e) {
			logger.error(e);
		}

		switch (method.toLowerCase()) {
		case "get":
			GetMethod getHandler = new GetMethod(address);

			InputStream getResponse = new ByteArrayInputStream("".getBytes());
			try {
				checkStatus(client.executeMethod(getHandler));
				getResponse = getHandler.getResponseBodyAsStream();
				output = convertStreamToString(getResponse);
			} catch (IOException e) {
				monitorFailedService(invocationinstance);
				throw new ServiceInvokerException(e);
			} finally {
				getHandler.releaseConnection();
			}
			break;
		case "post":
			PostMethod postHandler = new PostMethod(address);

			InputStream postResponse = new ByteArrayInputStream("".getBytes());
			try {
				postHandler.setQueryString(data); // TODO check: are both
													// needed?
				postHandler.setRequestBody(data);
				checkStatus(client.executeMethod(postHandler));
				postResponse = postHandler.getResponseBodyAsStream();
				output = convertStreamToString(postResponse);
			} catch (IOException e) {
				monitorFailedService(invocationinstance);
				throw new ServiceInvokerException(e);
			} finally {
				postHandler.releaseConnection();
			}
			break;
		case "put":
			PutMethod putHandler = new PutMethod(address);
			InputStream putResponse = new ByteArrayInputStream("".getBytes());
			try {
				putHandler.setRequestEntity(new StringRequestEntity(parameters.get("data"),
						"application/json", charset));
				checkStatus(client.executeMethod(putHandler));
				putResponse = putHandler.getResponseBodyAsStream();
				output = convertStreamToString(putResponse);
			} catch (IOException e) {
				monitorFailedService(invocationinstance);
				throw new ServiceInvokerException(e);
			} finally {
				putHandler.releaseConnection();
			}
			break;
		case "delete":
			DeleteMethod deleteHandler = new DeleteMethod(address);
			InputStream deleteResponse = new ByteArrayInputStream("".getBytes());
			try {
				client.executeMethod(deleteHandler);
				deleteResponse = deleteHandler.getResponseBodyAsStream();
				output = convertStreamToString(deleteResponse);
			} catch (IOException e) {
				monitorFailedService(invocationinstance);
				throw new ServiceInvokerException(e);
			} finally {
				deleteHandler.releaseConnection();
			}
			break;
		default:
			throw new ServiceInvokerException("method not supported");
		}

		if (invocationinstance != null) {
			try {
				invocationinstance.setState(MonitoringInvocationState.Completed);
				long responseMessageSize = output.getBytes().length;
				long time = System.currentTimeMillis() - startTime;

				invocationinstance.sendSuccessfulInvocation(responseMessageSize,
						requestMessageSize, time);

				logger.debug("Performed monitoring. Invocation took: " + time + " ms");
			} catch (MonitoringException e) {
				e.printStackTrace();
			}
		}

		return output;
	}

	private int checkStatus(int status) throws ServiceInvokerException {
		if (status < 200 || status > 299) {
			throw new ServiceInvokerException("Invocation failed with status " + status);
		}
		return status;
	}

	private long getParameterSize(Map<String, String> parameters) {
		long size = 0;
		for (Entry<String, String> entrySet : parameters.entrySet()) {
			size += entrySet.getValue().length();
		}
		return size;
	}

	private static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is);
		s.useDelimiter("\\A");
		String retval = s.hasNext() ? s.next() : "";
		s.close();
		return retval;
	}

	@Override
	public String invoke(URL serviceID, String operation, String inputData)
			throws ServiceInvokerException {
		if (serviceRepository == null)
			throw new ServiceInvokerException("Repository not set by constructor");

		DiscoveryServiceImpl discovery = (DiscoveryServiceImpl) ServiceDiscoveryFactory
				.createDiscoveryService(serviceRepository);
		String endpoint = null;
		String namespace = null;
		DiscoveredOperationBase discoveredOperation = null;

		try {
			DiscoveredService discoveredService = discovery.discoverService(serviceID.toString());
			if (discoveredService == null) {
				throw new ServiceInvokerException("Service \"" + serviceID
						+ "\" was not found or is invalid");
			}
			endpoint = discoveredService.getEndpoint();
			namespace = discoveredService.getNameSpace();

			Iterator<DiscoveredOperation> ito = discoveredService.getOperationSet().iterator();
			while (ito.hasNext()) {
				DiscoveredOperation op = ito.next();
				if (op.getName().endsWith(operation)) { // TODO check
					discoveredOperation = (DiscoveredOperationBase) op;
					break;
				}
			}
			if (discoveredOperation == null) {
				throw new ServiceInvokerException("Operation " + operation + " not found in "
						+ serviceID + " of service " + discoveredService.getName());
			}
		} catch (DiscoveryException e) {
			e.printStackTrace();
		}
		// Model model = serviceRepository.getModel().open();
		// System.out.println(model.serialize(Syntax.RdfXml));

		XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(inputData.getBytes()));
		@SuppressWarnings("unchecked")
		Map<String, String> parameterMap = (Map<String, String>) decoder.readObject();
		decoder.close();
		if (endpoint != null && discoveredOperation.getMethod() == null) {
			return invokeNewSOAP(endpoint, operation, parameterMap, namespace);
		}

		// not WSDL SOAP call - REST or Other
		String address = discoveredOperation.getAddress();
		if (address.contains("^^")) {
			address = address.substring(0, address.indexOf("^^"));
		}
		if (address != null) {
			return invokeREST(serviceID, address, discoveredOperation.getMethod(), parameterMap);
		}
		throw new ServiceInvokerException("Service type not supported");
	}

	private String invokeNewSOAP(String endpoint, String operation,
			Map<String, String> inputVariableMap, String namespace) {
		logger.debug("Invoking " + endpoint);
		logger.debug("Using Variables " + inputVariableMap);

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

			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			OMElement result = sender.sendReceive(payload);

			String response = result.getFirstElement().getText();
			results = response;

		} catch (org.apache.axis2.AxisFault e) {
			e.printStackTrace();
		}

		return results;
	}

}
