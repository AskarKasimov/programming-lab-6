package ru.askar.serverLab6.collectionCommand;

import ru.askar.common.CommandResponse;
import ru.askar.common.cli.output.OutputWriter;
import ru.askar.common.exception.ExitCLIException;

public class ExitCommand extends CollectionCommand {
    public ExitCommand(OutputWriter outputWriter) {
        super("exit", 0, "exit - завершить программу (без сохранения в файл)", outputWriter, false);
    }

    @Override
    public CommandResponse execute(String[] args) throws ExitCLIException {
        throw new ExitCLIException();
    }
}
