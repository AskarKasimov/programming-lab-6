package ru.askar.serverLab6.collectionCommand;

import ru.askar.serverLab6.collection.CollectionManager;

public class InfoCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super(
                "info",
                0,
                "info - вывести информацию о коллекции (тип, дата инициализации, количество элементов)",
                false);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        outputWriter.writeOnSuccess(
                "Тип коллекции: " + collectionManager.getCollection().getClass());
        outputWriter.writeOnSuccess("Дата инициализации: " + collectionManager.getDateOfCreation());
        outputWriter.writeOnSuccess(
                "Количество элементов: " + collectionManager.getCollection().size());
    }
}
