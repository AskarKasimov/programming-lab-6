package ru.askar.serverLab6.collectionCommand;

import java.io.IOException;
import ru.askar.common.exception.*;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveLowerCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager) {
        super(
                "remove_lower",
                2,
                "remove_lower name price - удалить из коллекции все элементы, меньшие, чем заданный",
                false);
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
        Ticket ticket =
                Ticket.createTicket(
                        outputWriter,
                        inputReader,
                        1L,
                        args[0],
                        Long.parseLong(args[1]),
                        collectionManager.generateNextEventId(),
                        scriptMode);
        int oldSize = collectionManager.getCollection().size();
        collectionManager.getCollection().values().removeIf(t -> t.compareTo(ticket) < 0);
        if (oldSize == collectionManager.getCollection().size()) {
            outputWriter.writeOnFail("Элементы не найдены");
            return;
        }
        outputWriter.writeOnSuccess("Элементы удалены");
    }
}
