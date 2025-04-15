package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.CommandResponseCode;
import ru.askar.common.exception.InvalidInputFieldException;
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
        if (object == null)
            return new CommandResponse(
                    CommandResponseCode.ERROR, "Данной команде требуется объект!");
        Long idToUpdate = object.getId();
        if (collectionManager.getCollection().get(idToUpdate) == null) {
            return new CommandResponse(CommandResponseCode.ERROR, "Элемент с таким id не найден");
        }
        if (object.getEvent() != null && object.getEvent().getId() == null) {
            object.getEvent().setId(collectionManager.generateNextEventId());
        }
        try {
            collectionManager.remove(idToUpdate);
            collectionManager.put(object);
        } catch (InvalidInputFieldException e) {
            return new CommandResponse(CommandResponseCode.ERROR, e.getMessage());
        }
        return new CommandResponse(CommandResponseCode.SUCCESS, "Элемент заменён");
    }
}
