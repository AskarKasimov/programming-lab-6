package ru.askar.serverLab6.serverCommand;

import java.io.IOException;
import ru.askar.common.CommandResponse;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerStopCommand extends ServerCommand {
    public ServerStopCommand(ServerHandler serverHandler) {
        super("stop", 0, "stop - остановить сервер", serverHandler);
    }

    @Override
    public CommandResponse execute(String[] args) {
        if (serverHandler.getStatus()) {
            try {
                serverHandler.stop();
            } catch (IOException e) {
                return new CommandResponse(3, e.getMessage());
            }
        } else {
            return new CommandResponse(3, "Сервер не запущен");
        }
        return new CommandResponse(1, "Сервер остановлен");
    }
}
