package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.output.OutputWriter;

public class ClientStartCommand extends ClientCommand {
    /** Заполнение имени и количества требуемых аргументов */
    public ClientStartCommand(ClientHandler clientHandler, OutputWriter outputWriter) {
        super(
                "start",
                2,
                "start host port - запуск клиента на указанный хост и порт",
                null,
                clientHandler,
                outputWriter);
    }

    @Override
    public CommandResponse execute(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        if (clientHandler.getStatus()) {
            return new CommandResponse(3, "Клиент уже запущен!");
        } else {
            clientHandler.setHost(host);
            clientHandler.setPort(port);
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
            return new CommandResponse(1, "Клиент запускается");
        }
    }
}
