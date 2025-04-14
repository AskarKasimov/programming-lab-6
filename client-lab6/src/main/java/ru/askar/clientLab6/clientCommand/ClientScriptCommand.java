package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.CommandResponse;
import ru.askar.common.cli.output.OutputWriter;

public class ClientScriptCommand extends ClientCommand {
    public ClientScriptCommand(ClientHandler clientHandler, OutputWriter outputWriter) {
        super(
                "execute_script",
                1,
                "execute_script filename - считать и исполнить скрипт из указанного файла",
                null,
                clientHandler,
                outputWriter);
    }

    @Override
    public CommandResponse execute(String[] args) {
        return new CommandResponse(3, "execute_script не поддерживается на клиенте");
    }
}
