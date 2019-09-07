package subway.serializing;

public class SubwaySerializerException extends RuntimeException {
    public SubwaySerializerException(String message) {
        super(message);
    }

    public SubwaySerializerException(String message, Throwable cause) {
        super(message, cause);
    }
}
