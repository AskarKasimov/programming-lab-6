package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class ReplaceIfGreaterCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public ReplaceIfGreaterCommand(CollectionManager collectionManager, OutputWriter outputWriter) {
        super(
                "replace_if_greater",
                3,
                "replace_if_greater id name price - заменить значение по ключу, если новое значение больше старого",
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
            outputWriter.write(
                    OutputWriter.ANSI_GREEN + "Элемент обновлен" + OutputWriter.ANSI_RESET);
        } else {
            outputWriter.write(
                    OutputWriter.ANSI_RED
                            + "Новое значение не больше старого"
                            + OutputWriter.ANSI_RESET);
        }
    }
}
