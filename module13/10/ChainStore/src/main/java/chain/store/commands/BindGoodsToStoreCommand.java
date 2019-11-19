package chain.store.commands;

import chain.store.ChainStore;
import chain.store.command.Command;
import chain.store.command.CommandHandlingContext;
import chain.store.command.WrongCommandSyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindGoodsToStoreCommand implements Command {
    private static final Pattern pattern = Pattern.compile(
        "^(?<goods>\\S+|\".+\")\\s+(?<store>\\S+|\".+\")$"
    );

    private final ChainStore chainStore;

    public BindGoodsToStoreCommand(ChainStore chainStore) {
        this.chainStore = chainStore;
    }

    @Override
    public String getName() {
        return "ВЫСТАВИТЬ_ТОВАР";
    }

    @Override
    public String getSyntax() {
        return "ВЫСТАВИТЬ_ТОВАР <Товар> <Магазин>";
    }

    @Override
    public void handle(CommandHandlingContext context) {
        String arguments = context.getArguments();

        if(arguments == null) {
            throwWrongCommandSyntaxException();
        }

        Matcher matcher = pattern.matcher(arguments);

        if(!matcher.find()) {
            throwWrongCommandSyntaxException();
        }

        chainStore.bindGoodsToStore(
            matcher.group("goods"), matcher.group("store")
        );
    }

    private void throwWrongCommandSyntaxException() {
        throw new WrongCommandSyntaxException("Необходимо указать название товара и магазина", getSyntax());
    }
}
