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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
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
import at.sti2.msee.invocation.core.common.InvokerBase;
import at.sti2.msee.invocation.core.common.InvokerREST;
import at.sti2.msee.invocation.core.common.InvokerSOAP;
import at.sti2.msee.invocation.core.common.Parameter;
import at.sti2.msee.monitoring.api.MonitoringComponent;
import at.sti2.msee.monitoring.api.MonitoringInvocationInstance;
import at.sti2.msee.monitoring.api.MonitoringInvocationState;
import at.sti2.msee.monitoring.api.exception.MonitoringException;
import at.sti2.msee.monitoring.core.MonitoringComponentImpl;
import at.sti2.msee.triplestore.ServiceRepository;

/**
 * This is the main class of the invocation service. The main functionality of
 * the invocation service is the invocation of a registered service. The class
 * inspects the service and calls the different invocation mechanisms, e.g. SOAP
 * client for WSDL-based services and the {@link HttpClient} for RESTful
 * services.
 * 
 * @author Benjamin Hiltpolt
 * @author Christian Mayr
 * 
 */
public class ServiceInvocationImpl implements ServiceInvocation {
	protected static Logger logger = Logger.getLogger(ServiceInvocationImpl.class);

	private MonitoringComponent monitoring = null;
	private ServiceRepository serviceRepository = null;

	String endpoint = null;
	String namespace = null;
	DiscoveredOperationBase discoveredOperation = null;

	/**
	 * This private constructor initializes the monitoring instance.
	 */
	private ServiceInvocationImpl() {
		try {
			this.monitoring = MonitoringComponentImpl.getInstance();
		} catch (RepositoryException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This constructor builds the instance based on the given
	 * {@link ServiceRepository}.
	 * 
	 * @param serviceRepository
	 */
	public ServiceInvocationImpl(ServiceRepository serviceRepository) {
		this();
		this.serviceRepository = serviceRepository;
	}

	/**
	 * Returns the monitoring instance that is connected to the invocation
	 * service.
	 */
	public MonitoringComponent getMonitoring() {
		return monitoring;
	}

	/**
	 * This method takes the given serviceID, operation name, and the input data
	 * (path or query parameters for RESTful services or {@link SOAPMessage} for
	 * WSDL based services) and tries to invoke the registered service. The
	 * {@link ServiceInvokerException} is thrown when the service is unknown or
	 * not registered, the operation is unknown, or the inputData is not
	 * well-formed or simply incorrect.
	 * 
	 * @param serviceID
	 * @param operation
	 * @param inputData
	 * @return result of the invocation
	 * @throws ServiceInvokerException
	 */
	@Override
	public String invoke(String serviceIDURL, String operation, String inputData)
			throws ServiceInvokerException {
		if (serviceRepository == null)
			throw new ServiceInvokerException("Repository not set by constructor");

		URL serviceID = null;
		try {
			serviceID = new URL(serviceIDURL);
		} catch (MalformedURLException e) {
			throw new ServiceInvokerException("URL is not correct: " + serviceIDURL);
		}
		prepareDataFromDiscovery(serviceID, operation);

		List<Parameter> parameters = new ParameterParser(inputData).parse();
		if (endpoint != null && discoveredOperation.getMethod() == null) {
			InvokerSOAP invokerSoap = new InvokerSOAP(monitoring);
			return invokerSoap.invokeSOAP(endpoint, operation, parameters, namespace);
		}

		// not WSDL SOAP call - REST or Other
		String address = discoveredOperation.getAddress();
		if (address != null) {
			if (address.contains("^^")) {
				address = address.substring(0, address.indexOf("^^"));
			}
			InvokerREST invokerRest = new InvokerREST(monitoring);
			return invokerRest.invokeREST(serviceID, address, discoveredOperation.getMethod(),
					parameters);
		}
		throw new ServiceInvokerException("Service type not supported");
	}

	/**
	 * Prepares the given data (serviceID and operation name) for the
	 * invocation. Herein the endpoint, namespace, and the operation is
	 * discovered from the triple store.
	 * 
	 * @param serviceID
	 *            - URI of the registrated service
	 * @param operation
	 *            - name of the operation
	 * @throws ServiceInvokerException
	 */
	private void prepareDataFromDiscovery(URL serviceID, String operation)
			throws ServiceInvokerException {
		DiscoveryServiceImpl discovery = (DiscoveryServiceImpl) ServiceDiscoveryFactory
				.createDiscoveryService(serviceRepository);
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
			logger.error(e.getLocalizedMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new ServiceInvokerException("Discovery not possible: " + e.getLocalizedMessage(),
					e);
		}

	}

	/**
	 * Returns a list of all registered services
	 * 
	 * @return
	 * @throws ServiceInvokerException
	 */
	public List<String> getAllServices() throws ServiceInvokerException {
		if (serviceRepository == null)
			throw new ServiceInvokerException("Repository not set by constructor");

		DiscoveryServiceImpl discovery = (DiscoveryServiceImpl) ServiceDiscoveryFactory
				.createDiscoveryService(serviceRepository);
		return discovery.getServiceList();
	}

	/**
	 * 
	 * TODO: lowering, lifting TODO: monitoring, TODO: use service IDs not
	 * endpoints
	 * 
	 * Deprecated since June 2013 - Use invoke(URL, String, String) instead.
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
	public String invokeSOAP(String serviceIDURL, String soapMessage)
			throws ServiceInvokerException {
		URL webserviceURL = null;
		try {
			webserviceURL = new URL(serviceIDURL);
		} catch (MalformedURLException e1) {
			throw new ServiceInvokerException("URL is not correct: " + serviceIDURL);
		}
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

	/**
	 * Deprecated because functionality moved to {@link InvokerBase}.
	 */
	@Deprecated
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

	/**
	 * Deprecated because functionality moved to {@link InvokerBase}.
	 */
	@Deprecated
	private void startMonitoring(MonitoringInvocationInstance invocationinstance)
			throws MonitoringException {
		// check if monitored
		if (invocationinstance != null) {
			invocationinstance.setState(MonitoringInvocationState.Started);
			logger.debug("Monitoring is activated");
		}
	}

	/**
	 * Deprecated because functionality moved to {@link InvokerBase}.
	 */
	@Deprecated
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

}
