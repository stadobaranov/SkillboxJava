package online.school.parsing;

public class StudentParserException extends RuntimeException {
    public StudentParserException(String message) {
        super(message);
    }

    public StudentParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
