package ru.askar.clientLab6.connection;

import java.io.IOException;

public interface ClientHandler {
    void start() throws IOException;

    void stop() throws IOException;

    boolean getRunning();

    int getPort();

    void setPort(int port);

    String getHost();

    void setHost(String host);

    void sendMessage(Object object) throws IOException;
}
