package ru.askar.serverLab6.serverCommand;

import ru.askar.common.exception.ExitCLIException;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerExitCommand extends ServerCommand {
    public ServerExitCommand(ServerHandler serverHandler) {
        super("exit", 0, "exit - завершить программу", serverHandler);
    }

    @Override
    public void execute(String[] args) throws ExitCLIException {
        throw new ExitCLIException();
    }
}
