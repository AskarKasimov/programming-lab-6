package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveLowerCommand extends CollectionCommand {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(
            InputReader inputReader,
            CollectionManager collectionManager,
            OutputWriter outputWriter) {
        super(
                "remove_lower",
                2,
                "remove_lower name price - удалить из коллекции все элементы, меньшие, чем заданный",
                false);
        this.inputReader = inputReader;
        this.collectionManager = collectionManager;
        this.outputWriter = outputWriter;
    }

    @Override
    public CommandResponse execute(String[] args) {
        Ticket ticket;
        try {
            ticket =
                    Ticket.createTicket(
                            outputWriter,
                            inputReader,
                            1L,
                            args[0],
                            Long.parseLong(args[1]),
                            collectionManager.generateNextEventId(),
                            scriptMode);
        } catch (InvalidInputFieldException | UserRejectedToFillFieldsException e) {
            return new CommandResponse(3, e.getMessage());
        }
        int oldSize = collectionManager.getCollection().size();
        collectionManager.getCollection().values().removeIf(t -> t.compareTo(ticket) < 0);
        if (oldSize == collectionManager.getCollection().size()) {
            return new CommandResponse(3, "Элементы не найдены");
        }
        return new CommandResponse(1, "Элементы удалены");
    }
}
