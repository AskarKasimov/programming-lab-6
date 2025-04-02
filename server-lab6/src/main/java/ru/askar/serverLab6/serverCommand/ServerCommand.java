package ru.askar.serverLab6.serverCommand;

import ru.askar.common.cli.input.InputReader;
import ru.askar.common.object.Command;
import ru.askar.serverLab6.connection.ServerHandler;

public abstract class ServerCommand extends Command {
    protected final ServerHandler serverHandler;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     * @param inputReader
     */
    public ServerCommand(String name, int argsCount, InputReader inputReader, ServerHandler serverHandler) {
        super(name, argsCount, inputReader);
        this.serverHandler = serverHandler;
    }
}
