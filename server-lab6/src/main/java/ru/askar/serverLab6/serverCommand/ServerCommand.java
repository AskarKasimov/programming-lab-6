package ru.askar.serverLab6.serverCommand;

import ru.askar.common.object.Command;
import ru.askar.serverLab6.connection.ServerHandler;

public abstract class ServerCommand extends Command {
    protected final ServerHandler serverHandler;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     */
    public ServerCommand(String name, int argsCount, String info, ServerHandler serverHandler) {
        super(name, argsCount, info, null);
        this.serverHandler = serverHandler;
    }
}
