package chain.store.commands;

import chain.store.ChainStore;
import chain.store.command.Command;
import chain.store.command.CommandHandlingContext;
import chain.store.command.WrongCommandSyntaxException;

public class AddStoreCommand implements Command {
    private final ChainStore chainStore;

    public AddStoreCommand(ChainStore chainStore) {
        this.chainStore = chainStore;
    }

    @Override
    public String getName() {
        return "ДОБАВИТЬ_МАГАЗИН";
    }

    @Override
    public String getSyntax() {
        return "ДОБАВИТЬ_МАГАЗИН <Название>";
    }

    @Override
    public void handle(CommandHandlingContext context) {
        String name = context.getArguments();

        if(name == null) {
            throw new WrongCommandSyntaxException("Необходимо указать название магазина", getSyntax());
        }

        chainStore.addStore(name);

    }
}
