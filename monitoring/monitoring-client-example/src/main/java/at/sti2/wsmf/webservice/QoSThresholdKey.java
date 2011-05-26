
package at.sti2.wsmf.webservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for qoSThresholdKey.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="qoSThresholdKey">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RequestsMinimum"/>
 *     &lt;enumeration value="RequestsMaximum"/>
 *     &lt;enumeration value="ResponseTimeMinimum"/>
 *     &lt;enumeration value="ResponseTimeMaximum"/>
 *     &lt;enumeration value="PayloadSizeMinimum"/>
 *     &lt;enumeration value="PayloadSizeMaximum"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "qoSThresholdKey")
@XmlEnum
public enum QoSThresholdKey {

    @XmlEnumValue("RequestsMinimum")
    REQUESTS_MINIMUM("RequestsMinimum"),
    @XmlEnumValue("RequestsMaximum")
    REQUESTS_MAXIMUM("RequestsMaximum"),
    @XmlEnumValue("ResponseTimeMinimum")
    RESPONSE_TIME_MINIMUM("ResponseTimeMinimum"),
    @XmlEnumValue("ResponseTimeMaximum")
    RESPONSE_TIME_MAXIMUM("ResponseTimeMaximum"),
    @XmlEnumValue("PayloadSizeMinimum")
    PAYLOAD_SIZE_MINIMUM("PayloadSizeMinimum"),
    @XmlEnumValue("PayloadSizeMaximum")
    PAYLOAD_SIZE_MAXIMUM("PayloadSizeMaximum");
    private final String value;

    QoSThresholdKey(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static QoSThresholdKey fromValue(String v) {
        for (QoSThresholdKey c: QoSThresholdKey.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
