package ru.askar.serverLab6.connection;

public interface ServerHandler {
    void start();

    void stop();

    boolean getStatus();

    int getPort();

    void setPort(int port);
}
