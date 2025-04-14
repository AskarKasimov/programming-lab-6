package ru.askar.serverLab6.serverCommand;

import ru.askar.common.CommandResponse;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerStatusCommand extends ServerCommand {
    public ServerStatusCommand(ServerHandler serverHandler) {
        super("status", 0, "status - вывести информацию о состоянии сервера", serverHandler);
    }

    @Override
    public CommandResponse execute(String[] args) {
        if (serverHandler.getStatus()) {
            return new CommandResponse(0, "Сервер работает на порту " + serverHandler.getPort());
        } else {
            return new CommandResponse(0, "Сервер не запущен");
        }
    }
}
