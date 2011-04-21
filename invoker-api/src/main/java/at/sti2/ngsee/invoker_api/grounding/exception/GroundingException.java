package at.sti2.ngsee.invoker_api.grounding.exception;

public class GroundingException extends Exception {
	
	private static final long serialVersionUID = -2182175591556556081L;

	public GroundingException() {
		super();
	}
	
	public GroundingException(String message) {
		super(message);
	}
	
	public GroundingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GroundingException(Throwable cause) {
        super(cause);
    }
}
