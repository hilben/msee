/**
 * QoSParamValue.java - at.sti2.wsmf.data
 */
package at.sti2.wsmf.core.data.qos;

import java.io.Serializable;

import at.sti2.wsmf.api.data.qos.IQoSParamValue;
import at.sti2.wsmf.api.data.qos.QoSParamKey;
import at.sti2.wsmf.api.data.qos.QoSUnit;
/**
 * @author Alex Oberhauser
 */
public class QoSParamValue implements IQoSParamValue, Serializable {
	private static final long serialVersionUID = -8793209957841733499L;
	
	private QoSParamKey type;
	private String value;
	private QoSUnit unit;
	
	/**
	 * Used by the Web Service Generation Code.
	 */
	public QoSParamValue() {}
	
	public QoSParamValue(QoSParamKey _type, String _value, QoSUnit _unit) {
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
	public QoSParamKey getType() { return this.type; }

	@Override
	public String toString() {
		return this.type + " has value " + this.value + " " + this.unit.name();
	}
}
