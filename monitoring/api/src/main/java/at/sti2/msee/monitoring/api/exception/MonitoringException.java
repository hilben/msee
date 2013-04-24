package at.sti2.msee.monitoring.api.exception;

public class MonitoringException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MonitoringException() {
		super();
	}

	public MonitoringException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MonitoringException(String message, Throwable cause) {
		super(message, cause);
	}

	public MonitoringException(String message) {
		super(message);
	}

	public MonitoringException(Throwable cause) {
		super(cause);
	}
}
