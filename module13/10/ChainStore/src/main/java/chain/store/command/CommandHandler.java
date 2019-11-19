package chain.store.command;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<String, Command> map = new LinkedHashMap<>();
    private boolean shutdown;

    private static String getKey(String name) {
        return name.toUpperCase();
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void register(Command command) {
        map.put(getKey(command.getName()), command);
    }

    public void printAvailableList() {
        if(map.isEmpty()) {
            return;
        }

        System.out.println("Список доступных команд:");

        for(Command command: map.values()) {
            System.out.printf("* %s%n", command.getSyntax());
        }
    }

    public void handle(String commandLine) {
        if(shutdown) {
            return;
        }

        String commandLineParts[] = commandLine.split(" ", 2);
        String commandName = commandLineParts[0];
        Command command = map.get(getKey(commandName));

        if(command == null) {
            throw new UnknownCommandException(
                String.format("Неизвестная команда \"%s\"", commandName));
        }

        CommandHandlingContext context = new CommandHandlerContext(
            commandLineParts.length == 2? commandLineParts[1]: null
        );

        try {
            command.handle(context);
        }
        catch(CommandException exception) {
            throw exception;
        }
        catch(Exception exception) {
            throw new CommandHandlingException("При обработке команды возникло исключение", exception);
        }
    }

    public void shutdown() {
        shutdown = true;
    }

    private class CommandHandlerContext extends CommandHandlingContext {
        CommandHandlerContext(String arguments) {
            super(arguments);
        }

        @Override
        public void shutdown() {
            CommandHandler.this.shutdown();
        }
    }
}
