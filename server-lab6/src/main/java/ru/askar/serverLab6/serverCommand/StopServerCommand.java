package ru.askar.serverLab6.serverCommand;

import ru.askar.common.cli.input.InputReader;
import ru.askar.serverLab6.connection.ServerHandler;

import java.io.IOException;

public class StopServerCommand extends ServerCommand {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param inputReader
     */
    public StopServerCommand(InputReader inputReader, ServerHandler serverHandler) {
        super("stop", 0, inputReader, serverHandler);
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

    @Override
    public String getInfo() {
        return "stop - остановить сервер";
    }
}
