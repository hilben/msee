
package at.sti2.msee.delivery;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for registerInContext complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registerInContext">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceDescriptionURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contextURI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registerInContext", propOrder = {
    "serviceDescriptionURL",
    "contextURI"
})
public class RegisterInContext {

    protected String serviceDescriptionURL;
    protected String contextURI;

    /**
     * Gets the value of the serviceDescriptionURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceDescriptionURL() {
        return serviceDescriptionURL;
    }

    /**
     * Sets the value of the serviceDescriptionURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceDescriptionURL(String value) {
        this.serviceDescriptionURL = value;
    }

    /**
     * Gets the value of the contextURI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextURI() {
        return contextURI;
    }

    /**
     * Sets the value of the contextURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextURI(String value) {
        this.contextURI = value;
    }

}
