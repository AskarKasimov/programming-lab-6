package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;

public class ClientStartCommand extends ClientCommand {
    /** Заполнение имени и количества требуемых аргументов */
    public ClientStartCommand(ClientHandler clientHandler) {
        super(
                "start",
                2,
                "start host port - запуск клиента на указанный хост и порт",
                null,
                clientHandler);
    }

    @Override
    public void execute(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        if (clientHandler.getStatus()) {
            System.out.println("Клиент уже запущен!");
        } else {
            clientHandler.setHost(host);
            clientHandler.setPort(port);
            System.out.println("Запускаю...");
            Thread handlerThread =
                    new Thread(
                            () -> {
                                try {
                                    clientHandler.start();
                                } catch (Exception e) {
                                    System.out.println(
                                            "Ошибка при запуске клиента: " + e.getMessage());
                                }
                            });
            handlerThread.start();
        }
    }
}
