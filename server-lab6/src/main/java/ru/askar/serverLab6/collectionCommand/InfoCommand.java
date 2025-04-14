package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.serverLab6.collection.CollectionManager;

public class InfoCommand extends CollectionCommand {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager, OutputWriter outputWriter) {
        super(
                "info",
                0,
                "info - вывести информацию о коллекции (тип, дата инициализации, количество элементов)",
                outputWriter,
                false);
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append("Тип коллекции: ")
                .append(collectionManager.getCollection().getClass())
                .append("\nДата инициализации: ")
                .append(collectionManager.getDateOfCreation())
                .append("\nКоличество элементов: ")
                .append(collectionManager.getCollection().size());
        return new CommandResponse(0, builder.toString());
    }
}
