package ru.askar.serverLab6.serverCommand;

import ru.askar.common.cli.input.InputReader;
import ru.askar.common.exception.ExitCLIException;
import ru.askar.serverLab6.connection.ServerHandler;

public class ExitCommand extends ServerCommand {
    public ExitCommand(InputReader inputReader, ServerHandler serverHandler) {
        super("exit", 0, inputReader, serverHandler);
    }

    @Override
    public void execute(String[] args) throws ExitCLIException {
        throw new ExitCLIException();
    }

    @Override
    public String getInfo() {
        return "exit - завершить программу";
    }
}
