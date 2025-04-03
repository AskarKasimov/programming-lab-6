package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.exception.*;
import ru.askar.serverLab6.collection.CollectionManager;

public class ClearCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", 0, "clear - очистить коллекцию", false);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args)
            throws CollectionIsEmptyException,
                    NoSuchKeyException,
                    InvalidInputFieldException,
                    ExitCLIException,
                    IOException,
                    UserRejectedToFillFieldsException {
        super.execute(args);
        if (collectionManager.getCollection().isEmpty()) throw new CollectionIsEmptyException();
        else {
            collectionManager.getCollection().clear();
            outputWriter.writeOnSuccess("Коллекция очищена");
        }
    }
}
