package at.sti2.msee.registration.api;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.sti2.msee.registration.api.exception.ServiceRegistrationException;


/**
- name: wsdl:portType. 
- targetNamespace: XML namespace of the WSDL and XML elements generated from the Web service. 
These cannot be used in the SEI:
- serviceName: wsdl:service 
- endpointInterface: Specifies the qualified name of the service endpoint interface that defines the services' abstract Web service contract. If specified, the service endpoint interface is used to determine the abstract WSDL contract. (String)
- portName: wsdl:portName. 
- wsdlLocation: Web address of the WSDL document defining the Web service.
**/
@WebService(name="RegistrationServicePortType", targetNamespace = "http://msee.sti2.at/delivery/")
public interface ServiceRegistration {

	@WebMethod
	public String register(
			@WebParam(name = "serviceDescriptionURL") String serviceDescriptionURL)
			throws ServiceRegistrationException;

	@WebMethod(operationName="registerInContext")
	public String register(
			@WebParam(name = "serviceDescriptionURL") String serviceDescriptionURL,
			@WebParam(name = "contextURI") String contextURI)
			throws ServiceRegistrationException;

	@WebMethod
	public String deregister(@WebParam(name = "serviceURI") String serviceURI)
			throws ServiceRegistrationException;

	@WebMethod
	public String update(@WebParam(name = "serviceURI") String serviceURI,
			@WebParam(name = "serviceURL") String serviceURL)
			throws ServiceRegistrationException;

}
