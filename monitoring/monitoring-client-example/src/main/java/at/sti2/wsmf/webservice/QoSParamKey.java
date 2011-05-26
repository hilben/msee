
package at.sti2.wsmf.webservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for qoSParamKey.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="qoSParamKey">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RequestTotal"/>
 *     &lt;enumeration value="RequestSuccessful"/>
 *     &lt;enumeration value="RequestFailed"/>
 *     &lt;enumeration value="ResponseTimeMinimum"/>
 *     &lt;enumeration value="ResponseTimeMaximum"/>
 *     &lt;enumeration value="ResponseTimeAverage"/>
 *     &lt;enumeration value="PayloadSizeMinimum"/>
 *     &lt;enumeration value="PayloadSizeAverage"/>
 *     &lt;enumeration value="PayloadSizeMaximum"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "qoSParamKey")
@XmlEnum
public enum QoSParamKey {

    @XmlEnumValue("RequestTotal")
    REQUEST_TOTAL("RequestTotal"),
    @XmlEnumValue("RequestSuccessful")
    REQUEST_SUCCESSFUL("RequestSuccessful"),
    @XmlEnumValue("RequestFailed")
    REQUEST_FAILED("RequestFailed"),
    @XmlEnumValue("ResponseTimeMinimum")
    RESPONSE_TIME_MINIMUM("ResponseTimeMinimum"),
    @XmlEnumValue("ResponseTimeMaximum")
    RESPONSE_TIME_MAXIMUM("ResponseTimeMaximum"),
    @XmlEnumValue("ResponseTimeAverage")
    RESPONSE_TIME_AVERAGE("ResponseTimeAverage"),
    @XmlEnumValue("PayloadSizeMinimum")
    PAYLOAD_SIZE_MINIMUM("PayloadSizeMinimum"),
    @XmlEnumValue("PayloadSizeAverage")
    PAYLOAD_SIZE_AVERAGE("PayloadSizeAverage"),
    @XmlEnumValue("PayloadSizeMaximum")
    PAYLOAD_SIZE_MAXIMUM("PayloadSizeMaximum");
    private final String value;

    QoSParamKey(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static QoSParamKey fromValue(String v) {
        for (QoSParamKey c: QoSParamKey.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
