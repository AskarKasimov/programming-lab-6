package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.exception.CollectionIsEmptyException;
import ru.askar.serverLab6.collection.CollectionManager;

public class ClearCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", 0, "clear - очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) throws CollectionIsEmptyException {
        if (collectionManager.getCollection().isEmpty()) throw new CollectionIsEmptyException();
        else {
            collectionManager.getCollection().clear();
            outputWriter.writeOnSuccess("Коллекция очищена");
        }
    }
}
