package ru.askar.clientLab6.connection;

import java.io.IOException;

public interface ClientHandler {
    void start() throws IOException;

    void stop();

    boolean getRunning();

    void setPort(int port);

    void setHost(String host);

    void sendMessage(Object object) throws IOException;
}
