package chain.store.command;

public interface Command {
    public abstract String getName();

    public default String getSyntax() {
        return getName();
    }

    public abstract void handle(CommandHandlingContext context);
}
