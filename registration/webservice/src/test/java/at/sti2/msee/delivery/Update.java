
package at.sti2.msee.delivery;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for update complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="update">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceURI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "update", propOrder = {
    "serviceURI",
    "serviceURL"
})
public class Update {

    protected String serviceURI;
    protected String serviceURL;

    /**
     * Gets the value of the serviceURI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceURI() {
        return serviceURI;
    }

    /**
     * Sets the value of the serviceURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceURI(String value) {
        this.serviceURI = value;
    }

    /**
     * Gets the value of the serviceURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceURL() {
        return serviceURL;
    }

    /**
     * Sets the value of the serviceURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceURL(String value) {
        this.serviceURL = value;
    }

}
