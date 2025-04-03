package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.exception.*;
import ru.askar.serverLab6.collection.CollectionManager;

public class InsertCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public InsertCommand(CollectionManager collectionManager) {
        super("insert", 3, "insert id?null name price - добавить новый элемент", true);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args)
            throws InvalidInputFieldException,
                    UserRejectedToFillFieldsException,
                    NoSuchKeyException,
                    CollectionIsEmptyException,
                    ExitCLIException,
                    IOException {
        super.execute(args);
        collectionManager.getCollection().put(object.getId(), object);
        outputWriter.writeOnSuccess("Элемент добавлен в коллекцию");
    }
}
