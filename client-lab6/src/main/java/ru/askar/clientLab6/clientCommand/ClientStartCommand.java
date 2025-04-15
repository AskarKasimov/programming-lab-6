package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.CommandResponseCode;

public class ClientStartCommand extends ClientCommand {
    public ClientStartCommand(ClientHandler clientHandler) {
        super(
                "start",
                2,
                "start host port - запуск клиента на указанный хост и порт",
                clientHandler);
    }

    @Override
    public CommandResponse execute(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        if (clientHandler.getRunning()) {
            return new CommandResponse(CommandResponseCode.ERROR, "Клиент уже запущен!");
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
            return new CommandResponse(CommandResponseCode.SUCCESS, "Клиент запускается");
        }
    }
}
