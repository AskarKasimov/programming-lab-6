package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.CommandResponseCode;

public class ClientScriptCommand extends ClientCommand {
    public ClientScriptCommand(ClientHandler clientHandler) {
        super(
                "execute_script",
                1,
                "execute_script filename - считать и исполнить скрипт из указанного файла",
                clientHandler);
    }

    @Override
    public CommandResponse execute(String[] args) {
        return new CommandResponse(
                CommandResponseCode.ERROR, "execute_script не поддерживается на клиенте");
    }
}
