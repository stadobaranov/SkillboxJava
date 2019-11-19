package chain.store.command;

public abstract class CommandHandlingContext {
    private final String arguments;

    public CommandHandlingContext(String arguments) {
        this.arguments = arguments;
    }

    public String getArguments() {
        return arguments;
    }

    public abstract void shutdown();
}
