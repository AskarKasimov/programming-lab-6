package ru.askar.serverLab6.serverCommand;

import ru.askar.common.object.Command;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.common.cli.input.InputReader;
import ru.askar.serverLab6.connection.ServerHandler;

import java.util.ArrayList;
import java.util.List;

public class ServerHelpCommand extends ServerCommand {
    private final CommandExecutor executor;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param inputReader
     */
    public ServerHelpCommand(InputReader inputReader, ServerHandler serverHandler, CommandExecutor executor) {
        super("help", 0, inputReader, serverHandler);
        this.executor = executor;
    }

    @Override
    public void execute(String[] args) {
        List<Command> commands = new ArrayList<>(executor.getAllCommands().values());
        commands.forEach(command -> outputWriter.writeOnSuccess(command.getInfo()));
    }

    @Override
    public String getInfo() {
        return "status - вывести информацию о состоянии сервера";
    }
}
