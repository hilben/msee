package at.sti2.msee.ranking.api.exception;

/**
 * @author benni
 *
 */
public class RankingException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RankingException() {
		super();
	}

	public RankingException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RankingException(String message, Throwable cause) {
		super(message, cause);
	}

	public RankingException(String message) {
		super(message);
	}

	public RankingException(Throwable cause) {
		super(cause);
	}
}
