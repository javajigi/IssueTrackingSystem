package infinitefire.project.storage;

public class StorageFileNotFoundException extends StorageException {

	private static final long serialVersionUID = 3736604884873713006L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}