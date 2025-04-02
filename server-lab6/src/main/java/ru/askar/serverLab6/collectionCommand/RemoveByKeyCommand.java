package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.object.Command;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.exception.NoSuchKeyException;
import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveByKeyCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveByKeyCommand(CollectionManager collectionManager, InputReader inputReader) {
        super("remove_key", 1, inputReader);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) throws NoSuchKeyException {
        long id = Long.parseLong(args[0]);
        if (collectionManager.getCollection().remove(id) != null) {
            outputWriter.writeOnSuccess("Элемент удален");
        } else {
            throw new NoSuchKeyException();
        }
    }

    @Override
    public String getInfo() {
        return "remove_key key - удалить элемент из коллекции по его id";
    }
}
