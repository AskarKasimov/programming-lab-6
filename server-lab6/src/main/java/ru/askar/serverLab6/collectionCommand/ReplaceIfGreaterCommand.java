package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.serverLab6.collection.CollectionManager;

public class ReplaceIfGreaterCommand extends ObjectCollectionCommand {
    public ReplaceIfGreaterCommand(CollectionManager collectionManager) {
        super(
                "replace_if_greater",
                3,
                "replace_if_greater id name price - заменить значение по ключу, если новое значение больше старого",
                collectionManager);
    }

    @Override
    public CommandResponse execute(String[] args) {
        return null;
    }
}
