package ru.askar.serverLab6.serverCommand;

import java.util.ArrayList;
import java.util.List;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.object.Command;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerHelpCommand extends ServerCommand {
    private final CommandExecutor<ServerCommand> executor;

    /** Заполнение имени и количества требуемых аргументов */
    public ServerHelpCommand(ServerHandler serverHandler, CommandExecutor<ServerCommand> executor) {
        super("help", 0, "help - вывести справку по доступным серверным командам", serverHandler);
        this.executor = executor;
    }

    @Override
    public void execute(String[] args) {
        List<Command> commands = new ArrayList<>(executor.getAllCommands().values());
        commands.forEach(command -> outputWriter.writeOnSuccess(command.getInfo()));
    }
}
