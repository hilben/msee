package at.sti2.msee.invocation.api.exception;

public class ServiceInvokerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2259180619979262755L;

	public ServiceInvokerException() {
		super();
	}

	public ServiceInvokerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceInvokerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceInvokerException(String message) {
		super(message);
	}

	public ServiceInvokerException(Throwable cause) {
		super(cause);
	}

}
