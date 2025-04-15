package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.CommandResponseCode;
import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveByKeyCommand extends CollectionCommand {
    public RemoveByKeyCommand(CollectionManager collectionManager) {
        super(
                "remove_key",
                1,
                "remove_key key - удалить элемент из коллекции по его id",
                collectionManager);
    }

    @Override
    public CommandResponse execute(String[] args) {
        long id = Long.parseLong(args[0]);
        if (collectionManager.getCollection().remove(id) != null) {
            return new CommandResponse(CommandResponseCode.SUCCESS, "Элемент удален");
        } else {
            return new CommandResponse(CommandResponseCode.ERROR, "Элемент с таким id не найден");
        }
    }
}
