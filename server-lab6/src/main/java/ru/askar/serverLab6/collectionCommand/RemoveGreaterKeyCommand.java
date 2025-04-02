package ru.askar.serverLab6.collectionCommand;

import ru.askar.serverLab6.collection.CollectionManager;

public class RemoveGreaterKeyCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public RemoveGreaterKeyCommand(CollectionManager collectionManager) {
        super("remove_greater_key", 1);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        Long key = Long.parseLong(args[0]);
        int lastSize = collectionManager.getCollection().size();
        collectionManager.getCollection().entrySet().removeIf(e -> e.getKey() > key);
        if (lastSize == collectionManager.getCollection().size()) {
            outputWriter.writeOnFail("Элементы не найдены");
        } else {
            outputWriter.writeOnSuccess("Элементы удалены");
        }
    }

    @Override
    public String getInfo() {
        return "remove_greater_key key - удалить элементы, ключ которых превышает заданный";
    }
}
