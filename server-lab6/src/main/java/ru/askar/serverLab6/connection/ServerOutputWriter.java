package ru.askar.serverLab6.connection;

import java.io.IOException;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.output.OutputWriter;

public class ServerOutputWriter implements OutputWriter {
    private final ServerHandler serverHandler;

    public ServerOutputWriter(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public void write(Object message) {
        try {
            if (message instanceof CommandResponse) {
                serverHandler.sendMessage(message);
                System.out.println("Сообщение отправлено");
            } else {
                serverHandler.sendMessage(message);
                System.out.println("Сообщение почему-то строка");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
