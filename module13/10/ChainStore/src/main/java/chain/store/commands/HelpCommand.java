package chain.store.commands;

import chain.store.command.Command;
import chain.store.command.CommandHandler;
import chain.store.command.CommandHandlingContext;

public class HelpCommand implements Command {
    private final CommandHandler handler;

    public HelpCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "ПОМОЩЬ";
    }

    @Override
    public void handle(CommandHandlingContext context) {
        handler.printAvailableList();
    }
}
