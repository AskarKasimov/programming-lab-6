package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class InsertCommand extends CollectionCommand {
    private final InputReader inputReader;
    private final CollectionManager collectionManager;
    private final OutputWriter outputWriter;

    public InsertCommand(
            InputReader inputReader,
            CollectionManager collectionManager,
            OutputWriter outputWriter) {
        super("insert", 3, "insert id?null name price - добавить новый элемент", true);
        this.inputReader = inputReader;
        this.collectionManager = collectionManager;
        this.outputWriter = outputWriter;
    }

    @Override
    public CommandResponse execute(String[] args) {
        String name = args[1];
        long price;
        try {
            price = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            return new CommandResponse(3, "В поле price требуется число");
        }

        Long id;
        if (args[0].equals("null")) {
            id = collectionManager.generateNextTicketId();
        } else {
            id = Long.parseLong(args[0]);
            if (collectionManager.getCollection().containsKey(id)) {
                return new CommandResponse(3, "Такой id уже существует");
            }
        }

        Ticket ticket;
        try {
            ticket =
                    Ticket.createTicket(
                            outputWriter,
                            inputReader,
                            id,
                            name,
                            price,
                            collectionManager.generateNextEventId(),
                            scriptMode);
        } catch (InvalidInputFieldException | UserRejectedToFillFieldsException e) {
            return new CommandResponse(3, e.getMessage());
        }
        collectionManager.getCollection().put(ticket.getId(), ticket);
        return new CommandResponse(1, "Элемент добавлен в коллекцию (id=" + ticket.getId() + ")");
    }
}
