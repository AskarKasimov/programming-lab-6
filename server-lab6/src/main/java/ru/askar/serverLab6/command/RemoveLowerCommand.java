package ru.askar.serverLab6.command;

import ru.askar.common.Command;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.exception.InvalidInputFieldException;
import ru.askar.common.exception.UserRejectedToFillFieldsException;
import ru.askar.common.object.Ticket;
import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveLowerCommand extends Command {
    private final CollectionManager collectionManager;
    private final InputReader inputReader;

    public RemoveLowerCommand(CollectionManager collectionManager, InputReader inputReader) {
        super("remove_lower", 2, inputReader);
        this.inputReader = inputReader;
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) throws InvalidInputFieldException, UserRejectedToFillFieldsException {
        Ticket ticket = Ticket.createTicket(outputWriter, inputReader, 1L, args[0], Long.parseLong(args[1]), collectionManager.generateNextEventId(), scriptMode);
        int oldSize = collectionManager.getCollection().size();
        collectionManager.getCollection().values().removeIf(t -> t.compareTo(ticket) < 0);
        if (oldSize == collectionManager.getCollection().size()) {
            outputWriter.writeOnFail("Элементы не найдены");
            return;
        }
        outputWriter.writeOnSuccess("Элементы удалены");
    }

    @Override
    public String getInfo() {
        return "remove_lower name price - удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
