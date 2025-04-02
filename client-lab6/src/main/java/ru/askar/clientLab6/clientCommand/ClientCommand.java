package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.object.Command;

public abstract class ClientCommand extends Command {
    protected final ClientHandler clientHandler;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     * @param inputReader
     */
    public ClientCommand(String name, int argsCount, InputReader inputReader, ClientHandler clientHandler) {
        super(name, argsCount, inputReader);
        this.clientHandler = clientHandler;
    }
}
