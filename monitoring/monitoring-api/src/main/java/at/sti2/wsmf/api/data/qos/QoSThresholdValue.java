/**
 * QoSParamValue.java - at.sti2.wsmf.data
 */
package at.sti2.wsmf.api.data.qos;

import java.io.Serializable;

import at.sti2.wsmf.api.data.qos.IQoSThresholdValue;
import at.sti2.wsmf.api.data.qos.QoSThresholdKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;
/**
 * @author Alex Oberhauser
 */
public class QoSThresholdValue implements IQoSThresholdValue, Serializable {
	private static final long serialVersionUID = -389089502834101414L;
	
	private QoSThresholdKey type;
	private String value;
	private QoSUnit unit;
	
	/**
	 * Used by the Web Service Generation Code.
	 */
	public QoSThresholdValue() {}
	
	public QoSThresholdValue(QoSThresholdKey _type, String _value, QoSUnit _unit) {
		this.type = _type;
		this.value = _value;
		this.unit = _unit;
	}

	/**
	 * @see at.sti2.wsmf.api.data.qos.IQoSParamValue#getValue()
	 */
	@Override
	public String getValue() { return this.value; }
	
	/**
	 * @see at.sti2.wsmf.api.data.qos.IQoSParamValue#getUnit()
	 */
	@Override
	public QoSUnit getUnit() { return this.unit; }
	
	/**
	 * @see at.sti2.wsmf.api.data.qos.IQoSParamValue#getType()
	 */
	@Override
	public QoSThresholdKey getType() { return this.type; }

	@Override
	public String toString() {
		return this.type + " has value " + this.value + " " + this.unit.name();
	}
}
