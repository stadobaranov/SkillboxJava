package chain.store.commands;

import chain.store.ChainStore;
import chain.store.command.Command;
import chain.store.command.CommandHandlingContext;
import chain.store.command.WrongCommandSyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddGoodsCommand implements Command {
    private static final Pattern pattern = Pattern.compile(
        "^(?<name>\\S+|\".+\")\\s+(?<price>[1-9]\\d*)$"
    );

    private final ChainStore chainStore;

    public AddGoodsCommand(ChainStore chainStore) {
        this.chainStore = chainStore;
    }

    @Override
    public String getName() {
        return "ДОБАВИТЬ_ТОВАР";
    }

    @Override
    public String getSyntax() {
        return "ДОБАВИТЬ_ТОВАР <Название> <Цена>";
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

        chainStore.addGoods(
            matcher.group("name"), Integer.parseInt(matcher.group("price"))
        );
    }

    private void throwWrongCommandSyntaxException() {
        throw new WrongCommandSyntaxException("Необходимо указать название и цену товара", getSyntax());
    }
}
