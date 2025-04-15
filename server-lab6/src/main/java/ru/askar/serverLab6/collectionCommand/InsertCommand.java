package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.serverLab6.collection.CollectionManager;

public class InsertCommand extends ObjectCollectionCommand {
    public InsertCommand(CollectionManager collectionManager) {
        super("insert", 3, "insert id?null name price - добавить новый элемент", collectionManager);
    }

    @Override
    public CommandResponse execute(String[] args) {
        if (object == null) return new CommandResponse(3, "Данной команде требуется объект!");
        if (object.getId() == null) {
            object.setId(collectionManager.generateNextTicketId());
        }
        if (object.getEvent() != null && object.getEvent().getId() == null) {
            object.getEvent().setId(collectionManager.generateNextEventId());
        }
        try {
            collectionManager.put(object);
        } catch (InvalidInputFieldException e) {
            return new CommandResponse(3, e.getMessage());
        }
        return new CommandResponse(1, "Элемент добавлен");
    }
}
