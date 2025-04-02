package ru.askar.serverLab6.command;

import ru.askar.common.Command;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class InsertCommand extends Command {
    private final CollectionManager collectionManager;
    private final InputReader inputReader;

    public InsertCommand(CollectionManager collectionManager, InputReader inputReader) {
        super("insert", 3, inputReader);
        this.inputReader = inputReader;
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) throws InvalidInputFieldException, UserRejectedToFillFieldsException {
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
            outputWriter.writeOnWarning("id не был указан, поэтому он был сгенерирован автоматически (минимальный из отсутствующих): " + id);
        } else {
            id = Long.parseLong(args[0]);
            if (collectionManager.getCollection().containsKey(id)) {
                throw new IllegalArgumentException("Такой id уже существует");
            }
        }

        Ticket ticket = Ticket.createTicket(outputWriter, inputReader, id, name, price, collectionManager.generateNextEventId(), scriptMode);
        collectionManager.getCollection().put(ticket.getId(), ticket);
        outputWriter.writeOnSuccess("Элемент добавлен в коллекцию");
    }

    @Override
    public String getInfo() {
        return "insert id?null name price - добавить новый элемент";
    }
}
