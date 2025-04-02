package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;

public class ClientScriptCommand extends ClientCommand {
    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param clientHandler
     */
    public ClientScriptCommand(ClientHandler clientHandler) {
        super(
                "execute_script",
                1,
                "execute_script filename - считать и исполнить скрипт из указанного файла",
                null,
                clientHandler);
    }

    @Override
    public void execute(String[] args) {}
}
