package ru.askar.serverLab6.serverCommand;

import java.io.IOException;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.serverLab6.connection.ServerHandler;

public class ServerStopCommand extends ServerCommand {
    public ServerStopCommand(ServerHandler serverHandler, OutputWriter outputWriter) {
        super("stop", 0, "stop - остановить сервер", serverHandler, outputWriter);
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
