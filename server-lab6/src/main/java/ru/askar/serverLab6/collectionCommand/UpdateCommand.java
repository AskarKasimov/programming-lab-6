package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.input.InputReader;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.serverLab6.collection.CollectionManager;

public class UpdateCommand extends CollectionCommand {
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final CollectionManager collectionManager;

    public UpdateCommand(
            InputReader inputReader,
            CollectionManager collectionManager,
            OutputWriter outputWriter) {
        super(
                "update",
                3,
                "update id name price - обновить значение элемента коллекции, id которого равен заданному",
                true);
        this.inputReader = inputReader;
        this.collectionManager = collectionManager;
        this.outputWriter = outputWriter;
    }

    @Override
    public CommandResponse execute(String[] args) {
        return null;
    }
}
