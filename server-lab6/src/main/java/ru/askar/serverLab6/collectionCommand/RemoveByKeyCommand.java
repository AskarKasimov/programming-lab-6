package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveByKeyCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public RemoveByKeyCommand(CollectionManager collectionManager, OutputWriter outputWriter) {
        super(
                "remove_key",
                1,
                "remove_key key - удалить элемент из коллекции по его id",
                outputWriter,
                false);
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String[] args) {
        long id = Long.parseLong(args[0]);
        if (collectionManager.getCollection().remove(id) != null) {
            return new CommandResponse(1, "Элемент удален");
        } else {
            return new CommandResponse(3, "Элемент с таким id не найден");
        }
    }
}
