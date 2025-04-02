package ru.askar.serverLab6.serverCommand;

import ru.askar.common.cli.input.InputReader;
import ru.askar.serverLab6.connection.ServerHandler;

public class StartServerCommand extends ServerCommand {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param inputReader
     */
    public StartServerCommand(InputReader inputReader, ServerHandler serverHandler) {
        super("start", 1, inputReader, serverHandler);
    }

    @Override
    public void execute(String[] args) {
        int port = Integer.parseInt(args[0]);

        if (serverHandler.getStatus()) {
            System.out.println("Сервер уже запущен на порту " + serverHandler.getPort());
        } else {
            serverHandler.setPort(port);
            System.out.println("Запускаю...");
            Thread handlerThread = new Thread(() -> {
                try {
                    serverHandler.start();
                } catch (Exception e) {
                    System.out.println("Ошибка при запуске сервера: " + e.getMessage());
                }
            });
            handlerThread.start();
        }
    }

    @Override
    public String getInfo() {
        return "start port - запуск сервера на указанном порту";
    }
}
