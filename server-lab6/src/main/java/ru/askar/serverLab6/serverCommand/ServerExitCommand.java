package ru.askar.serverLab6.serverCommand;

import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.ExitCLIException;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerExitCommand extends ServerCommand {
    public ServerExitCommand(ServerHandler serverHandler, OutputWriter outputWriter) {
        super("exit", 0, "exit - завершить программу", serverHandler, outputWriter);
    }

    @Override
    public void execute(String[] args) throws ExitCLIException {
        throw new ExitCLIException();
    }
}
