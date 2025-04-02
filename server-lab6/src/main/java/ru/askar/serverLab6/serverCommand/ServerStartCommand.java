package ru.askar.serverLab6.serverCommand;

import ru.askar.serverLab6.connection.ServerHandler;

public class ServerStartCommand extends ServerCommand {
    /** Заполнение имени и количества требуемых аргументов */
    public ServerStartCommand(ServerHandler serverHandler) {
        super("start", 1, "start port - запуск сервера на указанном порту", serverHandler);
    }

    @Override
    public void execute(String[] args) {
        int port = Integer.parseInt(args[0]);

        if (serverHandler.getStatus()) {
            System.out.println("Сервер уже запущен на порту " + serverHandler.getPort());
        } else {
            serverHandler.setPort(port);
            System.out.println("Запускаю...");
            Thread handlerThread =
                    new Thread(
                            () -> {
                                try {
                                    serverHandler.start();
                                } catch (Exception e) {
                                    System.out.println(
                                            "Ошибка при запуске сервера: " + e.getMessage());
                                }
                            });
            handlerThread.start();
        }
    }
}
