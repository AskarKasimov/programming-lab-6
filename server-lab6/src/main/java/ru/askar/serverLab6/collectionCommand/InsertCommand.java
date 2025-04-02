package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class InsertCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public InsertCommand(CollectionManager collectionManager) {
        super("insert", 3, "insert id?null name price - добавить новый элемент");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args)
            throws InvalidInputFieldException, UserRejectedToFillFieldsException {
        String name = args[1];
        long price;
        try {
            price = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("В поле price требуется число");
        }

        Long id;
        if (args[0].equals("null")) {
            id = collectionManager.generateNextTicketId();
            outputWriter.writeOnWarning(
                    "id не был указан, поэтому он был сгенерирован автоматически (минимальный из отсутствующих): "
                            + id);
        } else {
            id = Long.parseLong(args[0]);
            if (collectionManager.getCollection().containsKey(id)) {
                throw new IllegalArgumentException("Такой id уже существует");
            }
        }

        Ticket ticket =
                Ticket.createTicket(
                        outputWriter,
                        inputReader,
                        id,
                        name,
                        price,
                        collectionManager.generateNextEventId(),
                        scriptMode);
        collectionManager.getCollection().put(ticket.getId(), ticket);
        outputWriter.writeOnSuccess("Элемент добавлен в коллекцию");
    }
}
