package at.sti2.ngsee.invoker.api.grounding.exception;

/**
 * <b>Purpose:</b> This exception indicates an error during execution of the grounding engine.<br>
 * <b>Description:</b>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2011 STI<br>
 * <b>Company:</b>       STI Innsbruck<br>
 *
 * @author      Michael Rogger<br>
 * @version     $Id$<br>
 * Date of creation:  13.04.2011<br>
 * File:         $Source$<br>
 * Modifier:     $Author$<br>
 * Revision:     $Revision$<br>
 * State:        $State$<br>
 */
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
