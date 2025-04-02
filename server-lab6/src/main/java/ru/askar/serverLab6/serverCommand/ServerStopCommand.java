package ru.askar.serverLab6.serverCommand;

import java.io.IOException;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerStopCommand extends ServerCommand {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param inputReader
     */
    public ServerStopCommand(ServerHandler serverHandler) {
        super("stop", 0, "stop - остановить сервер", serverHandler);
    }

    @Override
    public void execute(String[] args) throws IOException {
        if (serverHandler.getStatus()) {
            System.out.println("Останавливаю сервер...");
            serverHandler.stop();
        } else {
            System.out.println("Сервер не запущен");
        }
    }
}
