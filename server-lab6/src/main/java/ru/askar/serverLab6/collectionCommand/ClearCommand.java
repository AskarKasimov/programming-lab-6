package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.CollectionIsEmptyException;
import ru.askar.serverLab6.collection.CollectionManager;

public class ClearCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager, OutputWriter outputWriter) {
        super("clear", 0, "clear - очистить коллекцию", outputWriter, false);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) throws CollectionIsEmptyException {
        if (collectionManager.getCollection().isEmpty()) throw new CollectionIsEmptyException();
        else {
            collectionManager.getCollection().clear();
            outputWriter.write(
                    OutputWriter.ANSI_GREEN + "Коллекция очищена" + OutputWriter.ANSI_RESET);
        }
    }
}
