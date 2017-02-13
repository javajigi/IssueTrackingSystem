package infinitefire.project.security;

public class GetContextRequiredException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public GetContextRequiredException() {
		super();
	}
	
	public GetContextRequiredException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public GetContextRequiredException(String message, Throwable cause) {
		super(message, cause);
	}

	public GetContextRequiredException(String message) {
		super(message);
	}

	public GetContextRequiredException(Throwable cause) {
		super(cause);
	}
}
