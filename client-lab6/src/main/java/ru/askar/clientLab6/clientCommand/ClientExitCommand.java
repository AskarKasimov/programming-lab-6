package ru.askar.clientLab6.clientCommand;

import java.io.IOException;
import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.CommandResponse;

public class ClientExitCommand extends ClientCommand {
    public ClientExitCommand(ClientHandler clientHandler) {
        super("exit", 0, "exit - завершить программу", clientHandler);
    }

    @Override
    public CommandResponse execute(String[] args) {
        if (!clientHandler.getRunning()) {
            return new CommandResponse(3, "Клиент не подключен к серверу");
        }
        try {
            clientHandler.stop();
        } catch (IOException e) {
            return new CommandResponse(3, e.getMessage());
        }
        return new CommandResponse(1, "Отключён от сервера");
    }
}
