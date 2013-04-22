package at.sti2.msee.monitoring.api.exception;

public class MonitoringInvocationInstanceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MonitoringInvocationInstanceException() {
		super();
	}

	public MonitoringInvocationInstanceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MonitoringInvocationInstanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MonitoringInvocationInstanceException(String message) {
		super(message);
	}

	public MonitoringInvocationInstanceException(Throwable cause) {
		super(cause);
	}
}
