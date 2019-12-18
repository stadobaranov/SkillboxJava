package vote.parsing;

public class VoteParserException extends RuntimeException {
    public VoteParserException(String message) {
        super(message);
    }

    public VoteParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
