package chain.store;

import chain.store.command.CommandHandler;
import chain.store.command.CommandHandlingException;
import chain.store.command.UnknownCommandException;
import chain.store.command.WrongCommandSyntaxException;
import chain.store.commands.AddGoodsCommand;
import chain.store.commands.AddStoreCommand;
import chain.store.commands.BindGoodsToStoreCommand;
import chain.store.commands.DisplayStoreStatisticsCommand;
import chain.store.commands.ExitCommand;
import chain.store.commands.HelpCommand;
import java.util.Scanner;

public class Application implements AutoCloseable {
    private final ChainStore chainStore = new ChainStore("192.168.99.100", 27017);
    private final CommandHandler commandHandler = new CommandHandler();

    private void prepareCommands() {
        commandHandler.register(new HelpCommand(commandHandler));
        commandHandler.register(new AddStoreCommand(chainStore));
        commandHandler.register(new AddGoodsCommand(chainStore));
        commandHandler.register(new BindGoodsToStoreCommand(chainStore));
        commandHandler.register(new DisplayStoreStatisticsCommand(chainStore));
        commandHandler.register(new ExitCommand());
    }

    public void run() {
        prepareCommands();

        commandHandler.printAvailableList();
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while(!commandHandler.isShutdown()) {
            System.out.print("Введите команду: ");

            try {
                commandHandler.handle(scanner.nextLine());
            }
            catch(UnknownCommandException exception) {
                System.out.println(exception.getMessage());
                commandHandler.printAvailableList();
            }
            catch(WrongCommandSyntaxException exception) {
                System.out.printf("%s%nСинтаксис: %s%n", exception.getMessage(), exception.getCommandSyntax());
            }
            catch(CommandHandlingException exception) {
                System.out.println("При выполнении команды что-то пошло не так!");
                exception.getCause().printStackTrace(System.out);
            }

            System.out.println();
        }
    }

    @Override
    public void close() {
        chainStore.close();
    }

    public static void main(String... args) {
        try(Application application = new Application()) {
            application.run();
        }
    }
}
