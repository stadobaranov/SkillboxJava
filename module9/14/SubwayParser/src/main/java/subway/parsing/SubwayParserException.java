package subway.parsing;

public class SubwayParserException extends RuntimeException {
    public SubwayParserException(String message) {
        super(message);
    }

    public SubwayParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
