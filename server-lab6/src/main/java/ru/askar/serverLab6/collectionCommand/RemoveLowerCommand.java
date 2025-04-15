package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveLowerCommand extends ObjectCollectionCommand {
    public RemoveLowerCommand(CollectionManager collectionManager) {
        super(
                "remove_lower",
                2,
                "remove_lower name price - удалить из коллекции все элементы, меньшие, чем заданный",
                collectionManager);
    }

    @Override
    public CommandResponse execute(String[] args) {
        return null;
    }
}
