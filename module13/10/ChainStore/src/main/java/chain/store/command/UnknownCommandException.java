package chain.store.command;

public class UnknownCommandException extends CommandException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
