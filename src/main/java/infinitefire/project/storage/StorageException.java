package infinitefire.project.storage;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 4074814919047119953L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
