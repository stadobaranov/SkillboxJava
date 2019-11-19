package chain.store.command;

public class WrongCommandSyntaxException extends CommandException {
    private final String commandSyntax;

    public WrongCommandSyntaxException(String message, String commandSyntax) {
        super(message);
        this.commandSyntax = commandSyntax;
    }

    public String getCommandSyntax() {
        return commandSyntax;
    }
}
