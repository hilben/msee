package at.sti2.msee.monitoring.api.exception;

/**
 * @author benni
 *
 */
public class MonitoringNoDataStoredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MonitoringNoDataStoredException() {
		super();
	}

	public MonitoringNoDataStoredException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MonitoringNoDataStoredException(String message, Throwable cause) {
		super(message, cause);
	}

	public MonitoringNoDataStoredException(String message) {
		super(message);
	}

	public MonitoringNoDataStoredException(Throwable cause) {
		super(cause);
	}
}