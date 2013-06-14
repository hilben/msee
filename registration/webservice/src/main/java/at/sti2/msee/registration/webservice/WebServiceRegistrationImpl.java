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
package at.sti2.msee.registration.webservice;

import java.io.IOException;

import javax.jws.WebService;

import at.sti2.msee.registration.api.ServiceRegistration;
import at.sti2.msee.registration.api.exception.ServiceRegistrationException;
import at.sti2.msee.registration.core.ServiceRegistrationFactory;
import at.sti2.msee.registration.core.configuration.ServiceRegistrationConfiguration;
import at.sti2.msee.registration.webservice.configuration.Configuration;
import at.sti2.msee.triplestore.ServiceRepositoryConfiguration;

/**
 * - name: wsdl:portType. - targetNamespace: XML namespace of the WSDL and XML
 * elements generated from the Web service. These cannot be used in the SEI: -
 * serviceName: wsdl:service - endpointInterface: the java interface that
 * defines the SEI contract - portName: wsdl:portName. - wsdlLocation: Web
 * address of the WSDL document defining the Web service.
 **/

@WebService(targetNamespace = "http://msee.sti2.at/delivery/registration/", endpointInterface = "at.sti2.msee.registration.api.ServiceRegistration", portName = "RegistrationServicePort", serviceName = "service")
public class WebServiceRegistrationImpl implements ServiceRegistration {
	ServiceRegistration registrationDelegate;

	public WebServiceRegistrationImpl() throws IOException {
		Configuration configuration = new Configuration();
		this.loadConfiguration(configuration);
	}

	public WebServiceRegistrationImpl(ServiceRegistration registrationDelegate) {
		this.registrationDelegate = registrationDelegate;
	}

	WebServiceRegistrationImpl(Configuration configuration) throws IOException {
		this.loadConfiguration(configuration);
	}

	private void loadConfiguration(Configuration configuration) {
		String repositoryId = "msee"; //configuration.getSesameReposID();
		String serverEndpoint = "http://sesa.sti2.at:8080/openrdf-sesame"; //configuration.getSesameEndpoint();

		ServiceRegistrationConfiguration registrationConfiguration = new ServiceRegistrationConfiguration();

		ServiceRepositoryConfiguration repositoryConfiguration = new ServiceRepositoryConfiguration();
		repositoryConfiguration.setRepositoryID(repositoryId);
		repositoryConfiguration.setServerEndpoint(serverEndpoint);
		registrationConfiguration.setRepositoryConfiguration(repositoryConfiguration);

		registrationDelegate = ServiceRegistrationFactory
				.createServiceRegistration(registrationConfiguration);
	}

	@Override
	public String register(String serviceDescriptionURL)
			throws ServiceRegistrationException {
		return registrationDelegate.register(serviceDescriptionURL);
	}

	@Override
	public String registerInContext(String serviceDescriptionURL,
			String contextURI) throws ServiceRegistrationException {
		return registrationDelegate.registerInContext(serviceDescriptionURL,
				contextURI);
	}

	@Override
	public String deregister(String serviceURI)
			throws ServiceRegistrationException {
		throw new ServiceRegistrationException("Not yet implemented");
	}

	@Override
	public String update(String serviceURI, String serviceURL)
			throws ServiceRegistrationException {
		throw new ServiceRegistrationException("Not yet implemented");
	}
}
