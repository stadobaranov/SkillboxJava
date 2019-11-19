package chain.store.command;

public class CommandHandlingException extends CommandException {
    public CommandHandlingException(String message) {
        super(message);
    }

    public CommandHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
