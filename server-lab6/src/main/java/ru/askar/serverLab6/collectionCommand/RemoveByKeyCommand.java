package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.NoSuchKeyException;
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
    public void execute(String[] args) throws NoSuchKeyException {
        long id = Long.parseLong(args[0]);
        if (collectionManager.getCollection().remove(id) != null) {
            outputWriter.write(
                    OutputWriter.ANSI_GREEN + "Элемент удален" + OutputWriter.ANSI_RESET);
        } else {
            throw new NoSuchKeyException();
        }
    }
}
