package online.school;

public class StudentStorageException extends RuntimeException {
    public StudentStorageException(String message) {
        super(message);
    }

    public StudentStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
