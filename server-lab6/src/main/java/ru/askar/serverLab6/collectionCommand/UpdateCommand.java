package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.serverLab6.collection.CollectionManager;

public class UpdateCommand extends ObjectCollectionCommand {
    public UpdateCommand(CollectionManager collectionManager) {
        super(
                "update",
                3,
                "update id name price - обновить значение элемента коллекции, id которого равен заданному",
                collectionManager);
    }

    @Override
    public CommandResponse execute(String[] args) {
        return null;
    }
}
