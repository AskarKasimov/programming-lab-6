package ru.askar.serverLab6.serverCommand;

import ru.askar.common.cli.input.InputReader;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerStatusCommand extends ServerCommand {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param inputReader
     */
    public ServerStatusCommand(InputReader inputReader, ServerHandler serverHandler) {
        super("status", 0, inputReader, serverHandler);
    }

    @Override
    public void execute(String[] args) {
        if (serverHandler.getStatus()) {
            System.out.println("Сервер работает на порту " + serverHandler.getPort());
        } else {
            System.out.println("Сервер не запущен");
        }
    }

    @Override
    public String getInfo() {
        return "status - вывести информацию о состоянии сервера";
    }
}
