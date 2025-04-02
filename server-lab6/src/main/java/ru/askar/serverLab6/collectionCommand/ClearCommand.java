package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.Command;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.exception.CollectionIsEmptyException;
import ru.askar.serverLab6.collection.CollectionManager;

public class ClearCommand extends Command {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager, InputReader inputReader) {
        super("clear", 0, inputReader);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) throws CollectionIsEmptyException {
        if (collectionManager.getCollection().isEmpty())
            throw new CollectionIsEmptyException();
        else {
            collectionManager.getCollection().clear();
            outputWriter.writeOnSuccess("Коллекция очищена");
        }
    }

    @Override
    public String getInfo() {
        return "clear - очистить коллекцию";
    }
}
