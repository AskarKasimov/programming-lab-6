package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class UpdateCommand extends CollectionCommand {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final CollectionManager collectionManager;

    public UpdateCommand(
            InputReader inputReader,
            CollectionManager collectionManager,
            OutputWriter outputWriter) {
        super(
                "update",
                3,
                "update id name price - обновить значение элемента коллекции, id которого равен заданному",
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
        String name = args[1];
        long price;
        try {
            price = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            return new CommandResponse(3, "В поле price требуется число");
        }
        outputWriter.write(
                OutputWriter.ANSI_GREEN
                        + "Хотите изменить данные, помимо названия и цены? (y/n): "
                        + OutputWriter.ANSI_RESET);
        if (inputReader.getInputString().equals("y")) {
            Ticket newTicket;
            try {
                newTicket =
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
            collectionManager.getCollection().put(id, newTicket);
        } else {
            try {
                oldTicket.setName(name);
            } catch (InvalidInputFieldException e) {
                return new CommandResponse(3, e.getMessage());
            }
            try {
                oldTicket.setPrice(price);
            } catch (InvalidInputFieldException e) {
                return new CommandResponse(3, e.getMessage());
            }
        }
        return new CommandResponse(1, "Элемент обновлен");
    }
}
