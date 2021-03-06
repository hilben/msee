
package at.sti2.msee.delivery;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import at.sti2.msee.delivery.registration.ObjectFactory;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.7-b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "RegistrationServicePortType", targetNamespace = "http://msee.sti2.at/delivery/registration/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface RegistrationServicePortType {


    /**
     * 
     * @param serviceDescriptionURL
     * @return
     *     returns java.lang.String
     * @throws ServiceRegistrationException
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "register", targetNamespace = "http://msee.sti2.at/delivery/registration/", className = "at.sti2.msee.delivery.registration.Register")
    @ResponseWrapper(localName = "registerResponse", targetNamespace = "http://msee.sti2.at/delivery/registration/", className = "at.sti2.msee.delivery.registration.RegisterResponse")
    @Action(input = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/registerRequest", output = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/registerResponse", fault = {
        @FaultAction(className = ServiceRegistrationException.class, value = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/register/Fault/ServiceRegistrationException")
    })
    public String register(
        @WebParam(name = "serviceDescriptionURL", targetNamespace = "")
        String serviceDescriptionURL)
        throws ServiceRegistrationException
    ;

    /**
     * 
     * @param serviceURL
     * @param serviceURI
     * @return
     *     returns java.lang.String
     * @throws ServiceRegistrationException
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "update", targetNamespace = "http://msee.sti2.at/delivery/registration/", className = "at.sti2.msee.delivery.registration.Update")
    @ResponseWrapper(localName = "updateResponse", targetNamespace = "http://msee.sti2.at/delivery/registration/", className = "at.sti2.msee.delivery.registration.UpdateResponse")
    @Action(input = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/updateRequest", output = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/updateResponse", fault = {
        @FaultAction(className = ServiceRegistrationException.class, value = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/update/Fault/ServiceRegistrationException")
    })
    public String update(
        @WebParam(name = "serviceURI", targetNamespace = "")
        String serviceURI,
        @WebParam(name = "serviceURL", targetNamespace = "")
        String serviceURL)
        throws ServiceRegistrationException
    ;

    /**
     * 
     * @param serviceDescriptionURL
     * @param contextURI
     * @return
     *     returns java.lang.String
     * @throws ServiceRegistrationException
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "registerInContext", targetNamespace = "http://msee.sti2.at/delivery/registration/", className = "at.sti2.msee.delivery.registration.RegisterInContext")
    @ResponseWrapper(localName = "registerInContextResponse", targetNamespace = "http://msee.sti2.at/delivery/registration/", className = "at.sti2.msee.delivery.registration.RegisterInContextResponse")
    @Action(input = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/registerInContextRequest", output = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/registerInContextResponse", fault = {
        @FaultAction(className = ServiceRegistrationException.class, value = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/registerInContext/Fault/ServiceRegistrationException")
    })
    public String registerInContext(
        @WebParam(name = "serviceDescriptionURL", targetNamespace = "")
        String serviceDescriptionURL,
        @WebParam(name = "contextURI", targetNamespace = "")
        String contextURI)
        throws ServiceRegistrationException
    ;

    /**
     * 
     * @param serviceURI
     * @return
     *     returns java.lang.String
     * @throws ServiceRegistrationException
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deregister", targetNamespace = "http://msee.sti2.at/delivery/registration/", className = "at.sti2.msee.delivery.registration.Deregister")
    @ResponseWrapper(localName = "deregisterResponse", targetNamespace = "http://msee.sti2.at/delivery/registration/", className = "at.sti2.msee.delivery.registration.DeregisterResponse")
    @Action(input = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/deregisterRequest", output = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/deregisterResponse", fault = {
        @FaultAction(className = ServiceRegistrationException.class, value = "http://msee.sti2.at/delivery/registration/RegistrationServicePortType/deregister/Fault/ServiceRegistrationException")
    })
    public String deregister(
        @WebParam(name = "serviceURI", targetNamespace = "")
        String serviceURI)
        throws ServiceRegistrationException
    ;

}
