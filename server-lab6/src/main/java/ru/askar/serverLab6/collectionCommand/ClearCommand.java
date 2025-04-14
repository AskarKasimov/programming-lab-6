package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.serverLab6.collection.CollectionManager;

public class ClearCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager, OutputWriter outputWriter) {
        super("clear", 0, "clear - очистить коллекцию", outputWriter, false);
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String[] args) {
        if (collectionManager.getCollection().isEmpty())
            return new CommandResponse(2, "Коллекция пуста");
        else {
            collectionManager.getCollection().clear();
            return new CommandResponse(1, "Коллекция очищена");
        }
    }
}
