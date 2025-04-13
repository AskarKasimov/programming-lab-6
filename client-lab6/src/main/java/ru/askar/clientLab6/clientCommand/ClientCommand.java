package ru.askar.clientLab6.clientCommand;

import ru.askar.clientLab6.connection.ClientHandler;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.object.Command;

public abstract class ClientCommand extends Command {
    protected final ClientHandler clientHandler;

    /**
     * Заполнение имени и количества требуемых аргументов
     *
     * @param name
     * @param argsCount
     */
    public ClientCommand(
            String name,
            int argsCount,
            String info,
            InputReader inputReader,
            ClientHandler clientHandler,
            OutputWriter outputWriter) {
        super(name, argsCount, info, inputReader, outputWriter);
        this.clientHandler = clientHandler;
    }
}
