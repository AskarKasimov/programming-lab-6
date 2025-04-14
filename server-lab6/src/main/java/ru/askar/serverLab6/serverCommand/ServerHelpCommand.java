package ru.askar.serverLab6.serverCommand;

import java.util.ArrayList;
import java.util.List;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.object.Command;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerHelpCommand extends ServerCommand {
    private final CommandExecutor<ServerCommand> executor;

    /** Заполнение имени и количества требуемых аргументов */
    public ServerHelpCommand(
            ServerHandler serverHandler,
            CommandExecutor<ServerCommand> executor,
            OutputWriter outputWriter) {
        super(
                "help",
                0,
                "help - вывести справку по доступным серверным командам",
                serverHandler,
                outputWriter);
        this.executor = executor;
    }

    @Override
    public CommandResponse execute(String[] args) {
        StringBuilder builder = new StringBuilder();
        List<Command> commands = new ArrayList<>(executor.getAllCommands().values());
        commands.forEach(command -> builder.append(command.getInfo()).append("\n"));
        return new CommandResponse(0, builder.toString());
    }
}
