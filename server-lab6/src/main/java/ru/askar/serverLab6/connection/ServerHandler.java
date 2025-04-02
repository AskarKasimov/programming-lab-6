package ru.askar.serverLab6.connection;

import ru.askar.common.CommandDTO;

import java.io.IOException;

public interface ServerHandler {
    void start() throws IOException;

    void stop() throws IOException;

    boolean getStatus();

    int getPort();

    void setPort(int port);

    void sendMessage(CommandDTO object) throws IOException;
}
