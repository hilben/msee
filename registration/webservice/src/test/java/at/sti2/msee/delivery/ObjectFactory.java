
package at.sti2.msee.delivery;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.sti2.msee.delivery package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Register_QNAME = new QName("http://msee.sti2.at/delivery/", "register");
    private final static QName _Update_QNAME = new QName("http://msee.sti2.at/delivery/", "update");
    private final static QName _DeregisterResponse_QNAME = new QName("http://msee.sti2.at/delivery/", "deregisterResponse");
    private final static QName _RegisterInContextResponse_QNAME = new QName("http://msee.sti2.at/delivery/", "registerInContextResponse");
    private final static QName _UpdateResponse_QNAME = new QName("http://msee.sti2.at/delivery/", "updateResponse");
    private final static QName _ServiceRegistrationException_QNAME = new QName("http://msee.sti2.at/delivery/", "ServiceRegistrationException");
    private final static QName _RegisterInContext_QNAME = new QName("http://msee.sti2.at/delivery/", "registerInContext");
    private final static QName _RegisterResponse_QNAME = new QName("http://msee.sti2.at/delivery/", "registerResponse");
    private final static QName _Deregister_QNAME = new QName("http://msee.sti2.at/delivery/", "deregister");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.sti2.msee.delivery
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Update }
     * 
     */
    public Update createUpdate() {
        return new Update();
    }

    /**
     * Create an instance of {@link Register }
     * 
     */
    public Register createRegister() {
        return new Register();
    }

    /**
     * Create an instance of {@link UpdateResponse }
     * 
     */
    public UpdateResponse createUpdateResponse() {
        return new UpdateResponse();
    }

    /**
     * Create an instance of {@link RegisterInContextResponse }
     * 
     */
    public RegisterInContextResponse createRegisterInContextResponse() {
        return new RegisterInContextResponse();
    }

    /**
     * Create an instance of {@link DeregisterResponse }
     * 
     */
    public DeregisterResponse createDeregisterResponse() {
        return new DeregisterResponse();
    }

    /**
     * Create an instance of {@link ServiceRegistrationException }
     * 
     */
    public ServiceRegistrationException createServiceRegistrationException() {
        return new ServiceRegistrationException();
    }

    /**
     * Create an instance of {@link RegisterResponse }
     * 
     */
    public RegisterResponse createRegisterResponse() {
        return new RegisterResponse();
    }

    /**
     * Create an instance of {@link RegisterInContext }
     * 
     */
    public RegisterInContext createRegisterInContext() {
        return new RegisterInContext();
    }

    /**
     * Create an instance of {@link Deregister }
     * 
     */
    public Deregister createDeregister() {
        return new Deregister();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Register }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "register")
    public JAXBElement<Register> createRegister(Register value) {
        return new JAXBElement<Register>(_Register_QNAME, Register.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Update }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "update")
    public JAXBElement<Update> createUpdate(Update value) {
        return new JAXBElement<Update>(_Update_QNAME, Update.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeregisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "deregisterResponse")
    public JAXBElement<DeregisterResponse> createDeregisterResponse(DeregisterResponse value) {
        return new JAXBElement<DeregisterResponse>(_DeregisterResponse_QNAME, DeregisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterInContextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "registerInContextResponse")
    public JAXBElement<RegisterInContextResponse> createRegisterInContextResponse(RegisterInContextResponse value) {
        return new JAXBElement<RegisterInContextResponse>(_RegisterInContextResponse_QNAME, RegisterInContextResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "updateResponse")
    public JAXBElement<UpdateResponse> createUpdateResponse(UpdateResponse value) {
        return new JAXBElement<UpdateResponse>(_UpdateResponse_QNAME, UpdateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceRegistrationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "ServiceRegistrationException")
    public JAXBElement<ServiceRegistrationException> createServiceRegistrationException(ServiceRegistrationException value) {
        return new JAXBElement<ServiceRegistrationException>(_ServiceRegistrationException_QNAME, ServiceRegistrationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterInContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "registerInContext")
    public JAXBElement<RegisterInContext> createRegisterInContext(RegisterInContext value) {
        return new JAXBElement<RegisterInContext>(_RegisterInContext_QNAME, RegisterInContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "registerResponse")
    public JAXBElement<RegisterResponse> createRegisterResponse(RegisterResponse value) {
        return new JAXBElement<RegisterResponse>(_RegisterResponse_QNAME, RegisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Deregister }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://msee.sti2.at/delivery/", name = "deregister")
    public JAXBElement<Deregister> createDeregister(Deregister value) {
        return new JAXBElement<Deregister>(_Deregister_QNAME, Deregister.class, null, value);
    }

}
