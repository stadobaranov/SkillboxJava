public class DirectoryCopyException extends RuntimeException {
    public DirectoryCopyException(String message) {
        super(message);
    }

    public DirectoryCopyException(String message, Throwable cause) {
        super(message, cause);
    }
}
