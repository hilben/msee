
package at.sti2.see;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.sti2.see package. 
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

    private final static QName _Exception_QNAME = new QName("http://see.sti2.at/", "Exception");
    private final static QName _InvokeResponse_QNAME = new QName("http://see.sti2.at/", "invokeResponse");
    private final static QName _Invoke_QNAME = new QName("http://see.sti2.at/", "invoke");
    private final static QName _CheckAvailabilityResponse_QNAME = new QName("http://see.sti2.at/", "checkAvailabilityResponse");
    private final static QName _CheckAvailability_QNAME = new QName("http://see.sti2.at/", "checkAvailability");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.sti2.see
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CheckAvailabilityResponse }
     * 
     */
    public CheckAvailabilityResponse createCheckAvailabilityResponse() {
        return new CheckAvailabilityResponse();
    }

    /**
     * Create an instance of {@link Invoke }
     * 
     */
    public Invoke createInvoke() {
        return new Invoke();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link InvokeResponse }
     * 
     */
    public InvokeResponse createInvokeResponse() {
        return new InvokeResponse();
    }

    /**
     * Create an instance of {@link CheckAvailability }
     * 
     */
    public CheckAvailability createCheckAvailability() {
        return new CheckAvailability();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://see.sti2.at/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvokeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://see.sti2.at/", name = "invokeResponse")
    public JAXBElement<InvokeResponse> createInvokeResponse(InvokeResponse value) {
        return new JAXBElement<InvokeResponse>(_InvokeResponse_QNAME, InvokeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Invoke }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://see.sti2.at/", name = "invoke")
    public JAXBElement<Invoke> createInvoke(Invoke value) {
        return new JAXBElement<Invoke>(_Invoke_QNAME, Invoke.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckAvailabilityResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://see.sti2.at/", name = "checkAvailabilityResponse")
    public JAXBElement<CheckAvailabilityResponse> createCheckAvailabilityResponse(CheckAvailabilityResponse value) {
        return new JAXBElement<CheckAvailabilityResponse>(_CheckAvailabilityResponse_QNAME, CheckAvailabilityResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckAvailability }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://see.sti2.at/", name = "checkAvailability")
    public JAXBElement<CheckAvailability> createCheckAvailability(CheckAvailability value) {
        return new JAXBElement<CheckAvailability>(_CheckAvailability_QNAME, CheckAvailability.class, null, value);
    }

}
