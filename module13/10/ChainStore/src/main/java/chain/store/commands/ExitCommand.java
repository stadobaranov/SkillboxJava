package chain.store.commands;

import chain.store.command.Command;
import chain.store.command.CommandHandlingContext;

public class ExitCommand implements Command {
    @Override
    public String getName() {
        return "ВЫХОД";
    }

    @Override
    public void handle(CommandHandlingContext context) {
        context.shutdown();
    }
}
