package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.exception.*;
import ru.askar.serverLab6.collection.CollectionManager;
import ru.askar.serverLab6.model.Ticket;

public class UpdateCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super(
                "update",
                3,
                "update id name price - обновить значение элемента коллекции, id которого равен заданному",
                true);
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
        Long id = Long.parseLong(args[0]);
        Ticket oldTicket = collectionManager.getCollection().get(id);
        if (oldTicket == null) {
            outputWriter.writeOnFail("Элемент с таким id не найден");
            return;
        }
        String name = args[1];
        long price;
        try {
            price = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("В поле price требуется число");
        }
        outputWriter.writeOnSuccess("Хотите изменить данные, помимо названия и цены? (y/n): ");
        if (inputReader.getInputString().equals("y")) {
            Ticket newTicket =
                    Ticket.createTicket(
                            outputWriter,
                            inputReader,
                            id,
                            name,
                            price,
                            collectionManager.generateNextEventId(),
                            scriptMode);
            collectionManager.getCollection().put(id, newTicket);
        } else {
            oldTicket.setName(name);
            oldTicket.setPrice(price);
        }
        outputWriter.writeOnSuccess("Элемент обновлен");
    }
}
