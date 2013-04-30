package at.sti2.msee.monitoring.api.exception;

public class MonitoringNoDataStored extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MonitoringNoDataStored() {
		super();
	}

	public MonitoringNoDataStored(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MonitoringNoDataStored(String message, Throwable cause) {
		super(message, cause);
	}

	public MonitoringNoDataStored(String message) {
		super(message);
	}

	public MonitoringNoDataStored(Throwable cause) {
		super(cause);
	}
}