package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class UpdateCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager, OutputWriter outputWriter) {
        super(
                "update",
                3,
                "update id name price - обновить значение элемента коллекции, id которого равен заданному",
                outputWriter,
                true);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args)
            throws InvalidInputFieldException, UserRejectedToFillFieldsException {
        Long id = Long.parseLong(args[0]);
        Ticket oldTicket = collectionManager.getCollection().get(id);
        if (oldTicket == null) {
            outputWriter.write(
                    OutputWriter.ANSI_RED
                            + "Элемент с таким id не найден"
                            + OutputWriter.ANSI_RESET);
            return;
        }
        String name = args[1];
        long price;
        try {
            price = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("В поле price требуется число");
        }
        outputWriter.write(
                OutputWriter.ANSI_GREEN
                        + "Хотите изменить данные, помимо названия и цены? (y/n): "
                        + OutputWriter.ANSI_RESET);
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
        outputWriter.write(OutputWriter.ANSI_GREEN + "Элемент обновлен" + OutputWriter.ANSI_RESET);
    }
}
