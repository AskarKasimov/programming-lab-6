package ru.askar.serverLab6.command;

import ru.askar.common.Command;
import ru.askar.common.cli.input.InputReader;
import ru.askar.serverLab6.collection.CollectionManager;

public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager, InputReader inputReader) {
        super("info", 0, inputReader);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        outputWriter.writeOnSuccess("Тип коллекции: " + collectionManager.getCollection().getClass());
        outputWriter.writeOnSuccess("Дата инициализации: " + collectionManager.getDateOfCreation());
        outputWriter.writeOnSuccess("Количество элементов: " + collectionManager.getCollection().size());
    }

    @Override
    public String getInfo() {
        return "info - вывести информацию о коллекции (тип, дата инициализации, количество элементов)";
    }
}
