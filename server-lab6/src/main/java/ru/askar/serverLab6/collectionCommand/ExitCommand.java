package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.CommandResponse;
import ru.askar.serverLab6.connection.ServerHandler;

public class ExitCommand extends CollectionCommand {
    private final ServerHandler serverHandler;

    public ExitCommand(ServerHandler serverHandler) {
        super("exit", 0, "exit - завершить программу (без сохранения в файл)", false);
        this.serverHandler = serverHandler;
    }

    @Override
    public CommandResponse execute(String[] args) {
        try {
            serverHandler.stop();
        } catch (IOException e) {
            return new CommandResponse(3, e.getMessage());
        }
        return new CommandResponse(0, "Завершение работы программы");
    }
}
