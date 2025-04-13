package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.exception.*;
import ru.askar.serverLab6.collection.CollectionManager;
import ru.askar.serverLab6.model.Ticket;

public class ReplaceIfGreaterCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public ReplaceIfGreaterCommand(CollectionManager collectionManager) {
        super(
                "replace_if_greater",
                3,
                "replace_if_greater id name price - заменить значение по ключу, если новое значение больше старого",
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

        Ticket newTicket =
                Ticket.createTicket(
                        outputWriter,
                        inputReader,
                        collectionManager.generateNextTicketId(),
                        args[1],
                        Long.parseLong(args[2]),
                        collectionManager.generateNextEventId(),
                        scriptMode);

        if (oldTicket.compareTo(newTicket) < 0) {
            collectionManager.getCollection().put(id, newTicket);
            outputWriter.writeOnSuccess("Элемент обновлен");
        } else {
            outputWriter.writeOnFail("Новое значение не больше старого");
        }
    }
}
