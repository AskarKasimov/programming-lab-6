package ru.askar.serverLab6.collectionCommand;

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
    public void execute(String[] args) {
        outputWriter.write(
                OutputWriter.ANSI_GREEN
                        + "Тип коллекции: "
                        + collectionManager.getCollection().getClass()
                        + OutputWriter.ANSI_RESET);
        outputWriter.write(
                OutputWriter.ANSI_GREEN
                        + "Дата инициализации: "
                        + collectionManager.getDateOfCreation()
                        + OutputWriter.ANSI_RESET);
        outputWriter.write(
                OutputWriter.ANSI_GREEN
                        + "Количество элементов: "
                        + collectionManager.getCollection().size()
                        + OutputWriter.ANSI_RESET);
    }
}
