package ru.askar.serverLab6.serverCommand;

import ru.askar.common.cli.output.OutputWriter;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerStatusCommand extends ServerCommand {
    public ServerStatusCommand(ServerHandler serverHandler, OutputWriter outputWriter) {
        super(
                "status",
                0,
                "status - вывести информацию о состоянии сервера",
                serverHandler,
                outputWriter);
    }

    @Override
    public void execute(String[] args) {
        if (serverHandler.getStatus()) {
            System.out.println("Сервер работает на порту " + serverHandler.getPort());
        } else {
            System.out.println("Сервер не запущен");
        }
    }
}
