package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.cli.output.OutputWriter;
import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveGreaterKeyCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public RemoveGreaterKeyCommand(CollectionManager collectionManager, OutputWriter outputWriter) {
        super(
                "remove_greater_key",
                1,
                "remove_greater_key key - удалить элементы, ключ которых превышает заданный",
                outputWriter,
                false);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        Long key = Long.parseLong(args[0]);
        int lastSize = collectionManager.getCollection().size();
        collectionManager.getCollection().entrySet().removeIf(e -> e.getKey() > key);
        if (lastSize == collectionManager.getCollection().size()) {
            outputWriter.write(
                    OutputWriter.ANSI_RED + "Элементы не найдены" + OutputWriter.ANSI_RESET);
        } else {
            outputWriter.write(
                    OutputWriter.ANSI_GREEN + "Элементы удалены" + OutputWriter.ANSI_RESET);
        }
    }
}
