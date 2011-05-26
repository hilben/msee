
package at.sti2.wsmf.webservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsInvocationState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="wsInvocationState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Instantiated"/>
 *     &lt;enumeration value="Started"/>
 *     &lt;enumeration value="Terminated"/>
 *     &lt;enumeration value="Completed"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "wsInvocationState")
@XmlEnum
public enum WsInvocationState {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Instantiated")
    INSTANTIATED("Instantiated"),
    @XmlEnumValue("Started")
    STARTED("Started"),
    @XmlEnumValue("Terminated")
    TERMINATED("Terminated"),
    @XmlEnumValue("Completed")
    COMPLETED("Completed");
    private final String value;

    WsInvocationState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WsInvocationState fromValue(String v) {
        for (WsInvocationState c: WsInvocationState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
