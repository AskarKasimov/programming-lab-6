package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.cli.input.InputReader;

public class ClientStartCommand extends ClientCommand {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param inputReader
     */
    public ClientStartCommand(InputReader inputReader, ClientHandler clientHandler) {
        super("start", 2, inputReader, clientHandler);
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
            Thread handlerThread = new Thread(() -> {
                try {
                    clientHandler.start();
                } catch (Exception e) {
                    System.out.println("Ошибка при запуске клиента: " + e.getMessage());
                }
            });
            handlerThread.start();
        }
    }

    @Override
    public String getInfo() {
        return "start host port - запуск клиента на указанный хост и порт";
    }
}
