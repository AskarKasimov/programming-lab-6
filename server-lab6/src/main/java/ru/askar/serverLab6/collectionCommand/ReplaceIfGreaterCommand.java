package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class ReplaceIfGreaterCommand extends CollectionCommand {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final CollectionManager collectionManager;

    public ReplaceIfGreaterCommand(
            InputReader inputReader,
            CollectionManager collectionManager,
            OutputWriter outputWriter) {
        super(
                "replace_if_greater",
                3,
                "replace_if_greater id name price - заменить значение по ключу, если новое значение больше старого",
                true);
        this.inputReader = inputReader;
        this.collectionManager = collectionManager;
        this.outputWriter = outputWriter;
    }

    @Override
    public CommandResponse execute(String[] args) {
        Long id = Long.parseLong(args[0]);
        Ticket oldTicket = collectionManager.getCollection().get(id);
        if (oldTicket == null) {
            return new CommandResponse(3, "Элемент с таким id не найден");
        }

        Ticket newTicket;
        try {
            newTicket =
                    Ticket.createTicket(
                            outputWriter,
                            inputReader,
                            collectionManager.generateNextTicketId(),
                            args[1],
                            Long.parseLong(args[2]),
                            collectionManager.generateNextEventId(),
                            scriptMode);
        } catch (InvalidInputFieldException | UserRejectedToFillFieldsException e) {
            return new CommandResponse(3, e.getMessage());
        }

        if (oldTicket.compareTo(newTicket) < 0) {
            collectionManager.getCollection().put(id, newTicket);
            return new CommandResponse(1, "Элемент обновлен");
        } else {
            return new CommandResponse(3, "Новое значение не больше старого");
        }
    }
}
