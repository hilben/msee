/**
 * IQoSParamValue.java - at.sti2.wsmf.api.data
 */
package at.sti2.wsmf.api.data.qos;

/**
 * @author Alex Oberhauser
 */
public interface IQoSParamValue {
	
	public String getValue();
	public QoSUnit getUnit();
	public QoSParamKey getType();
	
}
