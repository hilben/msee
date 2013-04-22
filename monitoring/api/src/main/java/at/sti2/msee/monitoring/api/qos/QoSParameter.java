package at.sti2.msee.monitoring.api.qos;

public class QoSParameter {
	private QoSParamKey key;
	private QoSUnit unit;
	private String value;
	
	public QoSParameter(QoSParamKey key, QoSUnit unit, String value) {
		super();
		this.key = key;
		this.unit = unit;
		this.value = value;
	}

	public QoSParamKey getKey() {
		return key;
	}

	public void setKey(QoSParamKey key) {
		this.key = key;
	}

	public QoSUnit getUnit() {
		return unit;
	}

	public void setUnit(QoSUnit unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
