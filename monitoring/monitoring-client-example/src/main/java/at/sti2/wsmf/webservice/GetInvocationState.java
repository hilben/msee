
package at.sti2.wsmf.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getInvocationState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getInvocationState">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activityStartedEvent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getInvocationState", propOrder = {
    "activityStartedEvent"
})
public class GetInvocationState {

    protected String activityStartedEvent;

    /**
     * Gets the value of the activityStartedEvent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityStartedEvent() {
        return activityStartedEvent;
    }

    /**
     * Sets the value of the activityStartedEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityStartedEvent(String value) {
        this.activityStartedEvent = value;
    }

}
