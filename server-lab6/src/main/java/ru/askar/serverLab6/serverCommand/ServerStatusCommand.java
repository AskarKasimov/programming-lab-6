package ru.askar.serverLab6.serverCommand;

import ru.askar.serverLab6.connection.ServerHandler;

public class ServerStatusCommand extends ServerCommand {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param inputReader
     */
    public ServerStatusCommand(ServerHandler serverHandler) {
        super("status", 0, "status - вывести информацию о состоянии сервера", serverHandler);
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
