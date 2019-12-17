package vote;

public class BirthDayFormatException extends RuntimeException {
    public BirthDayFormatException(String message) {
        super(message);
    }

    public BirthDayFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
